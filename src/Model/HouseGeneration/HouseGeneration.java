package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Firetrap;
import Model.Unit.Player;
import Model.Unit.Zombie.*;
import Model.Direction;

import java.awt.*;
import java.util.LinkedList;

/**
 * Class HouseGeneration is intended to be initialized one time by controller
 * and then is in charge of initializing/re-creating levels
 */
public class HouseGeneration
{
  private int houseWidth, houseHeight;
  private Tile[][] houseTiles;
  private LinkedList<Zombie> zombieList;
  private LinkedList<Firetrap> firetrapList;

  private Player player;
  private int levelInitFireTrapCount = 0;

  private int minFeatX, minFeatY;
  private int maxFeatX, maxFeatY;

  private int currentWallX, currentWallY;
  private Direction currentDir;

  private int playerX, playerY;
  private int exitX, exitY;

  private int currentLevelNum;
  private Level currentLevel;

  // held for house generation: on death/respawn, last known seed is used
  //                            on new level, take new seed
  private static long lastRandomSeed;

  /**
   * Constructor for HouseGeneration
   * Should be called exactly once by the controller
   */
  public HouseGeneration()
  {
    houseWidth = Settings.PRACTICE_MAP_SIZE;
    houseHeight = Settings.PRACTICE_MAP_SIZE;
    houseTiles = new Tile[Settings.PRACTICE_MAP_SIZE][Settings.PRACTICE_MAP_SIZE];

    minFeatX = houseWidth / 10;
    minFeatY = houseHeight / 10;
    maxFeatX = houseWidth / 5;
    maxFeatY = houseHeight / 5;

    currentLevelNum = 1;

    lastRandomSeed = System.nanoTime();
    Settings.RANDOM.setSeed(lastRandomSeed);

    currentDir = Direction.NORTH;

    aStarTestRoom();
    //makeNewHouse(); // uncomment when ready to use

    createLevel();

    this.player.setLevel(currentLevel);
  }


  /**
   * makeNewHouse() is the method that holds the algorithm used for procedural generation
   * the algorithm is borrowed from RougeBasin.com
   */
  private void makeNewHouse()
  {
    // initialize new lists:
    zombieList = new LinkedList<>();
    firetrapList = new LinkedList<>();

    // 1 - fill with walls
    allWalls();

    // 2 - carve a single room
    carveFirstRoom();

    // 3 - find wall segment to build off of
    // the major recurring section of algorithm
    boolean houseNotDone = true;
    int failedCarvings = 0;
    int roomCount = 0;
    int corridorCount = 0;
    int pillarCount = 10;
    while (houseNotDone)
    {
      boolean needWallSeg = true;
      while (needWallSeg)
      {
        needWallSeg = lookForBuildWall();
      }

      // 4 - decide on what to build (corridor or room)
      //   - after deciding, guess a possible size (within the min/max set above)
      double percentRoomChance = 0.65;
      boolean buildRoom;
      buildRoom = Settings.RANDOM.nextDouble() < percentRoomChance;

      // the 4 variables that will be passed to check
      int neededWidth = 0;
      int neededHeight = 0;
      int cornerX = 0;
      int cornerY = 0;

      if (buildRoom)
      {
        neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX);
        neededHeight = minFeatY + Settings.RANDOM.nextInt(maxFeatY - minFeatY);
        if (currentDir == Direction.NORTH)
        {
          cornerX = currentWallX - Settings.RANDOM.nextInt(neededWidth);
          cornerY = currentWallY - neededHeight;
        }
        if (currentDir == Direction.EAST)
        {
          cornerX = currentWallX + 1;
          cornerY = currentWallY - Settings.RANDOM.nextInt(neededHeight);
        }
        if (currentDir == Direction.SOUTH)
        {
          cornerX = currentWallX - Settings.RANDOM.nextInt(neededWidth);
          cornerY = currentWallY + 1;
        }
        if (currentDir == Direction.WEST)
        {
          cornerX = currentWallX - neededWidth;
          cornerY = currentWallY - Settings.RANDOM.nextInt(neededHeight);
        }
      }
      if (!buildRoom)
      {
        if (currentDir == Direction.NORTH)
        {
          neededWidth = 1;
          neededHeight = minFeatY + Settings.RANDOM.nextInt(maxFeatY - minFeatY) - 1;
          cornerX = currentWallX;
          cornerY = currentWallY - neededHeight;
        }
        if (currentDir == Direction.EAST)
        {
          neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          neededHeight = 1;
          cornerX = currentWallX + 1;
          cornerY = currentWallY;
        }
        if (currentDir == Direction.SOUTH)
        {
          neededWidth = 1;
          neededHeight = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          cornerX = currentWallX;
          cornerY = currentWallY + 1;
        }
        if (currentDir == Direction.WEST)
        {
          neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          neededHeight = 1;
          cornerX = currentWallX - neededHeight;
          cornerY = currentWallY;
        }
      }

      // 5 - check to see if the thing decided in step 4 can fit (without overwriting any other floors)
      if (isSpaceClear(cornerX, cornerY, neededWidth, neededHeight))
      {
        carveRoom(cornerX, cornerY, neededWidth, neededHeight, buildRoom);
        houseTiles[currentWallX][currentWallY] = new Floor(new Point(currentWallX * Settings.TILE_SIZE, currentWallY * Settings.TILE_SIZE));
        // stupid end.. things carved.. just to test
        if (buildRoom)
        {
          roomCount++;
        } else
        {
          corridorCount++;
        }
      } else
      {
        failedCarvings++;
      }
      if (roomCount >= Settings.DEFAULT_NUMBER_ROOMS && corridorCount >= Settings.DEFAULT_NUMBER_HALLS && pillarCount >= Settings.DEFAULT_NUMBER_OBSTACLES)
      {
        houseNotDone = false;
      }
      if (failedCarvings > 1500)
      {
        houseNotDone = false;
      } // rather than just stopping might be good to restart..
    }

    // late step - place player
    playerSpawn();

    // 2nd to last step - place exit
    exitSpawn();

    // last last step - remove excess walls
    removeHiddenWalls();

    createLevel();
  }

  // allWalls is used to initialize all of house to wall
  private void allWalls()
  {
    for (int i = 0; i < houseWidth; i++)
    {
      for (int j = 0; j < houseHeight; j++)
      {
        houseTiles[i][j] = new Wall(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }
  }

  // creates a random size room 'near' the center of the map
  private void carveFirstRoom()
  {
    int houseMidX = houseWidth / 2;
    int houseMidY = houseHeight / 2;

    int xTop = houseMidX - Settings.RANDOM.nextInt(houseWidth / 4);
    int yTop = houseMidY - Settings.RANDOM.nextInt(houseWidth / 4);
    int xSize = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX);
    int ySize = minFeatY + Settings.RANDOM.nextInt(maxFeatY - minFeatY);

    carveRoom(xTop, yTop, xSize, ySize, true);
  }

  // carveRoom is the generalized room carving
  // room variable --> rooms can spawn pillars and zombies (might allow zombies in cooridors - seems unfair, but closer to .002)
  // room = false --> corridors can spawn firetraps
  private void carveRoom(int xTop, int yTop, int xSize, int ySize, boolean room)
  {
    for (int i = xTop; i < xSize + xTop; i++)
    {
      for (int j = yTop; j < ySize + yTop; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
        if (room)
        {
          houseTiles[i][j].setEmpty(!zombieSpawn(i, j));
          if (houseTiles[i][j].isEmptyFloor())
          {
            if ((i == (currentWallX + currentDir.getDX())) && (j == (currentWallY + currentDir.getDY())))
            {
              continue;
            }
            houseTiles[i][j].setEmpty(!obstacleSpawn(i, j));
          }
        }
        if (houseTiles[i][j].isEmptyFloor())
        {
          fireTrapSpawn(i, j);
        }
      }
    }
  }

  private boolean lookForBuildWall()
  {
    int xRand = Settings.RANDOM.nextInt(houseWidth);
    int yRand = Settings.RANDOM.nextInt(houseHeight);
    int floorAdjacent = 0;
    if (houseTiles[xRand][yRand].isWall())
    {
      for (Direction dir : Direction.values())
      {
        if (offMap(xRand + dir.getDX(), yRand + dir.getDY()))
        {
          return true;
        }
        if (houseTiles[xRand + dir.getDX()][yRand + dir.getDY()].isFloor())
        {
          currentWallX = xRand;
          currentWallY = yRand;
          currentDir = dir.inverseDir();
          floorAdjacent++;
        }
      }
      if (floorAdjacent == 1)
      {
        return false;
      }
    }
    return true;
  }

  private boolean isSpaceClear(int xTop, int yTop, int xSize, int ySize)
  {
    for (int i = xTop - 1; i < xSize + xTop + 1; i++)
    {
      for (int j = yTop - 1; j < ySize + yTop + 1; j++)
      {
        if (offMap(i, j))
        {
          return false;
        }
        if (houseTiles[i][j].isFloor())
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean offMap(int x, int y)
  {
    return x < 0 || x >= houseWidth || y < 0 || y >= houseHeight;
  }

  private void playerSpawn()
  {
    boolean playerNotPlaced = true;
    while (playerNotPlaced)
    {
      playerX = Settings.RANDOM.nextInt(houseWidth);
      playerY = Settings.RANDOM.nextInt(houseHeight);
      if (houseTiles[playerX][playerY].isEmptyFloor())
      {
        this.player = new Player(new Point(playerX * Settings.TILE_SIZE, playerY * Settings.TILE_SIZE));
        playerNotPlaced = false;
      }
    }
  }

  private boolean zombieSpawn(int x, int y)
  {
    if (Settings.RANDOM.nextDouble() < Settings.zombieSpawnRate)
    {
      if (Settings.RANDOM.nextBoolean())
      {
        zombieList.add(new ZombieLine(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360) + Settings.RANDOM.nextDouble())));
      } else
      {
        zombieList.add(new ZombieRandom(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360) + Settings.RANDOM.nextDouble())));
      }
      return true;
    }
    return false;
  }

  private boolean obstacleSpawn(int x, int y)
  {
    if (Settings.RANDOM.nextDouble() < Settings.OBSTACLE_SPAWN_RATE)
    {
      houseTiles[x][y] = new Pillar(new Point(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE));
      return true;
    }
    return false;
  }

  private void fireTrapSpawn(int x, int y)
  {
    if (Settings.RANDOM.nextDouble() < Settings.firetrapSpawnRate)
    {
      firetrapList.add(new Firetrap(new Point(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE)));
    }
  }

  private void exitSpawn()
  {
    boolean exitNotPlaced = true;
    double percentMapMin = 0.3;

    int minMapDist = ((int) (houseWidth * percentMapMin)) + ((int) (houseHeight * percentMapMin));

    while (exitNotPlaced)
    {
      lookForBuildWall();
      int playerExitDistance = Math.abs(playerX - currentWallX) + Math.abs(playerY - currentWallY);
      if (playerExitDistance < minMapDist)
      {
        continue;
      }
      exitX = currentWallX + currentDir.getDX();
      exitY = currentWallY + currentDir.getDY();
      if (isSpaceClear(exitX, exitY, 1, 1))
      {
        houseTiles[currentWallX][currentWallY] = new Floor(new Point(currentWallX * Settings.TILE_SIZE, currentWallY * Settings.TILE_SIZE));
        houseTiles[exitX][exitY] = new Exit(new Point(exitX * Settings.TILE_SIZE, exitY * Settings.TILE_SIZE));
        exitNotPlaced = false;
      }
    }
  }

  // method to remove walls that can't be seen
  private void removeHiddenWalls()
  {

    for (int i = 0; i < houseWidth; i++)
    {
      for (int j = 0; j < houseHeight; j++)
      {
        int wallAdjacent = 0;
        for (int dX = -1; dX <= 1; dX++)
        {
          for (int dY = -1; dY <= 1; dY++)
          {
            if (offMap(i + dX, j + dY))
            {
              wallAdjacent++;
              continue;
            }
            if (houseTiles[i + dX][j + dY].isWall())
            {
              wallAdjacent++;
            }
          }
          if (wallAdjacent == 9)
          {
            houseTiles[i][j].markForDeletion();
          }
        }
      }
    }
    for (int i = 0; i < houseWidth; i++)
    {
      for (int j = 0; j < houseHeight; j++)
      {
        if (houseTiles[i][j].getDeletion())
        {
          houseTiles[i][j] = null;
        }
      }
    }
  }


  private void createLevel()
  {
    currentLevel = new Level(this);
  }

  /**
   * respawnSameMap() is used to recreate a level on playerDeath()
   * uses a saved seed of the level to retrieve same random calls to recreate a level
   */
  public void respawnSameMap()
  {
    Settings.RANDOM.setSeed(lastRandomSeed);
    makeNewHouse();
    createLevel();
    this.player.setLevel(currentLevel);
  }

  /**
   * spawnNewLevel() is used to create a new level on playerWinLevel()
   * generates a new random seed, saves it and uses it to create a new level
   */
  public void spawnNewLevel()
  {
    lastRandomSeed = System.nanoTime();
    Settings.RANDOM.setSeed(lastRandomSeed);
    makeNewHouse();
    currentLevelNum++;
    this.player.setLevel(currentLevel);
  }

  /**
   * Returns a 2d array of tiles
   * @return houseTiles
   */
  public Tile[][] getHouseTiles()
  {
    return houseTiles;
  }

  /**
   * Returns a linked list of zombies
   * @return zombieList
   */
  public LinkedList<Zombie> getZombieList()
  {
    return zombieList;
  }

  /**
   * Returns the number of firetraps held by the player at level start
   * @return levelInitFireTrapCount
   */
  public int getFireTrapCount()
  {
    return levelInitFireTrapCount;
  }

  /**
   * Returns the full Level object that contains all level details
   * @return currentLevel
   */
  public Level getCurrentLevel()
  {
    return currentLevel;
  }

  /**
   * Returns the number of the current level
   * 1-indexed
   * @return currentLevelNum
   */
  public int getCurrentLevelNum()
  {
    return currentLevelNum;
  }

  /**
   * HouseGeneration constructs a new player object for each level
   * Returns the player
   * @return player
   */
  public Player getPlayer()
  {
    return player;
  }

  /**
   * Returns a linked list of Firetraps
   * @return firetrapList
   */
  public LinkedList<Firetrap> getFiretrapList()
  {
    return firetrapList;
  }

  /**
   * Returns the exit object
   * @return Exit
   */
  public Exit getExit()
  {
    return (Exit) houseTiles[exitX][exitY];
  }


  // DEMO + PRACTICE MAP below.  to be deleted on completion.
  private void aStarTestRoom()
  {
    // initialize new lists:
    zombieList = new LinkedList<>();
    firetrapList = new LinkedList<>();

    allWalls();

    for (int i = 1; i < 6; i++)
    {
      for (int j = 1; j < 6; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
        houseTiles[i + 6][j] = new Floor(new Point((i + 6) * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }
    houseTiles[6][5] = new Floor(new Point(6 * Settings.TILE_SIZE, 5 * Settings.TILE_SIZE));

    houseTiles[12][2] = new Exit(new Point(12 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));
    exitX = 12;
    exitY = 2;

    playerSpawn();

    houseTiles[5][4] = new Pillar(new Point(5 * Settings.TILE_SIZE, 4 * Settings.TILE_SIZE));

    zombieList.add(new ZombieLine(11 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360) + Settings.RANDOM.nextDouble())));

    firetrapList.add(new Firetrap(new Point(6 * Settings.TILE_SIZE, 5 * Settings.TILE_SIZE)));
  }
}
