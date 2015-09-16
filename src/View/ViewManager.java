package View;

import Model.Level;

import javax.swing.*;
import java.awt.*;

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
    this.frame.pack();

  }

  /**
   * Links the level.
   * Called upon level load. These lists are shared with the controller.
   * @param level the level that will now be viewed.
   */
  public void setLevel(Level level)
  {
    gamePanel.setLevel(level);
  }

  public void repaint()
  {
    frame.repaint();
  }
}
