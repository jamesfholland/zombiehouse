package Model.Unit;

import Model.Direction;
import Model.GameObject;
import Model.Level;
import Model.Settings;
import Model.Tile.Tile;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Unit extends GameObject
{

  //Shared animation numbers. All units (except fire) share common layout in sprite sheets
  protected static final int WALK_SPRITE_COUNT = 9;
  protected static final int WALK_SPRITE_ROW = 8; //Row 9
  protected static final int SPRITES_ROWS = 21;
  protected static final int SPRITES_PER_ROW = 13;

  protected static final int SPRITE_HORIZONTAL_OFFSET = 16;
  protected static final int SPRITE_VERTICAL_OFFSET = 13;



  protected int spriteState = 0;


  protected Rectangle2D nextHitbox;
  protected int tileX;
  protected int tileY;
  protected double speed;
  protected boolean collided = false;

  protected Direction direction = Direction.DOWN;
  protected Point vectorToMove;

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

  /**
   *
   * @param vectorPoint
   */
  public void checkCollisions(Point vectorPoint)
  {
    //nextHitbox.setFrame(newLocationX, newLocationY, size.getWidth(), size.getHeight());

    vectorToMove.setLocation(vectorPoint);

    if (vectorPoint.x == -1)
    {
      if (checkCollideLeft()) vectorToMove.x =0;
    }
    else if (vectorPoint.x == 1)
    {
      if (checkCollideRight()) vectorToMove.x = 0;
    }
    if (vectorPoint.y == -1)
    {
      if (checkCollideUp()) vectorToMove.y =0;
    }
    else if (vectorPoint.y == 1)
    {
      if (checkCollideDown()) vectorToMove.y = 0;
    }
  }

  public boolean checkCollideDown()
  {
    getTileCoordinates();
    if (!level.houseTiles[tileX][tileY+1].isPassable() && level.houseTiles[tileX][tileY +1].checkCollision(nextHitbox))
    {
      return true;
    }
    if (!level.houseTiles[tileX+1][tileY+1].isPassable() && level.houseTiles[tileX+1][tileY +1].checkCollision(nextHitbox))
    {
      return true;
    }

    return false;
  }

  public boolean checkCollideUp()
  {
    getTileCoordinates();
    if (!level.houseTiles[tileX][tileY-1].isPassable() && level.houseTiles[tileX][tileY - 1].checkCollision(nextHitbox))
    {
      return true;
    }
    if (!level.houseTiles[tileX+1][tileY-1].isPassable() && level.houseTiles[tileX +1][tileY - 1].checkCollision(nextHitbox))
    {
      return true;
    }


    return false;
  }

  public boolean checkCollideLeft()
  {
    getTileCoordinates();
    if (!level.houseTiles[tileX-1][tileY].isPassable() && level.houseTiles[tileX -1][tileY].checkCollision(nextHitbox))
    {
      return true;
    }
    if (!level.houseTiles[tileX - 1][tileY+1].isPassable() && level.houseTiles[tileX-1][tileY +1].checkCollision(nextHitbox))
    {
      return true;
    }

    return false;
  }

  public boolean checkCollideRight()
  {
    getTileCoordinates();

    if (!level.houseTiles[tileX+1][tileY].isPassable() && level.houseTiles[tileX+1][tileY].checkCollision(nextHitbox))
    {
      return true;
    }
    if (!level.houseTiles[tileX + 1][tileY+1].isPassable() && level.houseTiles[tileX+1][tileY + 1].checkCollision(nextHitbox))
    {
      return true;
    }

    return false;
  }
}
