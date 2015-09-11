package Model.Unit.Zombie;

import Model.Unit.Unit;
import java.util.Random;
import java.awt.Point;

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
  }


}
