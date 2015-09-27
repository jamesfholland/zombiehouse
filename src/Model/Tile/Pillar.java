package Model.Tile;

import Model.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Pillar is a derivative of floor and wall
 */
public class Pillar extends Tile
{
  private final static BufferedImage TABLE_IMAGE;
  private final static BufferedImage PLANT_IMAGE;
  private final static BufferedImage BURN_IMAGE;

  private PillarType PILLAR_TYPE;

  static
  {
    BufferedImage imageTemp = null;
    BufferedImage imageFloor = null;
    BufferedImage imageBurn = null;

    try
    {
      imageTemp = ImageIO.read(Pillar.class.getResourceAsStream("pillar.png"));
      imageFloor = ImageIO.read(Pillar.class.getResourceAsStream("hardwood.png"));
      imageBurn = ImageIO.read(Pillar.class.getResourceAsStream("burn.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    assert imageTemp != null;
    //Table type
    TABLE_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = TABLE_IMAGE.getGraphics();
    graphics.drawImage(imageFloor, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    graphics.drawImage(imageTemp.getSubimage(136, 176, 46, 79), 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);

    //Plant type
    PLANT_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = PLANT_IMAGE.getGraphics();
    graphics.drawImage(imageFloor, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    graphics.drawImage(imageTemp.getSubimage(225, 224, 29, 51), 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);

    //Burn type
    BURN_IMAGE = new BufferedImage(Settings.TILE_SIZE, Settings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    graphics = BURN_IMAGE.getGraphics();
    graphics.drawImage(imageFloor, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
    graphics.drawImage(imageBurn, 0, 0, Settings.TILE_SIZE, Settings.TILE_SIZE, null);
  }

  public Pillar(Point location)
  {
    super(location); //Sets up shared Tile settings.
    this.passable = false;
    if (Settings.RANDOM.nextBoolean())
    {
      this.PILLAR_TYPE = PillarType.PLANT;
    } else
    {
      this.PILLAR_TYPE = PillarType.TABLE;
    }

  }

  @Override
  public BufferedImage getImage()
  {
    switch (this.PILLAR_TYPE)
    {
      case PLANT:
        return PLANT_IMAGE;
      case TABLE:
        return TABLE_IMAGE;
      case BURN:
        return BURN_IMAGE;
    }
    return BURN_IMAGE;
  }

  @Override
  public void burn()
  {
    super.burn();
    this.passable = true;
    this.PILLAR_TYPE = PillarType.BURN;
  }

  @Override
  public String toString()
  {
    return "I";
  }

  //Types of pillars
  private enum PillarType
  {
    PLANT, TABLE, BURN
  }
}
