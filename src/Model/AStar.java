package Model;

import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * AStar is the class responsible for calculating the shortest distance to the player.
 * <p>
 * From the program perspective, only the model uses the AStar class.
 * <p>
 * The data class level has a copy of AStar
 * Each zombie asks AStar for the shortest path if it can smell the player.
 * AStar hands the zombie a heading to the next tile it should move to
 * AStar calculates the shortest distance to the player through the AStar pathfinding algorithm.
 */
public class AStar
{
  private static Level level;
  private PriorityQueue<Node> frontier = new PriorityQueue<>();
  private List<Node> visited = new ArrayList<>();
  private LinkedList<Node> path = new LinkedList<>();
  private List<Node> neighbors = new LinkedList<>();

  /**
   * Constructor for AStar.  needs the current level object for computations
   *
   * @param level - level holds the 2d tile array used for pathing
   */
  public AStar(Level level)
  {
    AStar.level = level;
  }

  /**
   * Calculates the euclidian distance between the player and a zombie
   *
   * @param zombLocation - the point for a zombie's location
   * @return a pixel distance from zombie to player
   */
  public static double eculiDistanceFromPlayer(Point zombLocation)
  {
    return zombLocation.distance(level.PLAYER.getLocation());
  }

  private void search(Point zombLocation)
  {
    frontier.clear();
    path.clear();
    visited.clear();
    neighbors.clear();
    Node start = new Node(zombLocation, 0);
    Node goal = new Node(level.PLAYER.getCenterLocation(), 0);
    Node current;
    frontier.add(start);
    while (!frontier.isEmpty())
    {
      current = frontier.poll();
      visited.add(current);
      if (current.equals(goal))
      {
        makePath(current);
        return;
      }
      setNeighbors(current);
      for (Node node : neighbors)
      {
        if (!visited.contains(node))
        {
          frontier.add(node);
        }
      }
      neighbors.clear();
    }

  }

  private void makePath(Node current)
  {
    path.add(current);
    while (current.parent != null)
    {
      current = current.parent;
      path.add(current);
    }
  }

  private void setNeighbors(Node current)
  {
    if (level.TILES[current.tileX][current.tileY - 1].isPassable())
    {
      neighbors.add(new Node(current.tileX, current.tileY - 1, current.gScore + 1, current));
    }

    if (level.TILES[current.tileX][current.tileY + 1].isPassable())
    {
      neighbors.add(new Node(current.tileX, current.tileY + 1, current.gScore + 1, current));
    }

    if (level.TILES[current.tileX + 1][current.tileY].isPassable())
    {
      neighbors.add(new Node(current.tileX + 1, current.tileY, current.gScore + 1, current));
    }

    if (level.TILES[current.tileX - 1][current.tileY].isPassable())
    {
      neighbors.add(new Node(current.tileX - 1, current.tileY, current.gScore + 1, current));
    }
  }

  /**
   * Uses the AStar algorithm to determine which direction a zombie should 'currently' / actively head (not the full path)
   *
   * @param zombieLocation - the zombie's current location Point
   * @return angle - a double value giving a 360 degree angle (not radian)
   */
  public double getHeading(Point zombieLocation)
  {
    Node toMoveTo;
    Point pointMoveTo;

    search(zombieLocation);

    if (path.isEmpty())
    {
      return -1;
    }

    toMoveTo = path.removeLast();

    if (toMoveTo.tileX == zombieLocation.x / Settings.TILE_SIZE && toMoveTo.tileY == zombieLocation.y / Settings.TILE_SIZE && path.size() >= 1)
    {
      toMoveTo = path.removeLast();
    }
    else
    {
      return calcRotationAngleInDegrees(zombieLocation, level.PLAYER.getLocation());
    }

    pointMoveTo = level.TILES[toMoveTo.tileX][toMoveTo.tileY].getCenterLocation();

    return calcRotationAngleInDegrees(zombieLocation, pointMoveTo);
  }

  private double calcRotationAngleInDegrees(Point zombie, Point target)
  {
    double theta = Math.atan2(zombie.y - target.y, zombie.x - target.x);
    theta += Math.PI;
    double angle = Math.toDegrees(theta);

    if (angle < 0)
    {
      angle += 360;
    }

    return angle;
  }

  private class Node implements Comparable<Node>
  {
    private int tileX;
    private int tileY;
    private int gScore;
    private int manhattanDistance;
    private int fScore;
    private Node parent;

    @Override
    public boolean equals(Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      Node node = (Node) o;

      return tileX == node.tileX && tileY == node.tileY;

    }

    @Override
    public int hashCode()
    {
      int result = tileX;
      result = 31 * result + tileY;
      return result;
    }

    public Node(Point p, int gScore)
    {
      this.tileX = p.x / Settings.TILE_SIZE;
      this.tileY = p.y / Settings.TILE_SIZE;
      this.gScore = gScore;
      setManhattanDistance();
      setfScore();
      this.parent = null;
    }

    public Node(int x, int y, int gScore, Node current)
    {
      this.tileX = x;
      this.tileY = y;
      this.gScore = gScore;
      setManhattanDistance();
      setfScore();
      this.parent = current;
    }

    private void setManhattanDistance()
    {
      manhattanDistance = Math.abs(tileX - level.PLAYER.location.x / Settings.TILE_SIZE) + Math.abs(tileY - level.PLAYER.location.y / Settings.TILE_SIZE);
    }

    private void setfScore()
    {
      fScore = gScore + manhattanDistance;
    }

    @Override
    public int compareTo(@NotNull Node o)
    {
      return Integer.compare(this.fScore, o.fScore);
    }
  }
}
