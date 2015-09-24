package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Player;
import Model.Unit.Zombie.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Class HouseGeneration is intended to be initialized one time by controller
 * and then is in charge of initializing/re-creating levels
 */
public class HouseGeneration
{
  int houseWidth, houseHeight;
  Tile[][] houseTiles;
  ArrayList<Zombie> zombieArrayList;
  Player player;
  int playerSpawnX, playerSpawnY;
  Random randGen;

  int minFeatX, minFeatY;
  int maxFeatX, maxFeatY;

  int currentWallX, currentWallY;
  Direction currentDir;

  int currentLevelNum;
  Level currentLevel;

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

    zombieArrayList = new ArrayList<Zombie>(20);
    currentLevelNum = 1;

    this.player = player;
    lastRandomSeed = System.nanoTime();
    randGen = new Random(lastRandomSeed);

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
    boolean houseNotDone = false;                 /// DO NOT WANT TO TEST THIS SEGMENT YET --> change to true when ready
    while( houseNotDone )
    {
      boolean needWallSeg = true;
      while( needWallSeg )
      {
        needWallSeg = lookForBuildWall();
      }

    // 4 - decide on what to build (corridor or room)
    //   - after deciding, guess a possible size (within the min/max set above)
      double percentRoomChance = 0.6;
      boolean buildRoom;
      if ( randGen.nextDouble() < percentRoomChance ) { buildRoom = true; }
      else { buildRoom = false; }

      int neededWidth, neededHeight;
      int cornerX, cornerY;

      if(buildRoom)
      {
        neededWidth = minFeatX + randGen.nextInt(maxFeatX - minFeatX);
        neededHeight = minFeatY + randGen.nextInt(maxFeatY - minFeatY);
        if(currentDir == Direction.NORTH)
        {
          cornerX = currentWallX - randGen.nextInt(neededWidth);
          cornerY = currentWallY - neededHeight + currentDir.getDY();
        }
        if(currentDir == Direction.EAST)
        {
          cornerX = currentWallX + 1;
          cornerY = currentWallY - randGen.nextInt(neededHeight);
        }
        if(currentDir == Direction.SOUTH)
        {
          cornerX = currentWallX - randGen.nextInt(neededWidth);
          cornerY = currentWallY + currentDir.getDY();
        }
        if(currentDir == Direction.WEST)
        {
          cornerX = currentWallX - neededWidth - 1;
          cornerY = currentWallY - randGen.nextInt(neededHeight);
        }
      }
      else
      {
        if(currentDir == Direction.NORTH)
        {
          neededWidth = 1;
          neededHeight = minFeatX + randGen.nextInt(maxFeatX - minFeatX);
          cornerX = currentWallX;
          cornerY = currentWallY - neededHeight;
        }
        if(currentDir == Direction.EAST)
        {
          neededWidth = minFeatY + randGen.nextInt(maxFeatY - minFeatY);
          neededHeight = 1;
          cornerX = currentWallX + 1;
          cornerY = currentWallY;
        }
        if(currentDir == Direction.SOUTH)
        {
          neededWidth = 1;
          neededHeight = minFeatX + randGen.nextInt(maxFeatX - minFeatX);
          cornerX = currentWallX - randGen.nextInt(neededWidth);
          cornerY = currentWallY - 1;
        }
        if(currentDir == Direction.WEST)
        {
          neededWidth = 1;
          neededHeight = minFeatX + randGen.nextInt(maxFeatX - minFeatX);
          cornerX = currentWallX - 1;
          cornerY = currentWallY - randGen.nextInt(neededHeight);
        }
      }




    // 5 - check to see if the thing decided in step 4 can fit (without overwriting any other floors)

    }



    // late step - place player (needed for quick testing)
    boolean playerNotPlaced = true;
    while( playerNotPlaced )
    {
      int xRand = randGen.nextInt(houseWidth);
      int yRand = randGen.nextInt(houseHeight);
      if ( houseTiles[xRand][yRand].isEmptyFloor() )
      {
        player.setLocation(new Point(xRand * Settings.TILE_SIZE, yRand * Settings.TILE_SIZE));
        playerNotPlaced = false;
      }
    }
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

    int xTop = houseMidX - randGen.nextInt(houseWidth / 4);
    int yTop = houseMidY - randGen.nextInt(houseWidth / 4);
    int xSize = minFeatX + randGen.nextInt(maxFeatX - minFeatX);
    int ySize = minFeatY + randGen.nextInt(maxFeatY - minFeatY);

    carveRoom(xTop, yTop, xSize, ySize);
  }

  // carveRoom is the generalized room carving
  private void carveRoom(int xTop, int yTop, int xSize, int ySize)
  {
    for( int i = xTop; i < xSize + xTop; i++)
    {
      for( int j = yTop; j < ySize + yTop; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }
  }

  private boolean lookForBuildWall()
  {
    int xRand = randGen.nextInt(houseWidth);
    int yRand = randGen.nextInt(houseHeight);
    if ( houseTiles[xRand][yRand].isWall() )
    {
      for( Direction dir : Direction.values() )
      {
        if ( houseTiles[xRand + dir.getDX()][yRand + dir.getDY()].isFloor() )
        {
          currentWallX = xRand;
          currentWallY = yRand;
          currentDir = dir;
          return true;
        }
      }
    }
    return false;
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



  // THE OLD WAY - remove when makeNewHouse works -

  // preset house is a 100 x 100 house with 6 rooms (possibly overlapping)
  // and a grid of hallways between them
  private void presetHouse()
  {
    playerSpawnX = -1;
    playerSpawnY = -1;

    allWalls();

    // not perment but want to be able to traverse rooms
    // placing a grid of hallways such that you can visit each room
    for( int i = 4; i < houseHeight - 1; i += 10 )
    {
      for( int j = 1; j < houseWidth - 1; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }
    for( int i = 4; i < houseWidth - 1; i += 10 )
    {
      for( int j = 1; j < houseHeight - 1; j++)
      {
        houseTiles[j][i] = new Floor(new Point(j * Settings.TILE_SIZE, i * Settings.TILE_SIZE));
      }
    }

    // default used to fill house with open floor
    // makeRoom(1, 1, Settings.PRACTICE_MAP_SIZE - 2, Settings.PRACTICE_MAP_SIZE - 2);
    for( int i = 0; i < Settings.DEFAULT_NUMBER_ROOMS; i++ )
    {
      makeRoom();
    }

    player.setLocation(new Point(playerSpawnX * Settings.TILE_SIZE, playerSpawnY * Settings.TILE_SIZE));


    //    houseTiles[1][1] = new Pillar(new Point(1 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
    //    houseTiles[10][10] = new Pillar(new Point(10 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));
    //    houseTiles[12][10] = new Pillar(new Point(12 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));

    //houseTiles[0][20] = new Exit(new Point(0 * Settings.TILE_SIZE, 20 * Settings.TILE_SIZE));

  }

  // to be deleted WITH presetHouse()
  private void makeRoom()
  {
    // generate size and placement of room
    int xSize = 0;
    xSize = randGen.nextInt(10) +5;
    int ySize = 0;
    ySize = randGen.nextInt(10) +5;
    int xTop = randGen.nextInt(houseWidth - xSize - 2) + 1;
    int yTop = randGen.nextInt(houseHeight - ySize - 2) + 1;

    // fill array with floor tiles (and appropriate spawns)
    for( int i = 0; i < xSize; i++ )
    {
      for( int j = 0; j < ySize; j++ )
      {
        houseTiles[xTop + i][yTop + j] = new Floor(new Point((xTop + i) * Settings.TILE_SIZE, (yTop + j) * Settings.TILE_SIZE));
        boolean tileEmpty = true;
        tileEmpty = ! zombieSpawn(xTop + i, yTop + j);
        // fireTrapSpawn(xTop + i, yTop + j); implement when ready to test with firetraps
        //if (j == ySize/2 && i == xSize/2)
        //{
          //zombieSpawn(i,j);
        //}
        if(tileEmpty)
        {
          tileEmpty = ! playerSpawn(xTop + i, yTop + j);
        }
        if(tileEmpty && randGen.nextDouble() < 0.02)
        {
          houseTiles[xTop + i][yTop + j] = new Pillar(new Point((xTop + i) * Settings.TILE_SIZE, (yTop + j) * Settings.TILE_SIZE));
        }
      }

    }
  }

  // to be deleted with presetHouse()
  private boolean playerSpawn(int x, int y)
  {
    if( randGen.nextInt(100) < 4 || playerSpawnX < 1 )
    {
      playerSpawnX = x;
      playerSpawnY = y;
      return true;
    }
    return false;
  }

  // maybe keep with new algorithm
  private boolean zombieSpawn(int x, int y)
  {

    if( randGen.nextDouble() < 0.02 )
    {
      zombieArrayList.add(new ZombieLine(x * Settings.TILE_SIZE, y * Settings.TILE_SIZE, (randGen.nextInt(360)+randGen.nextDouble())));
      return true;
    }
    return false;
  }

  private void createLevel()
  {
    currentLevel = new Level(1, houseTiles, zombieArrayList, 3, player);
  }

  public Level getCurrentLevel()
  {
    return currentLevel;
  }



  // Quick aStar test case.
  // 5x11 room, with wall seperating halfs
  private void aStarTestRoom()
  {
    playerSpawnX = 3;
    playerSpawnY = 2;

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

    player.setLocation(new Point(playerSpawnX * Settings.TILE_SIZE, playerSpawnY * Settings.TILE_SIZE));

    zombieArrayList.add(new ZombieLine(11 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE, (randGen.nextInt(360) + randGen.nextDouble())));
  }
}
