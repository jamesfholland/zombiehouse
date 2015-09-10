package View;

import Model.Tile.Tile;
import Model.Unit.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class manages the view and all gui components
 */
public class ViewManager
{
  private MainFrame frame;
  private ScoreBar scoreBar;
  private GamePanel gamePanel;

  public ViewManager()
  {
    this.frame = new MainFrame();
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);

    this.scoreBar = new ScoreBar();
    this.frame.add(this.scoreBar, BorderLayout.NORTH);

    this.gamePanel = new GamePanel();
    this.frame.add(this.gamePanel, BorderLayout.CENTER);

    this.frame.setVisible(true);

  }

  /**
   * Links list of units and tiles for drawing.
   * Called upon level load. These lists are shared with the controller.
   * @param tiles list of tiles in map
   * @param units list of units both zombies, player, fire, and traps.
   */
  public void setUnitsAndTiles(ArrayList<Tile> tiles, ArrayList<Unit> units, Unit player)
  {
    this.gamePanel.setUnitsAndTiles(tiles, units, player);
  }

}
