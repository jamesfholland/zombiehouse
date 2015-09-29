package Model.Tile;

import Model.GameObject;
import Model.Settings;

import java.awt.*;

/**
 * Abstract class Tile is the parent of all tile objects (Exit, Floor, Pillar, Wall)
 * holds the common variables and methods used by all tiles such as if the tile is burned or passable
 *
 * On a program perspective, the level data class holds an array of tiles which represents the map.
 * Other members of the model check their location relative to this tile array and other members of the model
 */
public abstract class Tile extends GameObject
{
  /**
   * Used for switching to burned graphics.
   */
  boolean burned;
  boolean passable;

  /**
   * Is the super constructor for all tiles.
   * @param location - is the pixel location of the tile on the map
   */
  Tile(Point location)
  {
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.location = location;
    this.burned = false;
    this.passable = true;
  }

  /**
   * Returns the boolean of wether a tile is passable / can be walked through
   * @return passable - boolean
   */
  public boolean isPassable()
  {
    return passable;
  }

  /**
   * Returns if tile is a wall.  Default false for all tiles, but Wall overwrites this method
   * @return false
   */
  public boolean isWall()
  {
    return false;
  }

  /**
   * Returns if tile is a floor.  Default false for all tiles, but Floor overwrites this method
   * @return false
   */
  public boolean isFloor()
  {
    return false;
  }

  /**
   * Returns if tile is an empty floor.  Default false for all tiles, but Floor overwrites this method
   * @return false
   */
  public boolean isEmptyFloor()
  {
    return false;
  }

  /**
   * Sets if tile is empty.  Overwritten by Floor
   */
  public void setEmpty(boolean unused)
  {
  }

  /**
   * Sets if tile should be deleted..  Overwritten by Wall
   */
  public void markForDeletion()
  {
  }

  /**
   * Returns if tile has been marked for deletion.  Overwritten by Wall
   * @return false
   */
  public boolean getDeletion()
  {
    return false;
  }

  /**
   * burn() is used by all tiles and is called when set tile is set on fire
   */
  public void burn()
  {
    this.burned = true;
  }
}
