package View;

import Model.Level;

import javax.swing.*;
import java.awt.*;

/**
 * This class handles displaying the stamina bar, zombie death count, and current level.
 */
class ScoreBar extends JPanel
{
  Level currentLevel;
  JLabel levelText = new JLabel();
  JButton menu = new JButton("Menu");

  ScoreBar()
  {
    super();
    setBackground(Color.BLACK);
    levelText.setForeground(Color.RED);

//    levelText.setText("Welcome to Hell!  Level: 1  ||  Firetrap Count: 1");
    this.add(levelText);

  }

  public void setLevel( Level level)
  {
    currentLevel = level;
  }



}
