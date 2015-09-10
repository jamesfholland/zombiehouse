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
}
