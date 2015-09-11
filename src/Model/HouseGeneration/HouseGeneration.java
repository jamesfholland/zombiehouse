package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Zombie.*;

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
    houseWidth = 5;
    houseHeight = 5;
    houseTiles = new Tile[5][5];
    zombieArrayList = new ArrayList<Zombie>(10);

    lastRandomSeed = System.nanoTime();

    presetHouse5by5();
    presetZombies();

    createLevel();

  }

  private void presetHouse5by5()
  {
    for(int i = 0; i < houseWidth; i++)
    {
      for( int j = 0; j < houseHeight; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }

    for(int i = 0; i < 3; i++)
    {
      houseTiles[i][1] = new Wall(new Point(i * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
      houseTiles[i + 2][3] = new Wall(new Point((i + 2) * Settings.TILE_SIZE, 3 * Settings.TILE_SIZE));
    }

    houseTiles[0][0] = new Floor(new Point(0 * Settings.TILE_SIZE, 0 * Settings.TILE_SIZE), true);
  }

  private void presetZombies()
  {
    zombieArrayList.add(new ZombieLine(1, 0, 0));
  }

  private void createLevel()
  {
    currentLevel = new Level(1, houseTiles, zombieArrayList, 3);
  }

  public Level getCurrentLevel() { return currentLevel; }



}
