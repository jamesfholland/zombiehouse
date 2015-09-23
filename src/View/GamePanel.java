package View;

import Model.Level;
import Model.Settings;
import Model.Tile.Tile;
import Model.Unit.Player;
import Model.Unit.Unit;
import Model.Unit.Zombie.Zombie;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

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
      corner.translate((-1) * Settings.WIDTH_STANDARD / 2, (int)((-1) * dynamicHeight / 2));

      setViewWindow();

      Rectangle2D sightBox = new Rectangle();
      Point sightCorner = new Point(center);
      sightCorner.translate(Settings.SIGHT_RANGE, Settings.SIGHT_RANGE);
      sightBox.setFrameFromCenter(center, sightCorner);


      //TODO: Optimize to only draw tiles in viewport.
      if (tiles != null)
      {
        for (int i = 0; i < tiles.length; i++)
        {
          for(int j = 0; j < tiles[i].length; j++)
          {
            if (tiles[i][j].checkCollision(viewWindow))
            {
              scaleAndDrawImage(tiles[i][j].getImage(), graphics, tiles[i][j].getLocation(), tiles[i][j].getSize());
            }
          }
        }
      }

      for(Zombie zombie : zombies)
      {
        if(zombie.checkCollision(viewWindow))
        {
          scaleAndDrawImage(zombie.getImage(), graphics, zombie.getLocation(), zombie.getSize());
        }
      }

      scaleAndDrawImage(player.getImage(), graphics, player.getLocation(), player.getSize());

      //drawShadows(graphics, player, sightBox);
    }
  }

  private void drawShadows(Graphics graphics, Unit unit, Rectangle2D sightBox)
  {

    int unitX = unit.getLocation().x + unit.getSize().width/2;
    int unitY = unit.getLocation().y + unit.getSize().height/2;
    Point unitP = new Point(unitX, unitY);
    int boxX;
    int boxY;

    Point[] destinations = new Point[4];
    Point[] boxCorners = new Point[4];
    //Store the two closest intersections.
    Point[][] intersections = new Point[4][2];
    Point tempIntersection;
    double distanceSq = Double.POSITIVE_INFINITY;

    Polygon polygon = new Polygon();

    LinkedList<Tile> visibleTiles = new LinkedList<>();

    for (int i = 0; i < tiles.length; i++)
    {
      for(int j = 0; j < tiles[i].length; j++)
      {
        if (tiles[i][j].checkCollision(sightBox) && !tiles[i][j].isPassable())
        {
          visibleTiles.add(tiles[i][j]);
        }
      }
    }

    PriorityQueue<Point> intersectPoints = new PriorityQueue<>(visibleTiles.size() * 4, new ClockwisePointComparator(center));

    graphics.setColor(Color.RED);
    for(Iterator<Tile> iterator= visibleTiles.iterator(); iterator.hasNext();)
    {
      Tile tile = iterator.next();
      boxX = tile.getLocation().x;
      boxY = tile.getLocation().y;


      //Calculate line from unit to box corner
      boxCorners[0] = new Point(boxX, boxY);
      boxCorners[1] = new Point(boxX + Settings.TILE_SIZE, boxY);
      boxCorners[2] = new Point(boxX, boxY + Settings.TILE_SIZE);
      boxCorners[3] = new Point(boxX + Settings.TILE_SIZE, boxY + Settings.TILE_SIZE);

      destinations[0] = getWallPoint(unitX, unitY, boxCorners[0].x, boxCorners[0].y, sightBox);
      destinations[1] = getWallPoint(unitX, unitY, boxCorners[1].x, boxCorners[1].y, sightBox);
      destinations[2] = getWallPoint(unitX, unitY, boxCorners[2].x, boxCorners[2].y, sightBox);
      destinations[3] = getWallPoint(unitX, unitY, boxCorners[3].x, boxCorners[3].y, sightBox);
      for(Tile subTile: visibleTiles)
      {
        for(int i = 0; i < destinations.length; i++)
        {
          tempIntersection = IntersectionFinder.getNearIntersection(unitP, destinations[i], subTile.getHitbox());
          if(tempIntersection != null)
          {
            if(center.distanceSq(tempIntersection) < distanceSq)
            {
                distanceSq = center.distanceSq(tempIntersection);
                intersections[i][1] = intersections[i][0]; //Move last closest to next position.
                intersections[i][0] = tempIntersection;

            }
          }
        }
      }

      for(int i = 0; i < destinations.length; i++)
      {
        intersectPoints.add(intersections[i][0]);
        //Check if intersection is also the wall corner that our destination was based on.
        //This means we are on a corner and need the wall hit assuming it doesn't intersect the far side of the tile.
        if(intersections[i][0].equals(boxCorners[i]))
        {
            if(intersections[i][1] == null)
            {
              intersectPoints.add(destinations[i]); //Add our wall point.
            }
          else
            {
              intersectPoints.add(intersections[i][1]); //Add the next closest intersection.
            }
        }
      }

    }

    Point current = null;
    Point previous = null;
    Point first = null;
  while (! intersectPoints.isEmpty())
  {
    //Setup initial loop (If we ever have only 1 point at start, there are bigger problems than our empty queue.
    if(current == null)
    {
      first = intersectPoints.poll();
      previous = first;
    }
    current = intersectPoints.poll();
    polygon.reset();
    polygon.addPoint(scaleX(current.x), scaleY(current.y));
    polygon.addPoint(scaleX(previous.x), scaleY(previous.y));
    polygon.addPoint(scaleX(center.x), scaleY(center.y));
    graphics.drawPolygon(polygon);
    }
  }

  private Point getWallPoint(int unitX, int unitY, int boxX, int boxY, Rectangle2D sightBox)
  {
    double m = (unitY - boxY) / (double)(unitX - boxX);
    double b = boxY - m * boxX;
    Point wallHit = new Point();
    double xPrime = sightBox.getX();
    if(boxX >= unitX)
    {
      xPrime = xPrime + (sightBox.getWidth());
      wallHit.setLocation(xPrime, m * xPrime + b);
    }
    else
    {
      wallHit.setLocation(xPrime, m * xPrime + b);
    }

    //Our x intercept is outside of the sighbox, means it intersects horizontal lines first.
    if(wallHit.y > sightBox.getMaxY() || wallHit.y < sightBox.getY())
    {
      wallHit = IntersectionFinder.getNearIntersection(new Point(unitX, unitY), wallHit, sightBox);
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
    return (int)((x - this.corner.x)/windowScale);
  }

  private int scaleY(int y)
  {
    return (int)((y - this.corner.y)/windowScale);
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
