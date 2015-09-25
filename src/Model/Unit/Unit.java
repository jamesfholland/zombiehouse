package Model.Unit;

import Model.Direction;
import Model.GameObject;
import Model.Settings;
import Model.Unit.Zombie.Zombie;
import Model.Unit.Zombie.ZombieLine;

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

  protected double heading;

  protected double locationXD;
  protected double locationYD;

  protected double nextLocationX;
  protected double nextLocationY;

  protected int tileX;
  protected int tileY;

  protected double bottomRightCornerX;
  protected double bottomRightCornerY;

  protected double speed;
  protected boolean collided = false;

  protected Direction direction = Direction.DOWN;

  protected Point headingVector;

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
   * Finds the units x,y coordinates on the tilesArray
   */
  private void setTileCoordinates()
  {
    this.tileX = (location.x/Settings.TILE_SIZE);
    this.tileY = (location.y/Settings.TILE_SIZE);
  }

  /**
   * Initializes the double variables responsible for keeping track of the
   * x and y coordinates of the unit
   */
  public void setDoubleLocation()
  {
    locationXD = (double) location.x;
    locationYD = (double) location.y;
  }

  public void setBottomRightCorner()
  {
    bottomRightCornerX = location.x + size.width-1;
    bottomRightCornerY = location.y + size.height-1;
  }

  /**
   * Moves the unit based on its speed, heading, and how long it's been since the last update
   * @param speed in tiles per second
   * @param heading degrees from east
   * @param deltaTime
   */
  public void move(double speed, double heading, long deltaTime)
  {
    //if there's no heading, don't move the unit
    if (heading == -1)
    {
      return;
    }

    double headingR = Math.toRadians(heading);

    //find the next location the unit wants to move to
    nextLocationX = (Math.cos(headingR)*speed*deltaTime) + locationXD;
    nextLocationY = (Math.sin(headingR)*speed*deltaTime) + locationYD;

    //set the test hitframe to that location
    nextHitbox.setFrame(nextLocationX, nextLocationY, size.width, size.height);

    //set the headingVector to know which way to check for collisions
    setHeadingVector();

    setTileCoordinates();

    //check for collisions
    checkCollisions();


    if (this instanceof Zombie)
    {
      checkZombieZombieCollision();
    }

    //set the unit at the appropriate place
    location.setLocation(nextLocationX, nextLocationY);
    hitbox.setFrame(nextLocationX, nextLocationY, size.width, size.height);
    locationXD = nextLocationX;
    locationYD = nextLocationY;
  }


  public void checkCollisions()
  {
    //if moving straight east
    if (headingVector.x == 1 && headingVector.y == 0)
    {
      if (checkCollideE() || checkCollideSE())
      {
        nextLocationX = locationXD;
      }
    }

    //if moving south east
    //this check is still buggy, gets units stuck on corners when moving SE
    else if (headingVector.x == 1 && headingVector.y == 1)
    {
      checkSE();
    }

    //moving south
    else if (headingVector.x == 0 && headingVector.y == 1)
    {
      if (checkCollideS() || checkCollideSE())
      {
        nextLocationY = locationYD;
      }
    }

    //moving southwest
    else if (headingVector.x == -1 && headingVector.y == 1)
    {
      if ((checkCollideW() || checkCollideSW()) && !checkCollideS())
      {
        nextLocationX = locationXD;
      }
      else if ((checkCollideS() || checkCollideSE()) && !checkCollideW())
      {
        nextLocationY = locationYD;
      }
      else if ((checkCollideS() && checkCollideW()))
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
      }
    }

    //moving west
    else if (headingVector.x == -1 && headingVector.y == 0)
    {
      if (checkCollideW() || checkCollideSW())
      {
        nextLocationX = locationXD;
      }
    }

    //moving north west
    else if (headingVector.x == -1 && headingVector.y == -1)
    {
      if (checkCollideNW() && (!checkCollideW() && !checkCollideN()))
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
      }
      else if ((checkCollideSW() || checkCollideW()) && !checkCollideN())
      {
        nextLocationX = locationXD;
      }
      else if ((checkCollideN() || checkCollideNE()) && !checkCollideW())
      {
        nextLocationY = locationYD;
      }
      else if ((checkCollideN() && checkCollideW()))
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
      }
    }

    //moving north
    else if (headingVector.x == 0 && headingVector.y == -1)
    {
      if (checkCollideN() || checkCollideNE())
      {
        nextLocationY = locationYD;
      }
    }

    //moving north east
    else if (headingVector.x == 1 && headingVector.y == -1)
    {
      if ((checkCollideE() || checkCollideSE()) && !checkCollideN())
      {
        nextLocationX = locationXD;
      }
      else if ((checkCollideN() || checkCollideNE()) && !checkCollideE())
      {
        nextLocationY = locationYD;
      }
      else if (checkCollideE() && checkCollideN())
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
      }
    }
  }

  private boolean checkCollideE()
  {
    if (!level.houseTiles[tileX+1][tileY].isPassable() && level.houseTiles[tileX+1][tileY].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideSE()
  {
    if (!level.houseTiles[tileX+1][tileY+1].isPassable() && level.houseTiles[tileX+1][tileY+1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideS()
  {
    if (!level.houseTiles[tileX][tileY+1].isPassable() && level.houseTiles[tileX][tileY+1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideSW()
  {
    if (!level.houseTiles[tileX-1][tileY+1].isPassable() && level.houseTiles[tileX-1][tileY+1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideW()
  {
    if (!level.houseTiles[tileX-1][tileY].isPassable() && level.houseTiles[tileX-1][tileY].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideNW()
  {
    if (!level.houseTiles[tileX-1][tileY-1].isPassable() && level.houseTiles[tileX-1][tileY-1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideN()
  {
    if (!level.houseTiles[tileX][tileY-1].isPassable() && level.houseTiles[tileX][tileY-1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideNE()
  {
    if (!level.houseTiles[tileX+1][tileY-1].isPassable() && level.houseTiles[tileX+1][tileY-1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private void checkSE()
  {
    int brTileX;
    int brTileY;

    setBottomRightCorner();

    brTileX = (int) bottomRightCornerX/Settings.TILE_SIZE;
    brTileY = (int) bottomRightCornerY/Settings.TILE_SIZE;


    if (checkCollideSE() && !(checkCollideE()||checkCollideS()))
    {
      if (brTileY == tileY && brTileX == tileX)
      {
        nextLocationY = locationYD;
        nextLocationX = locationXD;
        collided = true;
      }

      if (brTileX == tileX +1)
      {
        nextLocationY = locationYD;
        collided = true;
      }
      if (brTileY == tileY+1)
      {
        nextLocationX = locationXD;
        collided = true;
      }
    }

    if (checkCollideE())
    {
      nextLocationX = locationXD;
    }

    if (checkCollideS())
    {
      nextLocationY = locationYD;
    }
  }

  private void checkZombieZombieCollision()
  {
    for (int i = 0; i < level.zombieList.size(); ++i)
    {
      if (this != level.zombieList.get(i) && this.nextHitbox.intersects(level.zombieList.get(i).hitbox))
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
        collided = true;
      }
    }
  }

  //sets the heading vector to whatever direction the unit is moving
  public void setHeadingVector()
  {
    if (heading == 0.0)
    {
      headingVector.x = 1;
      headingVector.y = 0;
    }

    else if (heading > 0.0 && heading < 90.0)
    {
      headingVector.x = 1;
      headingVector.y = 1;
    }

    else if (heading == 90.0)
    {
      headingVector.x = 0;
      headingVector.y = 1;
    }

    else if (heading > 90.0 && heading < 180.0)
    {
      headingVector.x = -1;
      headingVector.y = 1;
    }

    else if (heading == 180.0)
    {
      headingVector.x = -1;
      headingVector.y = 0;
    }

    else if (heading >180.0 && heading < 270.0)
    {
      headingVector.x = -1;
      headingVector.y = -1;
    }

    else if (heading == 270.0)
    {

      headingVector.x = 0;
      headingVector.y = -1;
    }

    else if (heading >270.0 && heading < 360.0)
    {
      headingVector.x = 1;
      headingVector.y = -1;
    }
  }
}
