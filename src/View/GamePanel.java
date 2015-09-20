package View;

import Model.Level;
import Model.Settings;
import Model.Tile.Tile;
import Model.Unit.Player;
import Model.Unit.Unit;
import Model.Unit.Zombie.Zombie;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

      drawShadows(graphics, player);
    }
  }

  private void drawShadows(Graphics graphics, Unit unit)
  {

    int unitX = scaleX(unit.getLocation().x) + unit.getSize().width/2;
    int unitY = scaleY(unit.getLocation().y) + unit.getSize().height/2;
    int boxX;
    int boxY;

    Rectangle2D sightBox = new Rectangle();
    Point sightCorner = new Point(center);
    sightCorner.translate(Settings.SIGHT_RANGE, Settings.SIGHT_RANGE);
    sightBox.setFrameFromCenter(center, sightCorner);


    //ArrayList<Point> corners = new ArrayList<>();
    for (int i = 0; i < tiles.length; i++)
    {
      for(int j = 0; j < tiles[i].length; j++)
      {
        if (tiles[i][j].checkCollision(sightBox) && !tiles[i][j].isPassable())
        {
          boxX = scaleX(tiles[i][j].getLocation().x);
          boxY = scaleY(tiles[i][j].getLocation().y);
          double m = (unitY - boxY) / (double)(unitX - boxX);
          double b = boxY - m * boxX;
          Point wallHit = new Point();
          double xPrime = scaleX((int) sightBox.getX());
          if(boxX >= unitX)
          {
            xPrime = xPrime + sightBox.getWidth();
            wallHit.setLocation(xPrime, m * xPrime + b);

          }
          else
          {
            wallHit.setLocation(xPrime, m * xPrime + b);
          }

          graphics.setColor(Color.RED);
          graphics.drawLine(boxX, boxY, unitX, unitY);
          graphics.setColor(Color.BLUE);
          graphics.drawLine(wallHit.x, wallHit.y, boxX, boxY);

        }
      }
    }
  }

  private void scaleAndDrawImage(BufferedImage image, Graphics graphics, Point corner, Dimension size)
  {
    //Size is increased by 1 pixel to remove an off by one issue in scaling
    graphics.drawImage(image, scaleX(corner.x), scaleY(corner.y), (int) (size.width / windowScale) +1, (int) (size.height / windowScale) +1, null);
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
    this.player = level.getPlayer();
    this.tiles = level.getHouseTiles();
    this.zombies = level.getZombieList();

  }
}
