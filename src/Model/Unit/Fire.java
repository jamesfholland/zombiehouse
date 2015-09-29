package Model.Unit;

import Model.Settings;
import View.SoundManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Fire is spawned from firetraps
 * it lasts for 15 seconds
 * while burning it kills the player or zombies that contact it (except master zombie)
 * after it burns out it will leave burn marks on floor or walls and will 'burn down' pillars
 */
public class Fire extends Unit
{

  private static final BufferedImage[] FIRE_IGNITE;
  private static final BufferedImage[] FIRE_START;
  private static final BufferedImage[] FIRE_FIRST;
  private static final BufferedImage[] FIRE_SECOND;
  private static final BufferedImage[] FIRE_SMOKE;


  private static final int SPRITE_ROWS = 8;
  private static final int SPRITES_PER_ROW = 8;
  private static final int SPRITE_COUNT = SPRITES_PER_ROW * SPRITE_ROWS;

  private static final int SPRITE_HORIZONTAL_OFFSET = 0;
  private static final int SPRITE_VERTICAL_OFFSET = 35;


  private boolean isBurning;
  private int timeBurning;
  private int animationState;

  static
  {
    BufferedImage fireIgnite = null;
    BufferedImage fireStart = null;
    BufferedImage fireFirst = null;
    BufferedImage fireSecond = null;
    BufferedImage fireSmoke = null;

    int spriteHeight;
    int spriteWidth;

    try
    {
      fireIgnite = ImageIO.read(Player.class.getResourceAsStream("lighter_flame_01.png"));
      fireStart = ImageIO.read(Player.class.getResourceAsStream("fire_01.png"));
      fireFirst = ImageIO.read(Player.class.getResourceAsStream("fire_01b.png"));
      fireSecond = ImageIO.read(Player.class.getResourceAsStream("fire_01c.png"));
      fireSmoke = ImageIO.read(Player.class.getResourceAsStream("fire_02.png"));


    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    assert fireIgnite != null;
    spriteHeight = fireIgnite.getHeight() / SPRITE_ROWS;
    spriteWidth = fireIgnite.getWidth() / SPRITES_PER_ROW;
    FIRE_IGNITE = SpriteParser.parseMultiRowSprites(fireIgnite, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    assert fireStart != null;
    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_START = SpriteParser.parseMultiRowSprites(fireStart, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    assert fireFirst != null;
    spriteHeight = fireFirst.getHeight() / SPRITE_ROWS;
    spriteWidth = fireFirst.getWidth() / SPRITES_PER_ROW;
    FIRE_FIRST = SpriteParser.parseMultiRowSprites(fireFirst, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    assert fireSecond != null;
    spriteHeight = fireSecond.getHeight() / SPRITE_ROWS;
    spriteWidth = fireSecond.getWidth() / SPRITES_PER_ROW;
    FIRE_SECOND = SpriteParser.parseMultiRowSprites(fireSecond, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    assert fireSmoke != null;
    spriteHeight = fireSmoke.getHeight() / SPRITE_ROWS;
    spriteWidth = fireSmoke.getWidth() / SPRITES_PER_ROW;
    FIRE_SMOKE = SpriteParser.parseMultiRowSprites(fireSmoke, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

  }

  /**
   * The constructor for fire
   * @param tileX - is the x-index in the 2d tiles array (rather than pixel location)
   * @param tileY - is the y-index in the 2d tiles array (rather than pixel location)
   */
  public Fire(int tileX, int tileY)
  {
    this.location = new Point(tileX * Settings.TILE_SIZE, tileY * Settings.TILE_SIZE);
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);


    this.isBurning = true;
    this.timeBurning = 0;
  }

  /**
   * keeps track of fire lifespan and updates the desired image
   * @param deltaTime the time since last update
   */
  @Override
  public void update(long deltaTime)
  {
    if (isBurning && timeBurning < 15000)
    {
      timeBurning += deltaTime;
      SoundManager.playFire(this.getCenterLocation(), level.PLAYER.getCenterLocation());
    }
    else if (isBurning && timeBurning >= 15000)
    {
      isBurning = false;
      //Set tile under to burned.
    }
  }

  /**
   * returns if fire is burning
   * @return isBurning - boolean
   */
  public boolean isBurning()
  {
    return isBurning;
  }

  /**
   * returns the current image of the fire (based on stage of animation)
   * @return img - BufferedImage to be drawn
   */
  @Override
  public BufferedImage getImage()
  {
    animationState++;
    if (animationState >= SPRITE_COUNT)
    {
      animationState = 0;
    }

    if (timeBurning < 1000)
    {
      return FIRE_IGNITE[animationState];
    }
    else if (timeBurning < 5000)
    {
      return FIRE_START[animationState];
    }
    else if (timeBurning < 9000)
    {
      return FIRE_FIRST[animationState];
    }
    else if (timeBurning < 13000)
    {
      return FIRE_SECOND[animationState];
    }
    else
    {
      return FIRE_SMOKE[animationState];
    }
  }

}
