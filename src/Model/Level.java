package Model;

import Model.HouseGeneration.HouseGeneration;
import Model.Tile.Exit;
import Model.Tile.Tile;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
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
  public HouseGeneration houseGen;
  public int levelNum;
  public Tile[][] houseTiles;
  public Exit exit;
  public LinkedList<Zombie> zombieList;
  public LinkedList<Firetrap> firetrapList;
  public LinkedList<Fire> fireList;
  // fireTrapCount - when a player enters a new level the count should carry over
  //                 but when a player respawns, they should have as many as before.
  public int fireTrapCount;
  public Player player;
  public final AStar aStar;

  public Level( HouseGeneration houseGen )
  {
    this.houseGen = houseGen;
    player = houseGen.getPlayer();
    levelNum = houseGen.getCurrentLevelNum();
    houseTiles = houseGen.getHouseTiles();
    zombieList = houseGen.getZombieList();
    firetrapList = houseGen.getFiretrapList();
    fireTrapCount = houseGen.getFireTrapCount();
    exit = houseGen.getExit();
    this.aStar = new AStar(this);
    fireList = new LinkedList<Fire>();
  }

//  public void respawnLevel(HouseGeneration houseGen)
//  {
//    houseGen.respawnSameMap();
//    houseTiles = houseGen.getHouseTiles();
//    zombieList = houseGen.getZombieList();
//    fireTrapCount = houseGen.getFireTrapCount();
//  }
//
//  public void newLevel(HouseGeneration houseGen)
//  {
//    houseGen.spawnNewLevel();
//    levelNum = houseGen.getCurrentLevelNum();
//    houseTiles = houseGen.getHouseTiles();
//    zombieList = houseGen.getZombieList();
//  }
}
