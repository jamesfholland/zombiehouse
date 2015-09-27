package Model;

/**
 *
 */
public enum Direction
{
  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  private final int deltaX;
  private final int deltaY;

  Direction(int x, int y)
  {
    deltaX = x;
    deltaY = y;
  }

  public int getDX()
  {
    return deltaX;
  }

  public int getDY()
  {
    return deltaY;
  }

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
