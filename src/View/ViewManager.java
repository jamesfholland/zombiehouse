package View;

import Model.Level;


import javax.swing.*;
import java.awt.*;

/**
 * This class manages the view and all gui components
 * It is called by the Controller for level changes and repaints.
 * It initializes all GUI components.
 */
public class ViewManager
{
  private final MainFrame FRAME;
  private final ScoreBar SCORE_BAR;
  private final GamePanel GAME_PANEL;

  public final KeyboardInput KEYBOARD;
  private boolean isPaused;

  /**
   * Sets up the entire GUI and spawns the Settings Dialog.
   */
  public ViewManager()
  {
    this.isPaused = true;
    this.FRAME = new MainFrame();
    this.FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.FRAME.setExtendedState(Frame.MAXIMIZED_BOTH);

    JDialog dialog = new SettingsDialog(this, this.FRAME, "Settings");
    dialog.setVisible(true);

    this.SCORE_BAR = new ScoreBar();
    this.FRAME.add(this.SCORE_BAR, BorderLayout.NORTH);

    this.GAME_PANEL = new GamePanel();
    this.KEYBOARD = new KeyboardInput();
    this.GAME_PANEL.addKeyListener(KEYBOARD);
    this.GAME_PANEL.setFocusable(true);
    this.FRAME.add(this.GAME_PANEL, BorderLayout.CENTER);


  }

  /**
   * Sets the isPaused flag to true for the controller to detect.
   * Called from the Settings Dialog after settings have been made.
   */
  void startGame()
  {
    this.isPaused = false;
    this.FRAME.setVisible(true);
    this.FRAME.pack();
    this.GAME_PANEL.requestFocusInWindow();
  }

  /**
   * Links the level.
   * Called upon level load. These lists are shared with the controller.
   *
   * @param level the level that will now be viewed.
   */
  public void setLevel(Level level)
  {
    GAME_PANEL.setLevel(level);
    SCORE_BAR.setLevel(level);
  }

  public void repaint()
  {
    FRAME.repaint();
  }

  /**
   * Called by the controller to check if the Settings have been set and then start the game.
   * @return true if the game is paused.
   */
  public boolean isPaused()
  {
    return isPaused;
  }
}
