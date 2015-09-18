package Model.Tile;

import Model.GameObject;
import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Tile extends GameObject
{
  /**
   * Used for switching to burned graphics.
   */
  protected boolean burned;
  protected boolean passable;

  Tile(Point location)
  {
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.location = location;
    this.hitbox = new Rectangle(location, size);
    this.burned = false;
    this.passable = true;
  }

  public boolean isPassable()
  {
    return passable;
  }
}
