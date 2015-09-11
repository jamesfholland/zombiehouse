package Model;

import Model.Tile.Tile;
import Model.Unit.Player;
import Model.Unit.Zombie.*;
import java.util.ArrayList;

/**
 * Level holds the values for the current 'Level' / floor of the zombie house
 */
public class Level
{
  private int levelNum;
  private Tile[][] houseTiles;
  private ArrayList<Zombie> zombieList;
  // fireTrapCount - when a player enters a new level the count should carry over
  //                 but when a player respawns, they should have as many as before.
  private int fireTrapCount;

  public Level(int levelNum, Tile[][] houseTiles, ArrayList<Zombie> zombieList, int fireTrapCount)
  {
    this.levelNum = levelNum;
    this.houseTiles = houseTiles;
    this.zombieList = zombieList;
    this.fireTrapCount = fireTrapCount;
  }

  public int getLevelNum() { return levelNum; }

  public Tile[][] getHouseTiles() { return houseTiles; }

  public ArrayList<Zombie> getZombieList() { return zombieList; }

  public int getFireTrapCount() { return fireTrapCount; }
}
