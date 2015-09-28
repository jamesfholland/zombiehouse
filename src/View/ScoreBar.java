package View;

import Model.Level;

import javax.swing.*;
import java.awt.*;

/**
 * This class handles displaying the current level and firetrap count.
 */
class ScoreBar extends JPanel
{
  Level currentLevel;
  JLabel levelText = new JLabel();

  ScoreBar()
  {
    super();
    setBackground(Color.BLACK);
    levelText.setForeground(Color.RED);
    this.add(levelText);
  }

  @Override
  public void paintComponent(Graphics graphics)
  {
    if(currentLevel == null) { return; }
    levelText.setText("Welcome to our ZOMBIE TREEHOUSE!! :)   ||   Level: " + currentLevel.LEVEL_NUM + "   ||   Firetrap count: " + currentLevel.fireTrapCount);
  }


  public void setLevel( Level level)
  {
    currentLevel = level;
  }

}
