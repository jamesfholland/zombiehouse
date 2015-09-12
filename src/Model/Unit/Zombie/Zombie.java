package Model.Unit.Zombie;

import Model.Unit.Unit;

import java.awt.*;
import java.util.Random;

public abstract class Zombie extends Unit
{
  protected final static Random RAND = new Random(System.nanoTime());

  protected static double speed = 0.5;
  protected static double smell = 7.0;

  protected static double heading;

  protected boolean collided = false;

  public Zombie(int x, int y, double heading)
  {
    this.location = new Point(x,y);
    this.heading = heading;
    this.size = new Dimension(50,70);
  }

  protected void setHeading(double toSet)
  {
    heading = toSet;
  }

  protected double getHeading()
  {
    return heading;
  }


}
