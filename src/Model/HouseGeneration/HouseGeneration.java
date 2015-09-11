package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Zombie.*;
import Model.Settings;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class HouseGeneration is intended to be initialized one time by controller
 * and then is in charge of initializing/re-creating levels
 */
public class HouseGeneration
{
  int houseWidth, houseHeight;
  Tile[][] houseTiles;
  ArrayList<Zombie> zombieArrayList;

  Level currentLevel;

  // held for house generation: on death/respawn, last known seed is used
  //                            on new level, take new seed
  private static long lastRandomSeed;

  public HouseGeneration()
  {
    // "Magic number" 5 is just a place holder to make a small house, rather than
    // the full size house.
    houseWidth = Settings.PRACTICE_MAP_SIZE;
    houseHeight = Settings.PRACTICE_MAP_SIZE;
    houseTiles = new Tile[Settings.PRACTICE_MAP_SIZE][Settings.PRACTICE_MAP_SIZE];
    zombieArrayList = new ArrayList<Zombie>(10);

    lastRandomSeed = System.nanoTime();

    presetHouse();
    presetZombies();

    createLevel();

  }

  private void presetHouse()
  {
    for(int i = 0; i < houseWidth; i++)
    {
      for( int j = 0; j < houseHeight; j++)
      {
        houseTiles[i][j] = new Wall(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }

    for(int i = 1; i < 6; i++)
    {
      houseTiles[i][1] = new Floor(new Point(i * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
      houseTiles[i][3] = new Floor(new Point(i * Settings.TILE_SIZE, 3 * Settings.TILE_SIZE));
      houseTiles[i][5] = new Floor(new Point(i * Settings.TILE_SIZE, 5 * Settings.TILE_SIZE));
    }

    houseTiles[4][2] = new Floor(new Point(5 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));
    houseTiles[5][2] = new Floor(new Point(6 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));
    houseTiles[1][4] = new Floor(new Point(1 * Settings.TILE_SIZE, 4 * Settings.TILE_SIZE));
    houseTiles[2][4] = new Floor(new Point(2 * Settings.TILE_SIZE, 4 * Settings.TILE_SIZE));

    houseTiles[1][1] = new Pillar(new Point(1 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
  }

  private void presetZombies()
  {
    zombieArrayList.add(new ZombieLine(2 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE, 0));
    zombieArrayList.add(new ZombieLine(3 * Settings.TILE_SIZE, 6 * Settings.TILE_SIZE, 0));
  }

  private void createLevel()
  {
    currentLevel = new Level(1, houseTiles, zombieArrayList, 3);
  }

  public Level getCurrentLevel() { return currentLevel; }
}
