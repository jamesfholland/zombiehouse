package View;

import Model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SettingsDialog class is the class responsible for the pop up when zombiehouse opens. It allows the user to
 * set the default settings before the game begins.
 * <p>
 * From a program prospective, only the View touches the the SettingsDialog class.
 * The SettingsDialog has ability to write over the default settings shown Settings after it reads in the user's input
 */
class SettingsDialog extends JDialog
{
  private SettingsConverter converter = new SettingsConverter();

  private JTextField jtfSightRange = new JTextField("" + converter.pixelsToTiles(Settings.sightRange), 5); //convert to tiles

  private JTextField jtfPlayerHearing = new JTextField("" + converter.pixelsToTiles(Settings.playerHearing), 5); //convert to tiles

  private JTextField jtfWalkSpeed = new JTextField("" + converter.pixelsToTileSpeed(Settings.walkSpeed), 5); //convert to tiles/sec

  private JTextField jtfPlayerStamina = new JTextField("" + converter.millisecondsToSeconds(Settings.playerStamina), 5); //conversions to seconds

  private JTextField jtfPlayerRegen = new JTextField("" + Settings.playerRegen);

  private JTextField jtfZombieSpeed = new JTextField("" + converter.pixelsToTileSpeed(Settings.zombieSpeed), 5); //convert to tiles/sec

  private JTextField jtfZombieDecisionRate = new JTextField("" + converter.millisecondsToSeconds(Settings.zombieDecisionRate), 5); //convert to seconds

  private JTextField jtfZombieSmell = new JTextField("" + converter.pixelsToTiles(Settings.zombieSmell), 5); //convert to tiles

  private JTextField jtfFiretrapSpawnRate = new JTextField("" + converter.decimalToPercent(Settings.firetrapSpawnRate), 5); //convert to %

  private JTextField jtfZombieSpawnRate = new JTextField("" + converter.decimalToPercent(Settings.zombieSpawnRate), 5); // convert to %

  /**
   * Creates our Settings dialog for the player to pick settings.
   *
   * @param viewManager This is used to signal a gamestart once the player sets their settings.
   * @param frame       the parents Frame of the dialog
   * @param title       title screen for dialog.
   */
  SettingsDialog(ViewManager viewManager, JFrame frame, String title)
  {
    super(frame, title);
    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    setSize(new Dimension(200, 500));
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    JButton enter = new JButton("Start Game");
    enter.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent actionEvent)
      {
        convertAndSave(); //convert the user inputed settings to values the game reads
        viewManager.startGame(); //start the game
        dispose(); //close the jdialogue
      }
    });

    JLabel textSightRange = new JLabel("Player sight range (tiles)");
    this.add(textSightRange);
    this.add(jtfSightRange);
    JLabel textPlayerHearing = new JLabel("Player hearing (tiles)");
    this.add(textPlayerHearing);
    this.add(jtfPlayerHearing);
    JLabel textWalkSpeed = new JLabel("Player walk speed (tiles/sec)");
    this.add(textWalkSpeed);
    this.add(jtfWalkSpeed);
    JLabel textPlayerStamina = new JLabel("Player stamina (sec)");
    this.add(textPlayerStamina);
    this.add(jtfPlayerStamina);
    JLabel textPlayerRegen = new JLabel("Player stamina regen (stamina/sec)");
    this.add(textPlayerRegen);
    this.add(jtfPlayerRegen);
    JLabel textZombieSpeed = new JLabel("Zombie speed (tiles/sec)");
    this.add(textZombieSpeed);
    this.add(jtfZombieSpeed);
    JLabel textZombieDecisionRate = new JLabel("Seconds until decision");
    this.add(textZombieDecisionRate);
    this.add(jtfZombieDecisionRate);
    JLabel textZombieSmell = new JLabel("Zombie smell distance (tiles)");
    this.add(textZombieSmell);
    this.add(jtfZombieSmell);
    JLabel textFiretrapSpawnRate = new JLabel("Fire trap spawn rate (%/tile)");
    this.add(textFiretrapSpawnRate);
    this.add(jtfFiretrapSpawnRate);
    JLabel textZombieSpawnRate = new JLabel("Zombie spawn rate (%/tile)");
    this.add(textZombieSpawnRate);
    this.add(jtfZombieSpawnRate);
    this.add(enter);
  }

  private void convertAndSave()
  {
    String toParse = jtfSightRange.getText();
    Settings.sightRange = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse = jtfPlayerHearing.getText();
    Settings.playerHearing = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse = jtfWalkSpeed.getText();
    Settings.walkSpeed = converter.tilesToPixelSpeed(Double.parseDouble(toParse));
    Settings.runSpeed = 2.0 * Settings.walkSpeed;

    toParse = jtfPlayerStamina.getText();
    Settings.playerStamina = converter.secondsToMilliseconds(Double.parseDouble(toParse));

    toParse = jtfPlayerRegen.getText();
    Settings.playerRegen = Double.parseDouble(toParse);

    toParse = jtfZombieSpeed.getText();
    Settings.zombieSpeed = converter.tilesToPixelSpeed(Double.parseDouble(toParse));

    toParse = jtfZombieDecisionRate.getText();
    Settings.zombieDecisionRate = converter.secondsToMilliseconds(Double.parseDouble(toParse));

    toParse = jtfZombieSmell.getText();
    Settings.zombieSmell = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse = jtfFiretrapSpawnRate.getText();
    Settings.firetrapSpawnRate = converter.percentToDecimal(Double.parseDouble(toParse));

    toParse = jtfZombieSpawnRate.getText();
    Settings.zombieSpawnRate = converter.percentToDecimal(Double.parseDouble(toParse));
  }

  private class SettingsConverter
  {
    private double tilesToPixels(double tiles)
    {
      return tiles * Settings.TILE_SIZE;
    }

    private double pixelsToTiles(double pixels)
    {
      return pixels / Settings.TILE_SIZE;
    }

    private double pixelsToTileSpeed(double pixels)
    {
      return (pixels * 1000) / Settings.TILE_SIZE;
    }

    private double tilesToPixelSpeed(double tiles)
    {
      return (tiles * Settings.TILE_SIZE / 1000);
    }

    private double millisecondsToSeconds(double milliseconds)
    {
      return (milliseconds / 1000);
    }

    private double secondsToMilliseconds(double seconds)
    {
      return (seconds * 1000);
    }

    private double decimalToPercent(double decimal)
    {
      return decimal * 100;
    }

    private double percentToDecimal(double percent)
    {
      return percent / 100;
    }
  }

}
