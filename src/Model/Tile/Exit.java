package Model.Tile;

import Model.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Exit extends Tile
{
  private final static BufferedImage EXIT_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Exit.class.getResourceAsStream("playerFront.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    EXIT_IMAGE = imageTemp;
  }

  public Exit(Point location)
  {
    super(); //Sets up shared Tile settings.
    this.location = location;
  }

  @Override
  public BufferedImage getImage()
  {
    return EXIT_IMAGE;
  }

  @Override
  public void collide(GameObject other)
  {

  }

  @Override
  public String toString() { return "f"; }
}
