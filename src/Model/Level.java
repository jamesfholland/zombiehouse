package Model;

import Model.Tile.Exit;
import Model.Tile.Tile;
import Model.Unit.Fire;
import Model.Unit.Firetrap;
import Model.Unit.Player;
import Model.Unit.Zombie.Zombie;
import Model.Unit.Zombie.ZombieMaster;

import java.util.LinkedList;

/**
 * From the perspective of the program as a whole, the Level class is essentially a manager for all of the model.
 * Zombiehouse uses Level as a data class, a place to hold all of the game objects which make up a level of zombiehouse.
 * Thus, it contains  an instance of every GameObject type. The view reads from the level to know where to draw game objects,
 * and the controller tells the level to update the moving GameObjects on update calls.
 * <p>
 * Many of the Zombiehouse classes use level. GameObjects themselves have a static copy of level so they can check where they are/
 * if they're running into anything/need to know the player location.
 */
public class Level
{
  public final int LEVEL_NUM;
  public final Tile[][] TILES;
  public final Exit EXIT;
  public final LinkedList<Zombie> ZOMBIES;
  public final LinkedList<Firetrap> FIRETRAPS;
  public final LinkedList<Fire> FIRES;
  public final ZombieMaster MASTER;
  // fireTrapCount - when a PLAYER enters a new level the count should carry over
  //                 but when a PLAYER respawns, they should have as many as before.
  public int fireTrapCount;
  public final Player PLAYER;
  public final AStar ASTAR;

  /**
   * Level is constructed from the HouseGenerator
   *
   * @param houseGen - contains all the values needed for a level
   */
  public Level(HouseGeneration houseGen)
  {
    PLAYER = houseGen.getPlayer();
    LEVEL_NUM = houseGen.getCurrentLevelNum();
    TILES = houseGen.getHouseTiles();
    ZOMBIES = houseGen.getZombieList();
    MASTER = houseGen.getMasterZombie();
    FIRETRAPS = houseGen.getFiretrapList();
    fireTrapCount = houseGen.getFireTrapCount();
    EXIT = houseGen.getExit();
    ASTAR = new AStar(this);
    FIRES = new LinkedList<>();
  }
}
