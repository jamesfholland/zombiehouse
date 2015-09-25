package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Firetrap;
import Model.Unit.Player;
import Model.Unit.Zombie.*;

import java.lang.Math;
import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class HouseGeneration is intended to be initialized one time by controller
 * and then is in charge of initializing/re-creating levels
 */
public class HouseGeneration
{
  int houseWidth, houseHeight;
  Tile[][] houseTiles;
  LinkedList<Zombie> zombieList;
  LinkedList<Firetrap> firetrapList;

  Player player;
  int levelInitFireTrapCount = 0;

  //Random Settings.RANDOM;

  int minFeatX, minFeatY;
  int maxFeatX, maxFeatY;

  int currentWallX, currentWallY;
  Direction currentDir;

  int playerX, playerY;
  int exitX, exitY;
  Direction exitDir;

  int currentLevelNum;
  Level currentLevel;

  // NOTE - with new design may not need to hold lastRandomSeed
  //      - new design = houseGeneration will hold a copy of the current house as basic start

  // held for house generation: on death/respawn, last known seed is used
  //                            on new level, take new seed
  private static long lastRandomSeed;

  public HouseGeneration(Player player)
  {
    houseWidth = Settings.PRACTICE_MAP_SIZE;
    houseHeight = Settings.PRACTICE_MAP_SIZE;
    houseTiles = new Tile[Settings.PRACTICE_MAP_SIZE][Settings.PRACTICE_MAP_SIZE];

    minFeatX = houseWidth / 10;
    minFeatY = houseHeight / 10;
    maxFeatX = houseWidth / 5;
    maxFeatY = houseHeight / 5;

    zombieList = new LinkedList<Zombie>();
    currentLevelNum = 1;

    this.player = player;
    lastRandomSeed = System.nanoTime();
    Settings.RANDOM.setSeed(lastRandomSeed);

    // bug fix: currentDir must be initialized or else first room
    currentDir = Direction.NORTH;

    //presetHouse(); // delete when makeNewHouse is ready
    aStarTestRoom();
    //makeNewHouse(); // uncomment when ready to use

    createLevel();

    this.player.setLevel(currentLevel);
  }


  // THE NEW APPROACH
  // using roguebasin.com's algorithm
  private void makeNewHouse()
  {
    // 1 - fill with walls
    allWalls();

    // 2 - carve a single room
    carveFirstRoom();

    // 3 - find wall segment to build off of
    // the major recurring section of algorithm
    boolean houseNotDone = true;
    int thingsCarved = 0;
    int failedCarvings = 0;
    while( houseNotDone )
    {
      boolean needWallSeg = true;
      while( needWallSeg )
      {
        needWallSeg = lookForBuildWall();
      }

    // 4 - decide on what to build (corridor or room)
    //   - after deciding, guess a possible size (within the min/max set above)
      double percentRoomChance = 0.65;
      boolean buildRoom;
      if ( Settings.RANDOM.nextDouble() < percentRoomChance ) { buildRoom = true; }
      else { buildRoom = false; }

      // the 4 variables that will be passed to check
      int neededWidth = 0;
      int neededHeight = 0;
      int cornerX = 0;
      int cornerY = 0;

      if(buildRoom)
      {
        neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX);
        neededHeight = minFeatY + Settings.RANDOM.nextInt(maxFeatY - minFeatY);
        if(currentDir == Direction.NORTH)
        {
          cornerX = currentWallX - Settings.RANDOM.nextInt(neededWidth);
          cornerY = currentWallY - neededHeight;
        }
        if(currentDir == Direction.EAST)
        {
          cornerX = currentWallX + 1;
          cornerY = currentWallY - Settings.RANDOM.nextInt(neededHeight);
        }
        if(currentDir == Direction.SOUTH)
        {
          cornerX = currentWallX - Settings.RANDOM.nextInt(neededWidth);
          cornerY = currentWallY + 1;
        }
        if(currentDir == Direction.WEST)
        {
          cornerX = currentWallX - neededWidth;
          cornerY = currentWallY - Settings.RANDOM.nextInt(neededHeight);
        }
      }
      if(!buildRoom)
      {
        if(currentDir == Direction.NORTH)
        {
          neededWidth = 1;
          neededHeight = minFeatY + Settings.RANDOM.nextInt(maxFeatY - minFeatY) - 1;
          cornerX = currentWallX;
          cornerY = currentWallY - neededHeight;
        }
        if(currentDir == Direction.EAST)
        {
          neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          neededHeight = 1;
          cornerX = currentWallX + 1;
          cornerY = currentWallY;
        }
        if(currentDir == Direction.SOUTH)
        {
          neededWidth = 1;
          neededHeight = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          cornerX = currentWallX;
          cornerY = currentWallY + 1;
        }
        if(currentDir == Direction.WEST)
        {
          neededWidth = minFeatX + Settings.RANDOM.nextInt(maxFeatX - minFeatX) - 1;
          neededHeight = 1;
          cornerX = currentWallX - neededHeight;
          cornerY = currentWallY;
        }
      }

    // 5 - check to see if the thing decided in step 4 can fit (without overwriting any other floors)
      if( isSpaceClear(cornerX, cornerY, neededWidth, neededHeight) )
      {
        carveRoom(cornerX, cornerY, neededWidth, neededHeight, buildRoom);
        houseTiles[currentWallX][currentWallY] = new Floor(new Point(currentWallX * Settings.TILE_SIZE, currentWallY * Settings.TILE_SIZE));
        // stupid end.. things carved.. just to test
        thingsCarved++;
      }
      else
      {
        failedCarvings++;
      }
      if (thingsCarved > 20) { houseNotDone = false; }
      if (failedCarvings > 100) { houseNotDone = false; }
    }

    // late step - place player (needed for quick testing)
    playerSpawn();

    // last step - place exit
    // trying to keep at a distance of at least some percentage distance of the map size (can't spawn next to player)
    exitSpawn();

    // last last step - remove excess walls (should help search/sight algorithms) --> is not working yet! breaks other codes..
    // removeHiddenWalls();
  }

  // allWalls is used to initialize all of house to wall
  private void allWalls() {
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
    for( int i = xTop; i < xSize + xTop; i++)
    {
      for( int j = yTop; j < ySize + yTop; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
        if( room )
        {
          houseTiles[i][j].setEmpty( !zombieSpawn(i, j) );
          if( houseTiles[i][j].isEmptyFloor() )
          {
            if( (i == (currentWallX + currentDir.getDX())) && (j == (currentWallY + currentDir.getDY())) ) { continue; }
            houseTiles[i][j].setEmpty( !obstacleSpawn(i, j) );
          }
        }
        //fireTrapSpawn();

      }
    }
  }

  private boolean lookForBuildWall()
  {
    int xRand = Settings.RANDOM.nextInt(houseWidth);
    int yRand = Settings.RANDOM.nextInt(houseHeight);
    int floorAdjacent = 0;
    if ( houseTiles[xRand][yRand].isWall() )
    {
      for( Direction dir : Direction.values() )
      {
        if ( offMap ( xRand + dir.getDX(), yRand + dir.getDY() ) ) { return true; }
        if ( houseTiles[xRand + dir.getDX()][yRand + dir.getDY()].isFloor() )
        {
          currentWallX = xRand;
          currentWallY = yRand;
          currentDir = dir.inverseDir();
          floorAdjacent++;
        }
      }
      if( floorAdjacent == 1) { return false; }
    }
    return true;
  }

  private boolean isSpaceClear(int xTop, int yTop, int xSize, int ySize)
  {
    for( int i = xTop - 1; i < xSize + xTop + 1; i++)
    {
      for( int j = yTop - 1; j < ySize + yTop + 1; j++)
      {
        if( offMap(i, j) ) { return false; }
        if( houseTiles[i][j].isFloor() ) { return false; }
      }
    }
    return true;
  }

  private boolean offMap(int x, int y)
  {
    if( x < 0 || x >= houseWidth) { return true; }
    if( y < 0 || y >= houseHeight) { return true; }
    return false;
  }

  private void playerSpawn()
  {
    boolean playerNotPlaced = true;
    while( playerNotPlaced )
    {
      playerX = Settings.RANDOM.nextInt(houseWidth);
      playerY = Settings.RANDOM.nextInt(houseHeight);
      if ( houseTiles[playerX][playerY].isEmptyFloor() )
      {
        player.setLocation(new Point(playerX * Settings.TILE_SIZE, playerY * Settings.TILE_SIZE));
        playerNotPlaced = false;
      }
    }
  }

  private boolean zombieSpawn(int x, int y)
  {
    if( Settings.RANDOM.nextDouble() < Settings.zombieSpawnRate )
    {
      if( Settings.RANDOM.nextBoolean() )
      {
        zombieList.add(new ZombieLine(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360)+Settings.RANDOM.nextDouble())));
      }
      else
      {
        zombieList.add(new ZombieRandom(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360)+Settings.RANDOM.nextDouble())));
      }
      return true;
    }
    return false;
  }

  private boolean obstacleSpawn(int x, int y)
  {
    if( Settings.RANDOM.nextDouble() < Settings.obstacleSpawnRate )
    {
      houseTiles[x][y] = new Pillar(new Point(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE));
      return true;
    }
    return false;
  }

  private void exitSpawn()
  {
    boolean exitNotPlaced = true;
    double percentMapMin = 0.3;

    int minMapDist = ((int)(houseWidth * percentMapMin)) + ((int)(houseHeight * percentMapMin));

    while( exitNotPlaced )
    {
      lookForBuildWall();
      int playerExitDistance = Math.abs(playerX - currentWallX) + Math.abs(playerY - currentWallY);
      if( playerExitDistance < minMapDist )
      {
        continue;
      }
      exitX = currentWallX + currentDir.getDX();
      exitY = currentWallY + currentDir.getDY();
      exitDir = currentDir.inverseDir();
      if( isSpaceClear(exitX, exitY, 1, 1) )
      {
        houseTiles[currentWallX][currentWallY] = new Floor(new Point(currentWallX * Settings.TILE_SIZE, currentWallY * Settings.TILE_SIZE));
        houseTiles[exitX][exitY] = new Exit(new Point(exitX * Settings.TILE_SIZE, exitY * Settings.TILE_SIZE));
        exitNotPlaced = false;
      }
    }
  }

  // Always always breaks...
  // mark for deletion
  // check all 8 directions before removal
  private void removeHiddenWalls()
  {
    for( int i = 0; i < houseWidth; i++ )
    {
      for( int j = 0; j < houseHeight; j++ )
      {
        int wallAdjacent = 0;
        for( Direction dir : Direction.values() )
        {
          if ( houseTiles[i][j].isWall() ) { wallAdjacent++; }
        }
        if( wallAdjacent == 4 ) { houseTiles[i][j] = null; }
      }
    }
  }


  private void createLevel()
  {
    currentLevel = new Level(currentLevelNum, houseTiles, zombieList, 3, player);
  }

  public void respawnSameMap()
  {
    Settings.RANDOM.setSeed(lastRandomSeed);
    makeNewHouse();
    createLevel();
  }

  public void spawnNewLevel()
  {
    lastRandomSeed = System.nanoTime();
    Settings.RANDOM.setSeed(lastRandomSeed);
    makeNewHouse();
    currentLevelNum++;
  }

  public Tile[][] getHouseTiles() { return houseTiles; }

  public LinkedList<Zombie> getZombieList() { return zombieList; }

  public int getFireTrapCount() { return levelInitFireTrapCount; }

  public Level getCurrentLevel() { return currentLevel; }

  public int getCurrentLevelNum() { return currentLevelNum; }


  // DEMO + PRACTICE MAP below.  to be deleted on completion.


  // Quick aStar test case.
  // 5x11 room, with wall seperating halfs
  private void aStarTestRoom()
  {
    playerX = 3;
    playerY = 2;

    allWalls();

    // default used to fill house with open floor
    // makeRoom(1, 1, Settings.PRACTICE_MAP_SIZE - 2, Settings.PRACTICE_MAP_SIZE - 2);
    for( int i = 1; i < 6; i++ )
    {
      for( int j = 1; j < 6; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
        houseTiles[i + 6][j] = new Floor(new Point((i + 6) * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }
    houseTiles[6][5] = new Floor(new Point(6 * Settings.TILE_SIZE, 5 * Settings.TILE_SIZE));

    houseTiles[12][1] = new Exit(new Point(12 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
    houseTiles[12][2] = new Exit(new Point(12 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));

    player.setLocation(new Point(playerX * Settings.TILE_SIZE, playerY * Settings.TILE_SIZE));

    zombieList.add(new ZombieLine(11 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE, (Settings.RANDOM.nextInt(360) + Settings.RANDOM.nextDouble())));
  }
}
