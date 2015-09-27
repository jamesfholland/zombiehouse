package Model;

import Model.HouseGeneration.HouseGeneration;
import Model.Tile.Exit;
import Model.Tile.Tile;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Player;
import Model.Unit.Zombie.Zombie;

import java.util.LinkedList;

/**
 * Level holds the values for the current 'Level' / floor of the zombie house
 */
public class Level
{
  public final int LEVEL_NUM;
  public final Tile[][] TILES;
  public final Exit EXIT;
  public final LinkedList<Zombie> ZOMBIES;
  public final LinkedList<Firetrap> FIRETRAPS;
  public final LinkedList<Fire> FIRES;
  // fireTrapCount - when a PLAYER enters a new level the count should carry over
  //                 but when a PLAYER respawns, they should have as many as before.
  public int fireTrapCount;
  public final Player PLAYER;
  public final AStar ASTAR;

  public Level(HouseGeneration houseGen)
  {
    PLAYER = houseGen.getPlayer();
    LEVEL_NUM = houseGen.getCurrentLevelNum();
    TILES = houseGen.getHouseTiles();
    ZOMBIES = houseGen.getZombieList();
    FIRETRAPS = houseGen.getFiretrapList();
    fireTrapCount = houseGen.getFireTrapCount() + 1;
    EXIT = houseGen.getExit();
    ASTAR = new AStar(this);
    FIRES = new LinkedList<>();
  }
}
