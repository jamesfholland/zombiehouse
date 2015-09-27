package Model.Tile;

import Model.GameObject;
import Model.Settings;

import java.awt.*;

public abstract class Tile extends GameObject
{
  /**
   * Used for switching to burned graphics.
   */
  boolean burned;
  boolean passable;

  Tile(Point location)
  {
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.location = location;
    this.burned = false;
    this.passable = true;
  }

  public boolean isPassable()
  {
    return passable;
  }

  public boolean isWall()
  {
    return false;
  }

  public boolean isFloor()
  {
    return false;
  }

  public boolean isEmptyFloor()
  {
    return false;
  }

  public void setEmpty(boolean unused)
  {
  }

  public void markForDeletion()
  {
  }

  public boolean getDeletion()
  {
    return false;
  }

  public void burn()
  {
    this.burned = true;
  }
}
