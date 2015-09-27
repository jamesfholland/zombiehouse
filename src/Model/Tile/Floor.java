package Model.Tile;

import Model.GameObject;
import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Floor extends Tile
{
  private final static BufferedImage FLOOR_IMAGE;
  private final static BufferedImage FLOOR_BURNED_IMAGE;

  // floor tiles are created either in rooms or hallways
  // by numbering the floor tiles, we can assess which room a floor is part of
  // 0 = hallway - special rule - hallways may not have obstructions
  // 1+ = rooms
  private int roomNum;
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

  public Floor(Point location)
  {
    super(location); /*Sets up shared Tile settings*/
    empty = true;
  }

  public Floor(Point location, int roomNum)
  {
    super(location); //Sets up shared Tile settings.
    this.roomNum = roomNum;
    empty = true;
  }

  @Override
  public BufferedImage getImage()
  {
    if(burned) return FLOOR_BURNED_IMAGE;

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

  @Override
  public boolean isFloor() { return true; }

  @Override
  public boolean isEmptyFloor() { return empty; }

  @Override
  public void setEmpty(boolean empty) { this.empty = empty; }

  public int getRoomNum() { return roomNum; }

  @Override
  public boolean isWall() { return false; }
}
