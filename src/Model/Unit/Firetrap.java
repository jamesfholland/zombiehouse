package Model.Unit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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


  public Firetrap(Point location)
  {
    this.location = location;
    this.size = new Dimension(FIRETRAP_IMAGE.getWidth(), FIRETRAP_IMAGE.getHeight());
  }

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

  @Override
  public void update(long deltaTime)
  {

  }

  @Override
  public BufferedImage getImage()
  {
    return FIRETRAP_IMAGE;
  }

}
