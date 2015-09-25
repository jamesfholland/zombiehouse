package Model;

import Model.Unit.Zombie.Zombie;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by jmweisburd on 9/24/15.
 */
public class AStar
{
  static Level level;
  Set<tileData> openList;

  public AStar(Level level)
  {
    this.level = level;
  }

  public static double eculiDistanceFromPlayer(Point zombLocation)
  {
    return zombLocation.distance(level.player.location);
  }

  public static double getHeading(int tileX, int tileY)
  {

    return 0.0;
  }

  private class tileData
  {
    public int tileX;
    public int tileY;
    public boolean passable;
  }
}
