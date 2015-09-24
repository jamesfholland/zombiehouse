package Model.Tile;

import Model.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Wall extends Tile
{
  private final static BufferedImage WALL_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(Wall.class.getResourceAsStream("wood.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    WALL_IMAGE = imageTemp;
  }


  public Wall(Point location)
  {
    super(location); //Sets up shared Tile settings.
    this.passable = false;
  }

  @Override
  public BufferedImage getImage()
  {
    return WALL_IMAGE;
  }

  @Override
  public void collide(GameObject other)
  {

  }

  @Override
  public boolean isWall() { return true; }

  @Override
  public String toString() { return "x"; }
}
