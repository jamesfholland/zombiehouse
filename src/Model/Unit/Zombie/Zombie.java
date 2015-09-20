package Model.Unit.Zombie;

import Model.Settings;
import Model.Unit.Unit;

import java.awt.*;
import java.util.Random;

public abstract class Zombie extends Unit
{
  protected final static Random RAND = new Random(System.nanoTime());

  protected static double smell = 7.0;

  protected double heading;
  protected Point vector;
  protected Point testPoint;

  protected boolean collided = false;

  protected double doubleX;
  protected double doubleY;

  public Zombie(int x, int y, double heading)
  {
    this.location = new Point(x,y);
    this.testPoint = new Point(0,0);
    this.heading = heading;
    this.speed = Settings.TILE_SIZE/2000.0;
    this.size = Settings.ZOMBIE_SIZE;
    this.hitbox = new Rectangle(location, size);
    this.nextHitbox = new Rectangle(location,size);
    this.doubleX = location.x;
    this.doubleY = location.y;
    this.vector = new Point(0,0);
    this.vectorToMove = new Point(0,0);
    setVector();
  }

  protected void setHeading(double toSet)
  {
    heading = toSet;
  }

  protected double toRadians()
  {
    return Math.toRadians(heading);
  }

  protected void setVector()
  {
    if (heading == 0.0)
    {
      vector.x = 1;
      vector.y = 0;
    }

    else if (heading > 0.0 && heading < 90.0)
    {
      vector.x = 1;
      vector.y = 1;
    }

    else if (heading == 90.0)
    {
      vector.x = 0;
      vector.y = 1;
    }

    else if (heading > 90.0 && heading < 180.0)
    {
      vector.x = -1;
      vector.y = 1;
    }

    else if (heading == 180.0)
    {
      vector.x = -1;
      vector.y = 0;
    }

    else if (heading >180.0 && heading < 270.0)
    {
      vector.x = -1;
      vector.y = -1;
    }

    else if (heading == 270.0)
    {

      vector.x = 0;
      vector.y = -1;
    }

    else if (heading >270.0 && heading < 360.0)
    {
      vector.x = 1;
      vector.y = -1;
    }
  }


}
