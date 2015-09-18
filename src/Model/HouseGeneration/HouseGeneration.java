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

  Level currentLevel;

  // held for house generation: on death/respawn, last known seed is used
  //                            on new level, take new seed
  private static long lastRandomSeed;

  public HouseGeneration(Player player)
  {
    houseWidth = Settings.PRACTICE_MAP_SIZE;
    houseHeight = Settings.PRACTICE_MAP_SIZE;
    houseTiles = new Tile[Settings.PRACTICE_MAP_SIZE][Settings.PRACTICE_MAP_SIZE];
    zombieArrayList = new ArrayList<Zombie>(10);

    //player.setLocation(new Point(3 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));

    this.player = player;
    lastRandomSeed = System.nanoTime();
    randGen = new Random(lastRandomSeed);

    presetHouse();
    presetZombies();

    createLevel();

    this.player.setLevel(currentLevel);
  }

  private void presetHouse()
  {
    playerSpawnX = -1;
    playerSpawnY = -1;

    for( int i = 0; i < houseWidth; i++ )
    {
      for( int j = 0; j < houseHeight; j++ )
      {
        houseTiles[i][j] = new Wall(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }

    //makeRoom(1, 1, Settings.PRACTICE_MAP_SIZE - 2, Settings.PRACTICE_MAP_SIZE - 2);
    for( int i = 0; i < Settings.DEFAULT_NUMBER_ROOMS; i++ )
    {
      int xSize = randGen.nextInt(40);
      int ySize = randGen.nextInt(40);
      int xTop = randGen.nextInt(houseWidth - xSize - 2) + 1;
      int yTop = randGen.nextInt(houseHeight - ySize - 2) + 1;
      makeRoom(xTop, yTop, xSize, ySize);
    }

    player.setLocation(new Point(playerSpawnX * Settings.TILE_SIZE, playerSpawnY * Settings.TILE_SIZE));


    //    houseTiles[1][1] = new Pillar(new Point(1 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
    //    houseTiles[10][10] = new Pillar(new Point(10 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));
    //    houseTiles[12][10] = new Pillar(new Point(12 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));


    //houseTiles[0][20] = new Exit(new Point(0 * Settings.TILE_SIZE, 20 * Settings.TILE_SIZE));

  }

  private void makeRoom(int topX, int topY, int sizeX, int sizeY)
  {
    for( int i = 0; i < sizeX; i++ )
    {
      for( int j = 0; j < sizeY; j++ )
      {
        houseTiles[topX + i][topY + j] = new Floor(new Point((topX + i) * Settings.TILE_SIZE, (topY + j) * Settings.TILE_SIZE));
        playerSpawn(topX + i, topY + j);
      }
    }
  }

  private void playerSpawn(int x, int y)
  {
    if( randGen.nextInt(100) < 4 || playerSpawnX < 1 )
    {
      playerSpawnX = x;
      playerSpawnY = y;
    }
  }

  private void presetZombies()
  {
    zombieArrayList.add(new ZombieLine(2 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE, 0));
    zombieArrayList.add(new ZombieLine(3 * Settings.TILE_SIZE, 6 * Settings.TILE_SIZE, 0));
  }

  private void createLevel()
  {
    currentLevel = new Level(1, houseTiles, zombieArrayList, 3, player);
  }

  public Level getCurrentLevel()
  {
    return currentLevel;
  }
}
