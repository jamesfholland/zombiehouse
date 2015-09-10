package Model.Tile;

import Model.GameObject;
import Model.Settings;

import java.awt.*;

public abstract class Tile extends GameObject
{
  /**
   * Used for switching to burned graphics.
   */
  protected boolean isBurned;
  protected boolean isFire;

  Tile()
  {
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.isBurned = false;
    this.isFire = false;


  }
}
