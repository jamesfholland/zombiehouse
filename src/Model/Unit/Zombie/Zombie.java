package Model.Unit.Zombie;

import Model.AStar;
import Model.Settings;
import Model.Unit.Unit;

import java.awt.*;

public abstract class Zombie extends Unit
{
  int lastDecision = 0;

  boolean knowsPlayerLocation = false;


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

  void canSmellPlayer()
  {
    knowsPlayerLocation = AStar.eculiDistanceFromPlayer(this.location) < Settings.zombieSmell;
  }
}
