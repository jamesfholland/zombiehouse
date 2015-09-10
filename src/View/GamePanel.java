package View;

import Model.Settings;
import Model.Tile.Tile;
import Model.Unit.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class handles drawing the actual game window. It is always centered on the player.
 */
public class GamePanel extends JPanel
{

  /**
   * List of tiles in map. This might need to change to a different Collection type.
   */
  private ArrayList<Tile> tiles;

  /**
   * List of units in map. Also might need to change.
   */
  private ArrayList<Unit> units;

  private Unit player;

  /**
   * This will need to shift as the player moves.
   */
  private Rectangle2D viewWindow;
  private Point center;
  private double viewTileSize;

  /**
   * Setup the new GamePanel
   */
  GamePanel()
  {
    super();
    //Set to red to distinguish from ScoreBar for now.
    setBackground(Color.RED);

    viewWindow = new Rectangle(0,0, Settings.WIDTH_STANDARD, Settings.HEIGHT_STANDARD);
    center = new Point();
  }

  /**
   * Links list of units and tiles for drawing.
   * Called upon level load. These lists are shared with the controller.
   * @param tiles list of tiles in map
   * @param units list of units both zombies, player, fire, and traps.
   */
  void setUnitsAndTiles(ArrayList<Tile> tiles, ArrayList<Unit> units, Unit player)
  {
    this.tiles = tiles;
    this.units = units;
    this.player = player;
  }

  private void setViewWindow(Point center)
  {
    //This isn't correct it needs to account for increased length as window narrows. All sizes are fixed by width.
    viewWindow.setFrameFromCenter(center.x, center.y, center.x+Settings.WIDTH_STANDARD/2, center.y + Settings.HEIGHT_STANDARD/2);
  }

  /**
   * Paint all the units and tiles.
   * @param graphics
   */
  @Override
  public void paintComponent(Graphics graphics)
  {

    //Calculate tilesize based on resizable width of jPanel
    viewTileSize = this.getWidth()/ Settings.TILED_WIDTH;

    if(player != null)
    {
      center.setLocation(player.getLocation());
      //This could probably be better written.
      center.translate(player.getSize().width, player.getSize().height);

      setViewWindow(center);

      if (tiles != null)
      {
        for (Tile tile : tiles)
        {
          if(tile.checkCollision(viewWindow))
          {
            //Draw tile at calculated pixel location
          }
        }
      }

    }
  }
}
