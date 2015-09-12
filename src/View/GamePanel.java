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
    double dynamicHeight = Settings.HEIGHT_STANDARD * windowScale;
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
    windowScale = Settings.WIDTH_STANDARD / (double) this.getWidth();

    if (player != null)
    {
      center.setLocation(player.getLocation());
      //This could probably be better written.
      center.translate(player.getSize().width, player.getSize().height);
      corner.setLocation(center);
      corner.translate((-1) * this.getWidth() / 2, (-1) * this.getHeight() / 2);

      setViewWindow();



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

    }
  }

  private void scaleAndDrawImage(BufferedImage image, Graphics graphics, Point corner, Dimension size)
  {
    graphics.drawImage(image, corner.x - this.corner.x, corner.y - this.corner.y, (int) (size.width / windowScale), (int) (size.height / windowScale), null);

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
