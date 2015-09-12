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
  protected boolean isBurned;

  Tile()
  {
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.isBurned = false;


  }
}
