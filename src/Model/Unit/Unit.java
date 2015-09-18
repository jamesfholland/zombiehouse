package Model.Unit;

import Model.GameObject;
import Model.Level;
import Model.Settings;
import Model.Tile.Tile;

import javax.sound.sampled.Clip;
import java.awt.geom.Rectangle2D;

public abstract class Unit extends GameObject
{
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

  public boolean checkCollideDown()
  {
    getTileCoordinates();
    if (!level.passable(tileX,tileY+1) && level.checkCollided(tileX,tileY+1,nextHitbox))
    {
      return true;
    }
    else if (!level.passable(tileX+1,tileY+1) && level.checkCollided(tileX+1,tileY+1,nextHitbox))
    {
      return true;
    }
    return false;
  }

  public boolean checkCollideUp()
  {
    getTileCoordinates();
    if (!level.passable(tileX,tileY-1) && level.checkCollided(tileX,tileY-1,nextHitbox))
    {
      return true;
    }
    else if (!level.passable(tileX+1,tileY-1) && level.checkCollided(tileX+1,tileY-1,nextHitbox))
    {
      return true;
    }
    return false;
  }

  public boolean checkCollideLeft()
  {
    getTileCoordinates();
    if (!level.passable(tileX-1,tileY) && level.checkCollided(tileX-1,tileY,nextHitbox))
    {
      return true;
    }
    else if (!level.passable(tileX-1,tileY+1) && level.checkCollided(tileX+1,tileY+1,nextHitbox))
    {
      return true;
    }
    return false;
  }

  public boolean checkCollideRight()
  {
    getTileCoordinates();

    if (!level.passable(tileX+1,tileY) && level.checkCollided(tileX+1,tileY,nextHitbox))
    {
      return true;
    }
    else if (!level.passable(tileX+1,tileY+1) && level.checkCollided(tileX+1,tileY+1,nextHitbox))
    {
      return true;
    }
    return false;
  }



}
