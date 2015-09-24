package Model.HouseGeneration;

/**
 * Simple direction enum
 */
public enum Direction
{
  NORTH (-1, 0 ),
  EAST  ( 0, 1 ),
  SOUTH ( 1, 0 ),
  WEST  ( 0,-1 );

  private final int deltaX;
  private final int deltaY;

  Direction( int x, int y)
  {
    deltaX = x;
    deltaY = y;
  }

  public int getDX() { return deltaX; }

  public int getDY() { return deltaY; }
}
