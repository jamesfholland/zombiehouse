package Model.Tile;

import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Wall is default tile for at initilization
 * It is unpassable and obscures vision
 * Nothing but fire can occupy the space of a wall tile
 */
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

  /**
   * The constructor for walls
   * @param location - is the pixel (x,y) cordinates of the wall in the house
   */
  public Wall(Point location)
  {
    super(location); //Sets up shared Tile settings.
    this.passable = false;
    this.markedForDeletion = false;
  }

  /**
   * Returns the image for walls, saved in resources
   * @return WALL_IMAGE
   */
  @Override
  public BufferedImage getImage()
  {
    if (burned)
    {
      return WALL_BURNED_IMAGE;
    }

    return WALL_IMAGE;
  }

  /**
   * All tiles have isWall().  Walls are only one to return true
   * @return true
   */
  @Override
  public boolean isWall()
  {
    return true;
  }

  /**
   * markedForDeletion() is used to save if a wall should be deleted
   * for removeHiddenWalls()
   */
  @Override
  public void markForDeletion()
  {
    markedForDeletion = true;
  }

  /**
   * returns the boolean for if a wall should be deleted
   * @return markedForDeletion
   */
  @Override
  public boolean getDeletion()
  {
    return markedForDeletion;
  }
}
