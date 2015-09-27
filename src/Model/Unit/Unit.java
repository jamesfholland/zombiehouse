package Model.Unit;

import Model.Direction;
import Model.GameObject;
import Model.Settings;
import Model.Unit.Zombie.Zombie;

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

  private double locationXD;
  private double locationYD;

  private double nextLocationX;
  private double nextLocationY;

  int tileX;
  int tileY;

  private double bottomRightCornerX;
  private double bottomRightCornerY;

  protected double speed;
  protected boolean collided = false;

  protected Direction direction = Direction.SOUTH;

  protected Point headingVector;

  /**
   * This updates the game object's state as determined by its child class.
   *  @param deltaTime
   *
   */
  public abstract void update(long deltaTime);

  /**
   * Finds the units x,y coordinates on the tilesArray
   */
  void setTileCoordinates() throws ArrayIndexOutOfBoundsException
  {
    this.tileX = (location.x / Settings.TILE_SIZE);
    this.tileY = (location.y / Settings.TILE_SIZE);


    if (level.TILES[this.tileX][this.tileY] == null)
    {
      throw new ArrayIndexOutOfBoundsException();
    }

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

  private void setBottomRightCorner()
  {
    bottomRightCornerX = location.x + size.width - 1;
    bottomRightCornerY = location.y + size.height - 1;
  }

  /**
   * Moves the unit based on its speed, heading, and how long it's been since the last update
   *
   * @param speed     in tiles per second
   * @param heading   degrees from east
   * @param deltaTime
   */
  protected void move(double speed, double heading, long deltaTime)
  {
    //if there's no heading, don't move the unit
    if (heading == -1)
    {
      return;
    }

    double headingR = Math.toRadians(heading);

    //find the next location the unit wants to move to
    nextLocationX = (Math.cos(headingR) * speed * deltaTime) + locationXD;
    nextLocationY = (Math.sin(headingR) * speed * deltaTime) + locationYD;

    //set the test hitframe to that location
    nextHitbox.setFrame(nextLocationX, nextLocationY, size.width, size.height);

    //set the headingVector to know which way to check for collisions
    setHeadingVector();

    //making sure that zombies or players aren't trying to move outside of the tile array
    try
    {
      setTileCoordinates();
    }
    catch (ArrayIndexOutOfBoundsException error)
    {
      if (this instanceof Zombie)
      {
        //if zombies are moving outside of the array delete them
        level.ZOMBIES.remove(this);
      } else
      {
        return;
      }
    }

    //check for collisions
    checkCollisions();


    if (this instanceof Zombie)
    {
      checkZombieZombieCollision();
    }

    //set the unit at the appropriate place
    location.setLocation(nextLocationX, nextLocationY);
    //hitbox.setFrame(nextLocationX, nextLocationY, size.width, size.height);
    locationXD = nextLocationX;
    locationYD = nextLocationY;
  }


  private void checkCollisions()
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
      } else if ((checkCollideS() || checkCollideSE()) && !checkCollideW())
      {
        nextLocationY = locationYD;
      } else if ((checkCollideS() && checkCollideW()))
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
      } else if ((checkCollideSW() || checkCollideW()) && !checkCollideN())
      {
        nextLocationX = locationXD;
      } else if ((checkCollideN() || checkCollideNE()) && !checkCollideW())
      {
        nextLocationY = locationYD;
      } else if ((checkCollideN() && checkCollideW()))
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
      } else if ((checkCollideN() || checkCollideNE()) && !checkCollideE())
      {
        nextLocationY = locationYD;
      } else if (checkCollideE() && checkCollideN())
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
      }
    }
  }

  private boolean checkCollideE()
  {
    if (!level.TILES[tileX + 1][tileY].isPassable() && level.TILES[tileX + 1][tileY].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideSE()
  {
    if (!level.TILES[tileX + 1][tileY + 1].isPassable() && level.TILES[tileX + 1][tileY + 1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideS()
  {
    if (!level.TILES[tileX][tileY + 1].isPassable() && level.TILES[tileX][tileY + 1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideSW()
  {
    if (!level.TILES[tileX - 1][tileY + 1].isPassable() && level.TILES[tileX - 1][tileY + 1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideW()
  {
    if (!level.TILES[tileX - 1][tileY].isPassable() && level.TILES[tileX - 1][tileY].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideNW()
  {
    if (!level.TILES[tileX - 1][tileY - 1].isPassable() && level.TILES[tileX - 1][tileY - 1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideN()
  {
    if (!level.TILES[tileX][tileY - 1].isPassable() && level.TILES[tileX][tileY - 1].checkCollision(this.nextHitbox))
    {
      collided = true;
      return true;
    }
    return false;
  }

  private boolean checkCollideNE()
  {
    if (!level.TILES[tileX + 1][tileY - 1].isPassable() && level.TILES[tileX + 1][tileY - 1].checkCollision(this.nextHitbox))
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

    brTileX = (int) bottomRightCornerX / Settings.TILE_SIZE;
    brTileY = (int) bottomRightCornerY / Settings.TILE_SIZE;


    if (checkCollideSE() && !(checkCollideE() || checkCollideS()))
    {
      if (brTileY == tileY && brTileX == tileX)
      {
        nextLocationY = locationYD;
        nextLocationX = locationXD;
        collided = true;
      }

      if (brTileX == tileX + 1)
      {
        nextLocationY = locationYD;
        collided = true;
      }
      if (brTileY == tileY + 1)
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
    for (int i = 0; i < level.ZOMBIES.size(); ++i)
    {
      if (this != level.ZOMBIES.get(i) && this.nextHitbox.intersects(level.ZOMBIES.get(i).getHitbox()))
      {
        nextLocationX = locationXD;
        nextLocationY = locationYD;
        collided = true;
      }
    }
  }

  //sets the heading vector to whatever direction the unit is moving
  protected void setHeadingVector()
  {
    if (heading == 0.0)
    {
      headingVector.x = 1;
      headingVector.y = 0;
    } else if (heading > 0.0 && heading < 90.0)
    {
      headingVector.x = 1;
      headingVector.y = 1;
    } else if (heading == 90.0)
    {
      headingVector.x = 0;
      headingVector.y = 1;
    } else if (heading > 90.0 && heading < 180.0)
    {
      headingVector.x = -1;
      headingVector.y = 1;
    } else if (heading == 180.0)
    {
      headingVector.x = -1;
      headingVector.y = 0;
    } else if (heading > 180.0 && heading < 270.0)
    {
      headingVector.x = -1;
      headingVector.y = -1;
    } else if (heading == 270.0)
    {

      headingVector.x = 0;
      headingVector.y = -1;
    } else if (heading > 270.0 && heading < 360.0)
    {
      headingVector.x = 1;
      headingVector.y = -1;
    }
  }
}
