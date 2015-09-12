package Model.Tile;

import Model.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Floor extends Tile
{
  private final static BufferedImage FLOOR_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Floor.class.getResourceAsStream("floor.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    FLOOR_IMAGE = imageTemp;
  }

  public Floor(Point location)
  {
    super(); //Sets up shared Tile settings.
    this.location = location;
  }

  @Override
  public BufferedImage getImage()
  {
    return FLOOR_IMAGE;
  }

  @Override
  public void collide(GameObject other)
  {

  }

  @Override
  public String toString()
  {
    return ".";
  }
}
