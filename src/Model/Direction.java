package Model;

/**
 * A enum for the cardinal directions NORTH, EAST, SOUTH, WEST
 * has built in change in x, change in y values associated with the direction
 */
public enum Direction
{
  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  private final int deltaX;
  private final int deltaY;

  /**
   * Constuctor for the enums
   * @param x - change in X
   * @param y - change in Y
   */
  Direction(int x, int y)
  {
    deltaX = x;
    deltaY = y;
  }

  /**
   * returns the change in X associated with the direction
   * @return deltaX - int
   */
  public int getDX()
  {
    return deltaX;
  }

  /**
   * returns the change in Y associated with the direction
   * @return deltaY - int
   */
  public int getDY()
  {
    return deltaY;
  }

  /**
   * returns the direction that is 'opposite' (ie: north returns south)
   * @return dir - Direction Enum
   */
  public Direction inverseDir()
  {
    if (deltaY == -1)
    {
      return Direction.SOUTH;
    }
    if (deltaX == 1)
    {
      return Direction.WEST;
    }
    if (deltaY == 1)
    {
      return Direction.NORTH;
    }
    return Direction.EAST;
  }

}
