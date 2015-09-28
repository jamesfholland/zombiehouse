package Model.Unit.Zombie;

import Model.AStar;
import Model.Settings;
import Model.Unit.Unit;

import java.awt.*;

/**
 * Abstract class Zombie is the parent of all Zombie units (Linewalk, Random and Master)
 * Holds all the common variables and methods used by Zombie units
 */
public abstract class Zombie extends Unit
{
  /**
   * lastDecision holds the millisecond time since a zombie made its last decision
   * knowsPlayerLocation holds if the zombie can 'smell'/ sense the the player
   */
  int lastDecision = 0;
  boolean knowsPlayerLocation = false;

  /**
   * Is the super constructor for all zombies
   * @param x - x coordinate (pixels)
   * @param y - y coordinate (pixels)
   * @param heading - is a double for direction that zombie is facing / walking
   */
  Zombie(int x, int y, double heading)
  {
    this.location = new Point(x, y);
    setDoubleLocation();

    this.heading = heading;
    this.headingVector = new Point(0, 0);
    setHeadingVector();

    this.speed = Settings.TILE_SIZE / 2000.0;
    this.size = Settings.ZOMBIE_SIZE;

    this.nextHitbox = new Rectangle(location, size);
  }

  /**
   * Sets the boolean value of if zombie can sense player - based on euclidian distance
   */
  public void canSmellPlayer()
  {
    if (AStar.eculiDistanceFromPlayer(this.location) < Settings.zombieSmell)
    {
      knowsPlayerLocation = true;
    }
    else
    {
      knowsPlayerLocation = false;
    }
  }

  /**
   * returns if the zombie knows where the player is
   * @return knowsPlayerLocation - boolean
   */
  public boolean knowsPlayerLocation()
  {
    return knowsPlayerLocation;
  }
}
