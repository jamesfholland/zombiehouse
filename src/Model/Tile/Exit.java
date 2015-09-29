package Model.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Exit is a child of Tile that acts as the level end
 * There is a single Exit in each level, that is lit up regardless of map size
 * Does not obstruct vision or zombie movement
 */
public class Exit extends Tile
{
  private final static BufferedImage EXIT_IMAGE;

  static
  {
    BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Exit.class.getResourceAsStream("exit.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    EXIT_IMAGE = imageTemp;
  }

  /**
   * The constructor for the Exit
   *
   * @param location - is the pixel (x,y) cordinates of the exit in the house
   */
  public Exit(Point location)
  {
    super(location); //Sets up shared Tile settings.
  }

  /**
   * Returns the image for Exit, saved in resources
   *
   * @return EXIT_IMAGE
   */
  @Override
  public BufferedImage getImage()
  {
    return EXIT_IMAGE;
  }
}
