package Model.HouseGeneration;

import Model.Level;
import Model.Settings;
import Model.Tile.*;
import Model.Unit.Player;
import Model.Unit.Zombie.*;
import Model.Settings;

import java.awt.*;
import java.util.ArrayList;
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

  Level currentLevel;

  // held for house generation: on death/respawn, last known seed is used
  //                            on new level, take new seed
  private static long lastRandomSeed;

  public HouseGeneration(Player player)
  {
    // "Magic number" 5 is just a place holder to make a small house, rather than
    // the full size house.
    houseWidth = Settings.PRACTICE_MAP_SIZE;
    houseHeight = Settings.PRACTICE_MAP_SIZE;
    houseTiles = new Tile[Settings.PRACTICE_MAP_SIZE][Settings.PRACTICE_MAP_SIZE];
    zombieArrayList = new ArrayList<Zombie>(10);
    player.setLocation(new Point(3 * Settings.TILE_SIZE, 2 * Settings.TILE_SIZE));
    this.player = player;
    lastRandomSeed = System.nanoTime();

    presetHouse();
    presetZombies();

    createLevel();

    this.player.setLevel(currentLevel);
  }

  private void presetHouse()
  {
    for(int i = 1; i < Settings.PRACTICE_MAP_SIZE; i++)
    {
      for( int j = 1; j < Settings.PRACTICE_MAP_SIZE; j++)
      {
        houseTiles[i][j] = new Floor(new Point(i * Settings.TILE_SIZE, j * Settings.TILE_SIZE));
      }
    }

    for(int i = 0; i < Settings.PRACTICE_MAP_SIZE; i++)
    {
      int farWall = Settings.PRACTICE_MAP_SIZE -1;
      //top wall
      houseTiles[i][0] = new Wall(new Point(i * Settings.TILE_SIZE, 0 * Settings.TILE_SIZE));
      //Bottom wall
      houseTiles[i][farWall] = new Wall(new Point(i * Settings.TILE_SIZE, farWall * Settings.TILE_SIZE));
      //left wall
      houseTiles[0][i] = new Wall(new Point(0 * Settings.TILE_SIZE, i * Settings.TILE_SIZE));
      //right wall
      houseTiles[farWall][i] = new Wall(new Point(farWall * Settings.TILE_SIZE, i * Settings.TILE_SIZE));
    }

    houseTiles[1][1] = new Pillar(new Point(1 * Settings.TILE_SIZE, 1 * Settings.TILE_SIZE));
    houseTiles[10][10] = new Pillar(new Point(10 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));
    houseTiles[12][10] = new Pillar(new Point(12 * Settings.TILE_SIZE, 10 * Settings.TILE_SIZE));


    //houseTiles[0][20] = new Exit(new Point(0 * Settings.TILE_SIZE, 20 * Settings.TILE_SIZE));

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

  public Level getCurrentLevel() { return currentLevel; }
}
