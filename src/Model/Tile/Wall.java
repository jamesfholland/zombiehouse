package Model.Tile;

import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Wall extends Tile
{
  private final static BufferedImage WALL_IMAGE;
  private final static BufferedImage WALL_BURNED_IMAGE;

  static
  {
    BufferedImage imageWall = null;
    BufferedImage imageBurn = null;

    try
    {
      imageWall = ImageIO.read(Pillar.class.getResourceAsStream("wood.png"));
      imageBurn = ImageIO.read(Pillar.class.getResourceAsStream("burn.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    Graphics graphics;
    //Basic Floor
    WALL_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = WALL_IMAGE.getGraphics();
    graphics.drawImage(imageWall, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    //Burned Floor
    WALL_BURNED_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = WALL_BURNED_IMAGE.getGraphics();
    graphics.drawImage(imageWall, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    graphics.drawImage(imageBurn, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
  }

  private boolean markedForDeletion;


  public Wall(Point location)
  {
    super(location); //Sets up shared Tile settings.
    this.passable = false;
    this.markedForDeletion = false;
  }

  @Override
  public BufferedImage getImage()
  {
    if (burned)
    {
      return WALL_BURNED_IMAGE;
    }

    return WALL_IMAGE;
  }
  @Override
  public boolean isWall()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return "x";
  }

  @Override
  public void markForDeletion()
  {
    markedForDeletion = true;
  }

  @Override
  public boolean getDeletion()
  {
    return markedForDeletion;
  }
}
