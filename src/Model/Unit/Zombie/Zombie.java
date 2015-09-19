package Model.Unit.Zombie;

import Model.Unit.Unit;

import java.awt.*;
import java.util.Random;

public abstract class Zombie extends Unit
{
  protected final static Random RAND = new Random(System.nanoTime());

  protected static double speed = 40.0/1000.0;
  protected static double smell = 7.0;

  protected double heading;
  protected Point direction;
  protected Point testPoint;

  protected boolean collided = false;

  protected double doubleX;
  protected double doubleY;

  public Zombie(int x, int y, double heading)
  {
    this.location = new Point(x,y);
    this.direction = new Point(0,0);
    this.testPoint = new Point(0,0);
    this.heading = heading;
    this.size = new Dimension(50,70);
    this.hitbox = new Rectangle(location, size);
    this.nextHitbox = new Rectangle(location,size);
    this.doubleX = location.x;
    this.doubleY = location.y;
  }

  protected void setHeading(double toSet)
  {
    heading = toSet;
  }

  protected double toRadians()
  {
    return Math.toRadians(heading);
  }

  protected void getDirection()
  {
    if (heading == 0.0)
    {
      direction.x = 1;
      direction.y = 0;
    }

    else if (heading > 0.0 && heading < 90.0)
    {
      direction.x = 1;
      direction.y = -1;
    }

    else if (heading == 90.0)
    {
      direction.x = 0;
      direction.y =-1;
    }

    else if (heading > 90.0 && heading < 180.0)
    {
      direction.x = -1;
      direction.y = -1;
    }

    else if (heading == 180.0)
    {
      direction.x = -1;
      direction.y = 0;
    }

    else if (heading >180.0 && heading < 270.0)
    {
      direction.x = -1;
      direction.y = 1;
    }

    else if (heading == 270.0)
    {
      direction.y = 1;
      direction.x = 0;
    }

    else if (heading >270.0 && heading < 360)
    {
      direction.y = 1;
      direction.x = 1;
    }
  }


}
