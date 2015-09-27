package View;

import Model.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SettingsPanel allows the user to change default settings in the application.
 */
public class SettingsPanel extends JPanel
{
  SettingsConverter converter = new SettingsConverter();
  JLabel textSightRange = new JLabel("Player sight range (tiles)");
  JTextField jtfSightRange = new JTextField(""+ converter.pixelsToTiles(Settings.sightRange),5); //convert to tiles
  JLabel textPlayerHearing = new JLabel("Player hearing (tiles)");
  JTextField jtfPlayerHearing = new JTextField("" + converter.pixelsToTiles(Settings.playerHearing),5); //convert to tiles
  JLabel textWalkSpeed = new JLabel("Player walk speed (tiles/sec)");
  JTextField jtfWalkSpeed = new JTextField("" + converter.pixelsToTileSpeed(Settings.walkSpeed), 5); //convert to tiles/sec
  JLabel textPlayerStamina = new JLabel("Player stamina (sec)");
  JTextField jtfPlayerStamina = new JTextField("" + converter.millisecondsToSeconds(Settings.playerStamina),5); //conversions to seconds
  JLabel textPlayerRegen = new JLabel("Player stamina regen (stamina/sec)");
  JTextField jtfPlayerRegen = new JTextField("" + Settings.playerRegen, 5); //no conversion
  JLabel textZombieSpeed = new JLabel("Zombie speed (tiles/sec)");
  JTextField jtfZombieSpeed = new JTextField("" + converter.pixelsToTileSpeed(Settings.zombieSpeed), 5); //convert to tiles/sec
  JLabel textZombieDecisionRate = new JLabel("Seconds until decision");
  JTextField jtfZombieDecisionRate = new JTextField("" + converter.millisecondsToSeconds(Settings.zombieDecisionRate), 5); //convert to seconds
  JLabel textZombieSmell = new JLabel("Zombie smell distance (tiles)");
  JTextField jtfZombieSmell = new JTextField("" + converter.pixelsToTiles(Settings.zombieSmell), 5); //convert to tiles
  JLabel textFiretrapSpawnRate = new JLabel("Fire trap spawn rate (%/tile)");
  JTextField jtfFiretrapSpawnRate = new JTextField("" + converter.decimalToPercent(Settings.firetrapSpawnRate), 5); //convert to %
  JLabel textZombieSpawnRate = new JLabel("Zombe spawn rate (%/tile)");
  JTextField jtfZombieSpawnRate = new JTextField("" + converter.decimalToPercent(Settings.zombieSpawnRate), 5); // convert to %

  SettingsPanel()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    JButton enter = new JButton("Save Settings");
    enter.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent actionEvent)
      {
        convertAndSave();
      }
    });

    this.add(textSightRange);
    this.add(jtfSightRange);
    this.add(textPlayerHearing);
    this.add(jtfPlayerHearing);
    this.add(textWalkSpeed);
    this.add(jtfWalkSpeed);
    this.add(textPlayerStamina);
    this.add(jtfPlayerStamina);
    this.add(textPlayerRegen);
    this.add(jtfPlayerRegen);
    this.add(textZombieSpeed);
    this.add(jtfZombieSpeed);
    this.add(textZombieDecisionRate);
    this.add(jtfZombieDecisionRate);
    this.add(textZombieSmell);
    this.add(jtfZombieSmell);
    this.add(textFiretrapSpawnRate);
    this.add(jtfFiretrapSpawnRate);
    this.add(textZombieSpawnRate);
    this.add(jtfZombieSpawnRate);
    this.add(enter);
  }

  private void convertAndSave()
  {
    String toParse = jtfSightRange.getText();
    Settings.sightRange = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse  = jtfPlayerHearing.getText();
    Settings.playerHearing = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse = jtfWalkSpeed.getText();
    Settings.walkSpeed = converter.tilesToPixelSpeed(Double.parseDouble(toParse));

    toParse = jtfPlayerStamina.getText();
    Settings.playerStamina = converter.secondsToMilliseconds(Double.parseDouble(toParse));

    toParse = jtfZombieSpeed.getText();
    Settings.zombieSpeed = converter.tilesToPixelSpeed(Double.parseDouble(toParse));

    toParse = jtfZombieDecisionRate.getText();
    Settings.zombieDecisionRate = converter.secondsToMilliseconds(Double.parseDouble(toParse));

    toParse  = jtfZombieSmell.getText();
    Settings.zombieSmell = converter.tilesToPixels(Double.parseDouble(toParse));

    toParse = jtfFiretrapSpawnRate.getText();
    Settings.firetrapSpawnRate = converter.percentToDecimal(Double.parseDouble(toParse));

    toParse = jtfZombieSpawnRate.getText();
    Settings.zombieSpawnRate = converter.percentToDecimal(Double.parseDouble(toParse));
  }

  private class SettingsConverter
  {
    public double tilesToPixels(double tiles)
    {
      return tiles*Settings.TILE_SIZE;
    }
    public double pixelsToTiles(double pixels)
    {
      return pixels/Settings.TILE_SIZE;
    }
    public double pixelsToTileSpeed(double pixels)
    {
      return (pixels*1000)/Settings.TILE_SIZE;
    }

    public double tilesToPixelSpeed(double tiles)
    {
      return (tiles * Settings.TILE_SIZE / 1000);
    }

    public double millisecondsToSeconds(double milliseconds)
    {
      return (milliseconds/1000);
    }

    public double secondsToMilliseconds(double seconds)
    {
      return (seconds*1000);
    }

    public double decimalToPercent(double decimal)
    {
      return decimal * 100;
    }

    public double percentToDecimal(double percent)
    {
      return percent/100;
    }
  }

}
