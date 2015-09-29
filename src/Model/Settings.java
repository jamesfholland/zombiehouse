package Model;

import java.awt.*;
import java.util.Random;

/**
 * This contains the settings for the application.
 * These are only set either finally or by the Settings Dialog.
 */
public abstract class Settings
{

  /**
   * Number of rooms to generate.
   */
  public static final int DEFAULT_NUMBER_ROOMS = 20;

  /**
   * Number of Hallways to generate.
   */
  public static final int DEFAULT_NUMBER_HALLS = 4;

  /**
   * Number of random obstacles.
   */
  public static final int DEFAULT_NUMBER_OBSTACLES = 5;

  /**
   * Chance of obstacle spawn rate
   */
  public static final double OBSTACLE_SPAWN_RATE = 0.015;

  /**
   * Pixel Height Standard
   */
  public static final int HEIGHT_STANDARD = 1080;

  /**
   * Pixel Width Standard
   */
  public static final int WIDTH_STANDARD = 1920;

  /**
   * Tile Size (pixels)
   */
  public static final int TILE_SIZE = 80;

  /**
   * Tiles on practice level - TEMPORARY VARIABLE
   * square size - variable is length of side
   */
  public static final int PRACTICE_MAP_SIZE = 40;

  /**
   * The target refresh rate in milliseconds
   */
  public static final int REFRESH_RATE = 17; //60hz = 16.666666_ but 17 is close enough.

  /**
   * Shared Random number generator.
   * Must be shared across everything or levels may be created differently.
   */
  public static final Random RANDOM = new Random();

  /**
   * Zombie pixel size
   */
  public static final Dimension ZOMBIE_SIZE = new Dimension(50, 70);

  /**
   * Player pixel size
   */
  public static final Dimension PLAYER_SIZE = new Dimension(45, 60);

  /**
   * Player sight range in pixels
   */
  public static double sightRange = 5 * TILE_SIZE;

  /**
   * How far player can hear
   */
  public static double playerHearing = 10.0 * TILE_SIZE;

  /**
   * Player walk speed in pixels per second
   */
  public static double walkSpeed = Settings.TILE_SIZE / 1000.0;

  /**
   * Player run speed in pixels per second
   */
  public static double runSpeed = walkSpeed * 2;

  /**
   * Player stamina for running
   */
  public static double playerStamina = 5000;

  /**
   * Player stamina regen
   */
  public static double playerRegen = 0.2;

  /**
   * Zombie speed in pixels per second
   */
  public static double zombieSpeed = Settings.TILE_SIZE / 2000.0;

  /**
   * When the zombies make a decision
   */
  public static double zombieDecisionRate = 2000;

  /**
   * Zombie smell distance
   */
  public static double zombieSmell = 7.0 * TILE_SIZE;

  /**
   * Chance of firetrap in a given floor tile.
   */
  public static double firetrapSpawnRate = 0.01;


  /**
   * Chance of zombie spawn in a given floor tile
   */
  public static double zombieSpawnRate = 0.01;

}
