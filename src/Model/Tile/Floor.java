package Model.Tile;

import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Floor is a child Tile
 * it is the walkable space on the maps and does not obstruct vision
 * Players/Zombies/Firetraps/Fire can occupy floor tiles
 * Pillars may overwrite floor tiles after creation
 * Floors (like walls and Pillars) can be burned and their image will change
 */
public class Floor extends Tile
{
  private final static BufferedImage FLOOR_IMAGE;
  private final static BufferedImage FLOOR_BURNED_IMAGE;

  private boolean empty;

  static
  {
    BufferedImage imageFloor = null;
    BufferedImage imageBurn = null;

    try
    {
      imageFloor = ImageIO.read(Pillar.class.getResourceAsStream("hardwood.png"));
      imageBurn = ImageIO.read(Pillar.class.getResourceAsStream("burn.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    Graphics graphics;
    //Basic Floor
    FLOOR_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = FLOOR_IMAGE.getGraphics();
    graphics.drawImage(imageFloor, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    //Burned Floor
    FLOOR_BURNED_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = FLOOR_BURNED_IMAGE.getGraphics();
    graphics.drawImage(imageFloor, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    graphics.drawImage(imageBurn, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
  }

  /**
   * Constuctor for floor tiles
   * at time of initilization all floors are by default empty (though may be filled shortly after)
   * @param location - is the pixel (x,y) cordinates of the floor in the house
   */
  public Floor(Point location)
  {
    super(location); /*Sets up shared Tile settings*/
    empty = true;
  }

  /**
   * Returns the image for Floor, saved in resources
   * @return FloorImage - may be clean or burned
   */
  @Override
  public BufferedImage getImage()
  {
    if (burned)
    {
      return FLOOR_BURNED_IMAGE;
    }

    return FLOOR_IMAGE;
  }

  /**
   * All Tile objects have a isFloor() method.  Only floors return true
   * @return true
   */
  @Override
  public boolean isFloor()
  {
    return true;
  }

  /**
   * Like isFloor, all Tile objects have a isEmptyFloor.  Only empty floors
   * with member variable empty return true
   * @return true - if empty || false if not empty
   */
  @Override
  public boolean isEmptyFloor()
  {
    return empty;
  }

  /**
   * setEmpty is used to change the member variable empty
   * empty is only relevant to map initialization (not for collision/movement)
   * @param empty - boolean
   */
  @Override
  public void setEmpty(boolean empty)
  {
    this.empty = empty;
  }
}
