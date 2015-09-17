package Model.Unit;

import Model.GameObject;
import Model.Level;
import Model.Settings;

import javax.sound.sampled.Clip;
import java.awt.geom.Rectangle2D;

public abstract class Unit extends GameObject
{
  protected Level level;
  protected Rectangle2D nextHitbox;
  protected int tileX;
  protected int tileY;
  protected double speed;

  /**
   * This updates the game object's state as determined by its child class.
   * @param deltaTime
   * @param secondsFromStart
   */
  public abstract void update(long deltaTime, long secondsFromStart);


  /**
   * Returns the current sound associated with the unit;
   * @return Clip containing sound to be played. Null if no sound needs to be played.
   */
  public abstract Clip getSound();

  /**
   * Translate the point according to the provided delta.
   */
  public void moveUnit(int deltaX, int deltaY)
  {
    this.location.translate(deltaX, deltaY);
  }

  public void getTileCoordinates()
  {
    this.tileX = (location.x/Settings.TILE_SIZE);
    this.tileY = (location.y/Settings.TILE_SIZE);
  }

  public void checkCollideDown()
  {
    getTileCoordinates();
  }


}
