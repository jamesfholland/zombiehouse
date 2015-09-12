package Model.Tile;

import Model.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Pillar is a derivative of floor and wall
 */
public class Pillar extends Tile
{
  private final static BufferedImage PILLAR_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Pillar.class.getResourceAsStream("pillar.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    PILLAR_IMAGE = imageTemp;
  }

  public Pillar(Point location)
  {
    super(); //Sets up shared Tile settings.
    this.location = location;
  }

  @Override
  public BufferedImage getImage()
  {
    return PILLAR_IMAGE;
  }

  @Override
  public void collide(GameObject other)
  {

  }

  @Override
  public String toString()
  {
    return "I";
  }
}
