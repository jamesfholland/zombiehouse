package Model;

import java.util.Random;

/**
 * This contains the settings for the application.
 */
public abstract class Settings
{

  /**
   * Number of rooms to generate.
   */
  public static final int DEFAULT_NUMBER_ROOMS = 6;

  /**
   * Number of Hallways to generate.
   */
  public static final int DEFAULT_NUMBER_HALLS = 4;

  /**
   * Number of random obstacles.
   */
  public static final int DEFAULT_NUMBER_OBSTACLES = 5;

  /**
   * Chance of firetrap in a given floor tile.
   */
  public static final double DEFAULT_FIRETRAP_CHANCE = 0.10; //10%

  /**
   * Default width (tiles)
   */
  public static final int WIDTH_IN_TILES = 35;

  /**
   * Default Height (tiles)
   */
  public static final int HEIGHT_IN_TILES = 18;

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
   * Tiles per Width
   * Width is forced by requirements to be 24 tiles wide on the screen.
   */
  public static final double TILED_WIDTH = WIDTH_STANDARD / (double) TILE_SIZE;


  /**
   * Tiles on practice level - TEMPORARY VARIABLE
   * square size - variable is length of side
   */
  public static final int PRACTICE_MAP_SIZE = 100;

  /**
   * The target refresh rate in milliseconds
   */
  public static final int REFRESH_RATE = 17; //60hz = 16.666666_ but 17 is close enough.

  /**
   * Shared Random number generator.
   * Must be shared across everything or levels may be created differently.
   */
  public static final Random RANDOM = new Random();

}
