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
  Set<Node> openList;
  PriorityQueue<Node> frontier =  new PriorityQueue();
  Set<Node> visited = new HashSet<>();
  LinkedList<Node> path = new LinkedList();
  Set<Node> neighbors = new HashSet<>();

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
    Node start = new Node(zombLocation,0);
    Node goal = new Node(level.player.location, 0);
    frontier.add(start);
    while (!frontier.isEmpty())
    {
      Node current = frontier.poll();
      visited.add(current);
      if (current == goal)
      {
        makePath(current);
        return;
      }
      neighbors = setNeighbors(current);
      for (Node node : neighbors)
      {
        if (!visited.contains(node))
        {
          frontier.add(node);

        }
      }
    }

  }

  private void makePath(Node current)
  {
    path.add(current);
    while(current.parent!=null)
    {
      current = current.parent;
      path.add(current);
    }
    Collections.reverse(path);
  }

  private Set<Node> setNeighbors(Node current)
  {
    Set<Node> currentNeighbors = new HashSet<>();

    if (level.houseTiles[current.tileX][current.tileY-1].isPassable())
    {
      currentNeighbors.add(new Node(current.tileX, current.tileY - 1, current.gScore + 1, current));
    }
    if (level.houseTiles[current.tileX+1][current.tileY].isPassable())
    {
      currentNeighbors.add(new Node(current.tileX+1, current.tileY, current.gScore+1, current));
    }
    if (level.houseTiles[current.tileX][current.tileY-1].isPassable())
    {
      currentNeighbors.add(new Node(current.tileX, current.tileY-1, current.gScore+1, current));
    }
    if (level.houseTiles[current.tileX-1][current.tileY].isPassable())
    {
      currentNeighbors.add(new Node(current.tileX-1, current.tileY, current.gScore+1, current));
    }
    return currentNeighbors;
  }

  public double getHeading(Point zombieLocation)
  {
    Node toMoveTo;

    search(zombieLocation);

    int locationTileX = zombieLocation.x/Settings.TILE_SIZE;
    int locationTileY = zombieLocation.y/Settings.TILE_SIZE;

    int moveToPixelX;
    int moveToPixelY;

    if (path.isEmpty())
    {
      return -1;
    }

    else {
      if (path.peekFirst().tileX == locationTileX && path.peekFirst().tileY == locationTileY)
      {
        toMoveTo = path.get(1);
      }
      else
      {
        toMoveTo = path.get(0);
      }
    }

    moveToPixelX = level.houseTiles[toMoveTo.tileX][toMoveTo.tileY].location.x-2;
    moveToPixelY = level.houseTiles[toMoveTo.tileX][toMoveTo.tileY].location.y-2;

    moveToPixelX = Math.abs(moveToPixelX - zombieLocation.x);
    moveToPixelY = Math.abs(moveToPixelY - zombieLocation.y);

    return (Math.toDegrees(Math.atan(moveToPixelY/moveToPixelX)));
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
      if (((((Node) obj)).tileX == this.tileX) && ((((Node) obj)).tileY == this.tileY))
      {
        return true;
      }
      return false;
    }
    /*
    @Override
    public int compare(Node n1, Node n2)
    {
      if (n1.fScore < n2.fScore)
      {
        return -1;
      }

      if (n1.fScore > n2.fScore)
      {
        return 1;
      }

      return 0;
    }*/

    @Override
    public int compareTo(Node o)
    {
      return Integer.compare(this.fScore,o.fScore);
    }
  }
}
