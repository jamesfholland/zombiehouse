package Model.Unit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Firetraps are objects placed around map at random
 * if a zombie touches the firetrap (or a player runs into one) the trap explodes
 * leaving a 3x3 grid of fire
 */
public class Firetrap extends Unit
{
  private final static BufferedImage FIRETRAP_IMAGE;

  static
  {
    BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Firetrap.class.getResourceAsStream("firetrap.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    FIRETRAP_IMAGE = imageTemp;
  }


  /**
   * constuctor for firetraps
   * @param location - point location, pixel coordinate
   */
  public Firetrap(Point location)
  {
    this.location = location;
    this.size = new Dimension(FIRETRAP_IMAGE.getWidth(), FIRETRAP_IMAGE.getHeight());
  }

  /**
   * if fire trap is set off, it explodes leaving fire
   */
  public void spawnFire()
  {
    Point tile = this.getTileLocation();

    for (int i = tile.x - 1; i <= tile.x + 1; ++i)
    {
      for (int j = tile.y - 1; j <= tile.y + 1; ++j)
      {
        level.FIRES.add(new Fire(i, j));
      }
    }
  }

  /**
   * as a unit, firetrap has an update method, but has nothing to update
   * @param deltaTime the time since last update
   */
  @Override
  public void update(long deltaTime)
  {
  }

  /**
   * returns the image of a fire trap
   * @return img - BufferedImage to be drawn
   */
  @Override
  public BufferedImage getImage()
  {
    return FIRETRAP_IMAGE;
  }

}
