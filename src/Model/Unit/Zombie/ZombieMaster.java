package Model.Unit.Zombie;

import Model.Direction;
import Model.Settings;
import Model.Unit.SpriteParser;
import View.SoundManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ZombieMaster extends ZombieRandom
{
  private static final BufferedImage[] WALK_UP_IMAGE;
  private static final BufferedImage[] WALK_RIGHT_IMAGE;
  private static final BufferedImage[] WALK_LEFT_IMAGE;
  private static final BufferedImage[] WALK_DOWN_IMAGE;

  private boolean anOtherZombieKnows;

  static
  {
    BufferedImage imageTemp = null;
    int spriteHeight;
    int spriteWidth;

    try
    {
      imageTemp = ImageIO.read(ZombieRandom.class.getResourceAsStream("zombieMaster.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    assert imageTemp != null;
    spriteHeight = imageTemp.getHeight() / SPRITES_ROWS;
    spriteWidth = imageTemp.getWidth() / SPRITES_PER_ROW;

    int spriteRow = WALK_SPRITE_ROW;
    WALK_UP_IMAGE = SpriteParser.parseSprites(imageTemp, spriteRow, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, WALK_SPRITE_COUNT);
    spriteRow++; //Sprites rows are always in this order.
    WALK_LEFT_IMAGE = SpriteParser.parseSprites(imageTemp, spriteRow, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, WALK_SPRITE_COUNT);
    spriteRow++; //Sprites rows are always in this order.
    WALK_DOWN_IMAGE = SpriteParser.parseSprites(imageTemp, spriteRow, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, WALK_SPRITE_COUNT);
    spriteRow++; //Sprites rows are always in this order.
    WALK_RIGHT_IMAGE = SpriteParser.parseSprites(imageTemp, spriteRow, spriteHeight, spriteWidth, SPRITE_HORIZONTAL_OFFSET, SPRITE_VERTICAL_OFFSET, WALK_SPRITE_COUNT);
  }

  /**
   * Zombie constructor
   * @param x - pixel x cooridnate
   * @param y - pixel y coordinate
   * @param heading - 360 degree double
   */
  public ZombieMaster(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  /**
   * Zombie update() changes the zombie's position as well as checks for a new heading
   * @param deltaTime - millisecond time since last update call
   */
  @Override
  public void update(long deltaTime)
  {
    if (anOtherZombieKnows)
    {
      heading = level.ASTAR.getHeading(getCenterLocation());
      super.move(Settings.zombieSpeed , heading, deltaTime);
    }
    else
    {
      super.update(deltaTime);
    }
  }

  public void setAnOtherZombieKnowsTrue()
  {
    anOtherZombieKnows = true;
  }

  public void setAnOtherZombieKnowsFalse()
  {
    anOtherZombieKnows = false;
  }

  @Override
  public BufferedImage getImage()
  {
    if (direction == null)
    {
      return WALK_DOWN_IMAGE[0];
    }

    switch (direction)
    {
      case NORTH:
        return WALK_UP_IMAGE[spriteState];
      case SOUTH:
        return WALK_DOWN_IMAGE[spriteState];
      case WEST:
        return WALK_LEFT_IMAGE[spriteState];
      case EAST:
        return WALK_RIGHT_IMAGE[spriteState];
      default:
        return WALK_DOWN_IMAGE[0];
    }
  }

}
