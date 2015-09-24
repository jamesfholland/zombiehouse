package View;

import Model.*;
import Model.Tile.*;
import Model.Unit.*;
import Model.Unit.Zombie.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/**
 * This class handles drawing the actual game window. It is always centered on the player.
 */
public class GamePanel extends JPanel
{

  /**
   * List of tiles in map. This might need to change to a different Collection type.
   */
  private Level level;

  /**
   * This will need to shift as the player moves.
   */
  private Rectangle2D viewWindow;
  private Point center;
  private Point corner;

  private Area blackMask;
  private Area circleMask;

  private double windowScale;
  private Player player;
  private Tile[][] tiles;
  private ArrayList<Zombie> zombies;


  /**
   * Setup the new GamePanel
   */
  GamePanel()
  {
    super();
    //Set to red to distinguish from ScoreBar for now.
    setBackground(Color.RED);

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
   * Paint all the units and tiles.
   *
   * @param graphics
   */
  @Override
  public void paintComponent(Graphics graphics)
  {
    super.paintComponent(graphics);

    //Calculate scale factor of the resized window.
    this.getSize();
    windowScale = Settings.WIDTH_STANDARD / (double) this.getWidth();

    if (player != null)
    {

      center.setLocation(player.getLocation());

      center.translate(player.getSize().width / 2, player.getSize().height / 2);
      corner.setLocation(center);
      double dynamicHeight = this.getHeight() * windowScale;
      corner.translate((-1) * Settings.WIDTH_STANDARD / 2, (int) ((-1) * dynamicHeight / 2));

      setViewWindow();

      Rectangle2D sightBox = new Rectangle();
      Point sightCorner = new Point(center);
      sightCorner.translate(Settings.SIGHT_RANGE, Settings.SIGHT_RANGE);
      sightBox.setFrameFromCenter(center, sightCorner);
      blackMask = new Area(
          new Rectangle(
              scaleX((int) viewWindow.getX()),
              scaleY((int) viewWindow.getY()),
              (int) (viewWindow.getWidth() / windowScale),
              (int) (viewWindow.getHeight() / windowScale)));
      circleMask = new Area(
          new Rectangle(
              scaleX((int) viewWindow.getX()),
              scaleY((int) viewWindow.getY()),
              (int) (viewWindow.getWidth() / windowScale),
              (int) (viewWindow.getHeight() / windowScale)));
      circleMask.subtract(new Area(new Ellipse2D.Double(scaleX((int) sightBox.getX()), scaleY((int) sightBox.getY()), sightBox.getWidth() / windowScale, sightBox.getHeight() / windowScale)));

      LinkedList<Tile> walls = new LinkedList<>();
      //TODO: Optimize to only draw tiles in viewport.
      for (int i = 0; i < tiles.length; i++)
      {
        for (int j = 0; j < tiles[i].length; j++)
        {
          if (tiles[i][j].checkCollision(sightBox))
          {
            if (tiles[i][j].isPassable())
            {
              scaleAndDrawImage(tiles[i][j].getImage(), graphics, tiles[i][j].getLocation(), tiles[i][j].getSize());
            } else
            {
              walls.add(tiles[i][j]);
            }
          }
        }
      }

      for (Zombie zombie : zombies)
      {
        if (zombie.checkCollision(sightBox))
        {
          scaleAndDrawImage(zombie.getImage(), graphics, zombie.getLocation(), zombie.getSize());
        }
      }

      scaleAndDrawImage(player.getImage(), graphics, player.getLocation(), player.getSize());

      detectShadows(player, sightBox, walls);

      graphics.setColor(Color.BLACK);
      ((Graphics2D) graphics).fill(blackMask);

      for(Tile wall : walls)
      {
        scaleAndDrawImage(wall.getImage(), graphics, wall.getLocation(), wall.getSize());
      }
      graphics.setColor(Color.BLACK);
      ((Graphics2D) graphics).fill(circleMask);
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

    PriorityQueue<Point> intersectPoints = new PriorityQueue<>(walls.size() * 4, new ClockwisePointComparator(center));

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
    } else
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
    this.player = level.player;
    this.tiles = level.houseTiles;
    this.zombies = level.zombieList;

  }
}
