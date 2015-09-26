package Model;

import Model.Unit.Zombie.Zombie;
import java.awt.Point;
import java.util.*;

/**
 * Created by jmweisburd on 9/24/15.
 */
public class AStar
{
  static Level level;
  PriorityQueue<Node> frontier =  new PriorityQueue();
  List<Node> visited = new ArrayList<>();
  LinkedList<Node> path = new LinkedList();
  List<Node> neighbors = new LinkedList<>();
  Node goal;

  public AStar(Level level)
  {
    this.level = level;
  }

  public static double eculiDistanceFromPlayer(Point zombLocation)
  {
    return zombLocation.distance(level.player.location);
  }

  private void search(Point zombLocation)
  {
    frontier.clear();
    path.clear();
    visited.clear();
    neighbors.clear();
    Node start = new Node(zombLocation,0);
    goal = new Node(level.player.getCenterLocation(), 0);
    System.out.println(""+ goal.tileX + " " + goal.tileY);
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
    while(current.parent!=null)
    {
      System.out.println(""+ current.tileX + " " + current.tileY);
      current = current.parent;
      path.add(current);
    }
  }

  private void setNeighbors(Node current)
  {
    if (level.houseTiles[current.tileX][current.tileY-1].isPassable())
    {
      neighbors.add(new Node(current.tileX, current.tileY-1, current.gScore + 1, current));
    }

    if (level.houseTiles[current.tileX][current.tileY+1].isPassable())
    {
      neighbors.add(new Node(current.tileX, current.tileY+1, current.gScore + 1, current));
    }

    if (level.houseTiles[current.tileX+1][current.tileY].isPassable())
    {
      neighbors.add(new Node(current.tileX+1, current.tileY, current.gScore + 1, current));
    }

    if (level.houseTiles[current.tileX-1][current.tileY].isPassable())
    {
      neighbors.add(new Node(current.tileX-1, current.tileY, current.gScore+1, current));
    }
  }

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

    if (toMoveTo.tileX == zombieLocation.x/Settings.TILE_SIZE && toMoveTo.tileY == zombieLocation.y/Settings.TILE_SIZE && path.size()>=1)
    {
      toMoveTo = path.removeLast();
    }
    else
    {
      return calcRotationAngleInDegrees(zombieLocation, level.player.getLocation());
    }

    System.out.println(zombieLocation.x/Settings.TILE_SIZE + " " + zombieLocation.y/Settings.TILE_SIZE);
    System.out.println(toMoveTo.tileX + " " + toMoveTo.tileY);
    System.out.println();



    pointMoveTo = level.houseTiles[toMoveTo.tileX][toMoveTo.tileY].getCenterLocation();


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

    System.out.println("" + angle);
    return angle;
  }

  private class Node implements Comparable<Node>
  {
    public int tileX;
    public int tileY;
    public int gScore;
    public int manhattanDistance;
    public int fScore;
    public Node parent;

    public Node(Point p, int gScore)
    {
      this.tileX = p.x/Settings.TILE_SIZE;
      this.tileY = p.y/Settings.TILE_SIZE;
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

    public void setManhattanDistance()
    {
      manhattanDistance = Math.abs(tileX - level.player.location.x/Settings.TILE_SIZE) + Math.abs(tileY-level.player.location.y/Settings.TILE_SIZE);
    }

    public void setfScore()
    {
      fScore = gScore + manhattanDistance;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (((Node) obj).tileX == this.tileX && ((Node) obj).tileY == this.tileY)
      {
        return true;
      }
      return false;
    }

    @Override
    public int compareTo(Node o)
    {
      return Integer.compare(this.fScore, ((Node) o).fScore);
    }
  }
}
