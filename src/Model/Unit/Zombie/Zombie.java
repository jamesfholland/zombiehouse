package Model.Unit.Zombie;

import Model.Settings;
import Model.Unit.Unit;

import java.awt.*;
import java.util.Random;

public abstract class Zombie extends Unit
{
  protected final static Random RAND = new Random(System.nanoTime());
  protected int lastDecision = 0;

  protected boolean knowsPlayerLocation = false;


  public Zombie(int x, int y, double heading)
  {
    this.location = new Point(x,y);
    setDoubleLocation();

    this.heading = heading;
    this.headingVector = new Point(0,0);
    setHeadingVector();

    this.speed = Settings.TILE_SIZE/2000.0;
    this.size = Settings.ZOMBIE_SIZE;

    this.hitbox = new Rectangle(location, size);
    this.nextHitbox = new Rectangle(location,size);
  }

  public void canSmellPlayer()
  {
    if (level.aStar.eculiDistanceFromPlayer(this.location) < Settings.ZOMBIE_SMELL)
    {
      knowsPlayerLocation = true;
    }
    else
    {
      knowsPlayerLocation = false;
    }
  }
}
