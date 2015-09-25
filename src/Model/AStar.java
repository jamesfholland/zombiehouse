package Model;

import Model.Unit.Zombie.Zombie;

import java.awt.*;

/**
 * Created by jmweisburd on 9/24/15.
 */
public class AStar
{
  static Level level;
  static private Point playerPoint;
  private double euclDistance;
  private double xDiff;
  private double yDiff;

  public AStar(Level level)
  {
    this.level = level;
  }

  public double eculiDistanceFromPlayer(Point zombLocation)
  {
    return zombLocation.distance(level.player.location);
  }

}
