package View;

import Model.Level;

import javax.swing.*;
import java.awt.*;

/**
 * This class handles displaying the current level and firetrap count.
 */
class ScoreBar extends JPanel
{
  private Level currentLevel;
  private JLabel levelText = new JLabel();

  ScoreBar()
  {
    super();
    setBackground(Color.BLACK);
    levelText.setForeground(Color.RED);
    this.add(levelText);
  }

  /**
   * Used to keep the info up to date (not actually repainting graphically, but just changing text)
   *
   * @param graphics The Jpanel's graphics object
   */
  @Override
  public void paintComponent(Graphics graphics)
  {
    if (currentLevel == null)
    {
      return;
    }
    if (currentLevel.LEVEL_NUM > 5)
    {
      levelText.setText("YOU WIN! Congrats. As your prize why don't you continue to play the bonus levels.");
      return;
    }
    levelText.setText("Welcome to our ZOMBIE TREEHOUSE!! :)   ||   Level: " + currentLevel.LEVEL_NUM + "   ||   Firetrap count: " + currentLevel.fireTrapCount);
  }

  /**
   * Passes the current Level to ScoreBar, which it uses to keep track of relavent info
   *
   * @param level the current level. This is updated after level changes by the controller calling ViewManager.
   */
  public void setLevel(Level level)
  {
    currentLevel = level;
  }

}
