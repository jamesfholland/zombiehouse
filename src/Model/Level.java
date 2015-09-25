package Model;

import Model.HouseGeneration.HouseGeneration;
import Model.Tile.Tile;
import Model.Unit.Player;
import Model.Unit.Zombie.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Level holds the values for the current 'Level' / floor of the zombie house
 */
public class Level
{
  public int levelNum;
  public Tile[][] houseTiles;
  public LinkedList<Zombie> zombieList;
  // fireTrapCount - when a player enters a new level the count should carry over
  //                 but when a player respawns, they should have as many as before.
  public int fireTrapCount;
  public Player player;
  public final AStar aStar;

  // Add in a list of collidable tiles

  public Level(int levelNum, Tile[][] houseTiles, LinkedList<Zombie> zombieList, int fireTrapCount, Player player)
  {
    this.levelNum = levelNum;
    this.houseTiles = houseTiles;
    this.zombieList = zombieList;
    this.fireTrapCount = fireTrapCount;
    this.player = player;
    this.aStar = new AStar(this);
  }

  public void respawnLevel(HouseGeneration houseGen)
  {
    houseGen.respawnSameMap();
    houseTiles = houseGen.getHouseTiles();
    zombieList = houseGen.getZombieList();
    fireTrapCount = houseGen.getFireTrapCount();
  }

  public void newLevel(HouseGeneration houseGen)
  {
    houseGen.spawnNewLevel();
    levelNum = houseGen.getCurrentLevelNum();
    houseTiles = houseGen.getHouseTiles();
    zombieList = houseGen.getZombieList();

  }

  @Override
  public String toString()
  {
    StringBuilder houseString = new StringBuilder();
    for (int i = 0; i < Settings.PRACTICE_MAP_SIZE; i++)
    {
      for (int j = 0; j < Settings.PRACTICE_MAP_SIZE; j++)
      {
        houseString.append(houseTiles[j][i].toString());
      }
      houseString.append("\n");
    }
    return houseString.toString();
  }

}
