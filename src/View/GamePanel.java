package View;

import Model.Level;
import Model.Settings;
import Model.Tile.Tile;
import Model.Tile.Wall;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Unit;
import Model.Unit.Zombie.Zombie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class handles drawing the actual game. It is always centered on the PLAYER.
 * This class is managed by the ViewManager. Nothing else has access to it.
 * This class reads from its copy of the current level and draws all sub components.
 */
class GamePanel extends JPanel
{

  /**
   * List of tiles in map. This might need to change to a different Collection type.
   */
  private Level level;

  /**
   * This will need to shift as the PLAYER moves.
   */
  private Rectangle2D viewWindow;
  private Point center;
  private Point corner;

  private Area blackMask;
  private static final BufferedImage CIRCULAR_GRADIENT;

  private double windowScale;

  static
  {
    BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(GamePanel.class.getResourceAsStream("CircularGradient.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    CIRCULAR_GRADIENT = imageTemp;
  }


  /**
   * Setup the new GamePanel where the game will be drawn.
   */
  GamePanel()
  {
    super();
    //Set to red to distinguish from ScoreBar for now.
    setBackground(Color.BLACK);

    viewWindow = new Rectangle(0, 0, Settings.WIDTH_STANDARD, Settings.HEIGHT_STANDARD);
    center = new Point();
    corner = new Point();
    setPreferredSize(new Dimension(Settings.WIDTH_STANDARD, Settings.HEIGHT_STANDARD));
  }

  private void setViewWindow()
  {

    viewWindow.setFrameFromCenter(center, corner);
  }

  /**
   * Paints all the units and tiles.
   * <p>
   * It loops over all game objects and draws them if they are within the scene.
   * If an object is inside our determined view window it is drawn.
   * <p>
   * Level is used here constantly.
   *
   * @param graphics the graphics of the JPanel.
   */
  @Override
  public void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);

    Graphics2D graphics2D = (Graphics2D) graphics;

    if (level != null)
    {
      synchronized (level)
      {
        //Calculate scale factor of the resized window.
        this.getSize();
        windowScale = Settings.WIDTH_STANDARD / (double) this.getWidth();

        if (level.PLAYER != null)
        {

          center.setLocation(level.PLAYER.getCenterLocation());

          corner.setLocation(center);
          double dynamicHeight = this.getHeight() * windowScale;
          corner.translate((-1) * Settings.WIDTH_STANDARD / 2, (int) ((-1) * dynamicHeight / 2));

          setViewWindow();

          Rectangle2D sightBox = new Rectangle();
          Point sightCorner = new Point(center);
          sightCorner.translate((int) Settings.sightRange, (int) Settings.sightRange);
          sightBox.setFrameFromCenter(center, sightCorner);
          blackMask = new Area(new Rectangle(scaleX((int) viewWindow.getX()), scaleY((int) viewWindow.getY()), (int) (viewWindow.getWidth() / windowScale), (int) (viewWindow.getHeight() / windowScale)));
          Area circleMask = new Area(new Rectangle(scaleX((int) viewWindow.getX()), scaleY((int) viewWindow.getY()), (int) (viewWindow.getWidth() / windowScale), (int) (viewWindow.getHeight() / windowScale)));
          circleMask.subtract(new Area(new Ellipse2D.Double(scaleX((int) sightBox.getX()), scaleY((int) sightBox.getY()), sightBox.getWidth() / windowScale, sightBox.getHeight() / windowScale)));

          LinkedList<Tile> walls = new LinkedList<>();

          for (int i = 0; i < level.TILES.length; i++)
          {
            for (int j = 0; j < level.TILES[i].length; j++)
            {
              if (level.TILES[i][j] != null && level.TILES[i][j].checkCollision(sightBox))
              {
                if (!(level.TILES[i][j] instanceof Wall))
                {
                  scaleAndDrawImage(level.TILES[i][j].getImage(), graphics, level.TILES[i][j].getLocation(), level.TILES[i][j].getSize());
                }
                else
                {
                  walls.add(level.TILES[i][j]);
                }
              }
            }
          }

          for (Zombie zombie : level.ZOMBIES)
          {
            if (zombie.checkCollision(sightBox))
            {
              scaleAndDrawImage(zombie.getImage(), graphics, zombie.getLocation(), zombie.getSize());
            }
          }


          for (Firetrap firetrap : level.FIRETRAPS)
          {
            if (firetrap.checkCollision(sightBox))
            {
              scaleAndDrawImage(firetrap.getImage(), graphics, firetrap.getLocation(), firetrap.getSize());
            }
          }

          scaleAndDrawImage(level.MASTER.getImage(), graphics, level.MASTER.getLocation(), level.MASTER.getSize());

          scaleAndDrawImage(level.PLAYER.getImage(), graphics, level.PLAYER.getLocation(), level.PLAYER.getSize());

          detectShadows(level.PLAYER, sightBox, walls);

          graphics.setColor(Color.BLACK);
          graphics2D.fill(blackMask);

          for (Tile wall : walls)
          {
            scaleAndDrawImage(wall.getImage(), graphics, wall.getLocation(), wall.getSize());
          }
          graphics.setColor(Color.BLACK);
          graphics2D.fill(circleMask);

          scaleAndDrawImage(CIRCULAR_GRADIENT, graphics, new Point((int) sightBox.getX(), (int) sightBox.getY()), new Dimension((int) sightBox.getWidth(), (int) sightBox.getHeight()));
          for (Fire fire : level.FIRES)
          {
            scaleAndDrawImage(fire.getImage(), graphics, fire.getLocation(), fire.getSize());

          }
          scaleAndDrawImage(level.EXIT.getImage(), graphics, level.EXIT.getLocation(), level.EXIT.getSize());
        }
      }
    }
  }

  private void detectShadows(Unit unit, Rectangle2D sightBox, LinkedList<Tile> walls)
  {
    int unitX = unit.getLocation().x + unit.getSize().width / 2;
    int unitY = unit.getLocation().y + unit.getSize().height / 2;
    Point unitP = new Point(unitX, unitY);
    int boxX;
    int boxY;

    LinkedList<Point> destinations = new LinkedList<>();
    Point[] boxCorners = new Point[4];
    //Store the two closest intersections.
    Point intersections;
    Point tempIntersections;
    double distanceSq;

    Polygon polygon = new Polygon();

    for (Tile wall : walls)
    {
      boxX = wall.getLocation().x;
      boxY = wall.getLocation().y;

      //Calculate line from unit to box corner
      boxCorners[0] = new Point(boxX, boxY);
      boxCorners[1] = new Point(boxX + Settings.TILE_SIZE, boxY);
      boxCorners[2] = new Point(boxX, boxY + Settings.TILE_SIZE);
      boxCorners[3] = new Point(boxX + Settings.TILE_SIZE, boxY + Settings.TILE_SIZE);

      destinations.add(getWallPoint(unitX, unitY, boxCorners[0].x, boxCorners[0].y, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[1].x, boxCorners[1].y, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[2].x, boxCorners[2].y, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[3].x, boxCorners[3].y, sightBox));

      destinations.add(getWallPoint(unitX, unitY, boxCorners[0].x + 1, boxCorners[0].y + 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[1].x + 1, boxCorners[1].y + 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[2].x + 1, boxCorners[2].y + 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[3].x + 1, boxCorners[3].y + 1, sightBox));

      destinations.add(getWallPoint(unitX, unitY, boxCorners[0].x - 1, boxCorners[0].y - 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[1].x - 1, boxCorners[1].y - 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[2].x - 1, boxCorners[2].y - 1, sightBox));
      destinations.add(getWallPoint(unitX, unitY, boxCorners[3].x - 1, boxCorners[3].y - 1, sightBox));
    }

    destinations.add(new Point((int) sightBox.getX(), (int) sightBox.getY()));
    destinations.add(new Point((int) sightBox.getMaxX(), (int) sightBox.getY()));
    destinations.add(new Point((int) sightBox.getX(), (int) sightBox.getMaxY()));
    destinations.add(new Point((int) sightBox.getMaxX(), (int) sightBox.getMaxY()));

    destinations.add(new Point((int) sightBox.getX() + 1, (int) sightBox.getY() + 1));
    destinations.add(new Point((int) sightBox.getMaxX() + 1, (int) sightBox.getY() + 1));
    destinations.add(new Point((int) sightBox.getX() + 1, (int) sightBox.getMaxY() + 1));
    destinations.add(new Point((int) sightBox.getMaxX() + 1, (int) sightBox.getMaxY() + 1));

    destinations.add(new Point((int) sightBox.getX() - 1, (int) sightBox.getY() - 1));
    destinations.add(new Point((int) sightBox.getMaxX() - 1, (int) sightBox.getY() - 1));
    destinations.add(new Point((int) sightBox.getX() - 1, (int) sightBox.getMaxY() - 1));
    destinations.add(new Point((int) sightBox.getMaxX() - 1, (int) sightBox.getMaxY() - 1));


    PriorityQueue<Point> intersectPoints = new PriorityQueue<>(5, new ClockwisePointComparator(center));

    for (Point destination : destinations)
    {
      distanceSq = Double.POSITIVE_INFINITY;
      intersections = destination;
      for (Tile subTile : walls)
      {
        tempIntersections = IntersectionFinder.getIntersections(unitP, destination, subTile.getHitbox());
        if (tempIntersections != null)
        {
          if (center.distanceSq(tempIntersections) < distanceSq)
          {
            distanceSq = center.distanceSq(tempIntersections);
            intersections = tempIntersections;
          }
        }
      }
      intersectPoints.add(intersections);
    }

    Point current;
    while (!intersectPoints.isEmpty())

    {
      current = intersectPoints.poll();
      polygon.addPoint(scaleX(current.x), scaleY(current.y));
    }

    blackMask.subtract(new Area(polygon));
  }

  private Point getWallPoint(int unitX, int unitY, int boxX, int boxY, Rectangle2D sightBox)
  {
    double m = (unitY - boxY) / (double) (unitX - boxX);
    double b = boxY - m * boxX;
    Point wallHit = new Point();
    double xPrime = sightBox.getX();
    if (boxX >= unitX)
    {
      xPrime = xPrime + (sightBox.getWidth());
      wallHit.setLocation(xPrime, m * xPrime + b);
    }
    else
    {
      wallHit.setLocation(xPrime, m * xPrime + b);
    }

    //Our x intercept is outside of the sighbox, means it intersects horizontal lines first.
    if (wallHit.y > sightBox.getMaxY() || wallHit.y < sightBox.getY())
    {
      wallHit = IntersectionFinder.getIntersections(new Point(unitX, unitY), wallHit, sightBox);
    }
    return wallHit;
  }

  private void scaleAndDrawImage(BufferedImage image, Graphics graphics, Point corner, Dimension size)
  {
    //Size is increased by 1 pixel to remove an off by one issue in scaling
    graphics.drawImage(image, scaleX(corner.x), scaleY(corner.y), (int) (size.width / windowScale) + 1, (int) (size.height / windowScale) + 1, null);
  }

  private int scaleX(int x)
  {
    return (int) ((x - this.corner.x) / windowScale);
  }

  private int scaleY(int y)
  {
    return (int) ((y - this.corner.y) / windowScale);
  }

  /**
   * Links list of units and tiles for drawing.
   * Called upon level load. These lists are shared with the controller.
   *
   * @param level the level to display
   */
  void setLevel(Level level)
  {
    this.level = level;
  }
}
