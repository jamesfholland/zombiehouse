package Model;

/**
 * This contains the settings for the application.
 */
public interface Settings
{

  /**
   * Number of rooms to generate.
   */
  int DEFAULT_NUMBER_ROOMS = 6;

  /**
   * Number of Hallways to generate.
   */
  int DEFAULT_NUMBER_HALLS = 4;

  /**
   * Number of random obstacles.
   */
  int DEFAULT_NUMBER_OBSTACLES = 5;

  /**
   * Chance of firetrap in a given floor tile.
   */
  double DEFAULT_FIRETRAP_CHANCE = 0.10; //10%

  /**
   * Default width (tiles)
   */
  int WIDTH_IN_TILES = 35;

  /**
   * Default Height (tiles)
   */
  int HEIGHT_IN_TILES = 18;

  /**
   * Pixel Height Standard
   */
  int HEIGHT_STANDARD = 1080;

  /**
   * Pixel Width Standard
   */
  int WIDTH_STANDARD = 1920;

  /**
   * Tile Size (pixels)
   */
  int TILE_SIZE = 80;

  /**
   * Tiles per Width
   * Width is forced by requirements to be 24 tiles wide on the screen.
   */
  double TILED_WIDTH = WIDTH_STANDARD / (double) TILE_SIZE;


  /**
   * Tiles on practice level - TEMPORARY VARIABLE
   * square size - variable is length of side
   */

  final int PRACTICE_MAP_SIZE = 7;



}
