package Model.Unit;

import Model.GameObject;
import Model.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Fire extends Unit
{

  private static final BufferedImage[] FIRE_IGNITE;
  private static final BufferedImage[] FIRE_START;
  private static final BufferedImage[] FIRE_FIRST;
  private static final BufferedImage[] FIRE_SECOND;
  private static final BufferedImage[] FIRE_SMOKE;



  protected static final int SPRITE_ROWS = 8;
  protected static final int SPRITES_PER_ROW = 8;
  protected static final int SPRITE_COUNT = SPRITES_PER_ROW * SPRITE_ROWS;

  protected static final int SPRITE_HORIZONTAL_OFFSET = 0;
  protected static final int SPRITE_VERTICAL_OFFSET = 35;


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
      fireIgnite  = ImageIO.read(Player.class.getResourceAsStream("lighter_flame_01.png"));
      fireStart = ImageIO.read(Player.class.getResourceAsStream("fire_01.png"));
      fireFirst = ImageIO.read(Player.class.getResourceAsStream("fire_01b.png"));
      fireSecond = ImageIO.read(Player.class.getResourceAsStream("fire_01c.png"));
      fireSmoke = ImageIO.read(Player.class.getResourceAsStream("fire_02.png"));


    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_IGNITE = SpriteParser.parseMultiRowSprites(fireIgnite, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_START = SpriteParser.parseMultiRowSprites(fireStart, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_FIRST = SpriteParser.parseMultiRowSprites(fireFirst, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_SECOND = SpriteParser.parseMultiRowSprites(fireSecond, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

    spriteHeight = fireStart.getHeight() / SPRITE_ROWS;
    spriteWidth = fireStart.getWidth() / SPRITES_PER_ROW;
    FIRE_SMOKE = SpriteParser.parseMultiRowSprites(fireSmoke, SPRITE_ROWS, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, SPRITES_PER_ROW);

  }

  public Fire(int tileX, int tileY)
  {
    this.location = new Point(tileX* Settings.TILE_SIZE, tileY*Settings.TILE_SIZE);
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.getHitbox();

    this.isBurning = true;
    this.timeBurning = 0;
  }

  @Override
  public void update(long deltaTime, long secondsFromStart)
  {
    if (isBurning && timeBurning < 15000)
    {
      timeBurning += deltaTime;
    }
    else if (isBurning && timeBurning >= 15000)
    {
      isBurning = false;
      //Set tile under to burned.
    }
  }

  public boolean isBurning()
  {
    return isBurning;
  }

  @Override
  public Clip getSound()
  {
    return null;
  }

  @Override
  public BufferedImage getImage()
  {
    animationState++;
    if(animationState >= SPRITE_COUNT)
    {
      animationState = 0;
    }

    if(timeBurning < 1000)
    {
      return FIRE_IGNITE[animationState];
    }
    else if(timeBurning < 5000)
    {
      return FIRE_START[animationState];
    }
    else if(timeBurning < 9000)
    {
      return FIRE_FIRST[animationState];
    }
    else if(timeBurning < 13000)
    {
      return FIRE_SECOND[animationState];
    }
    else
    {
      return FIRE_SMOKE[animationState];
    }
  }

  @Override
  public void collide(GameObject other)
  {

  }
}
