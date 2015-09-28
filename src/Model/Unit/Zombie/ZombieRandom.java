package Model.Unit.Zombie;

import Model.Direction;
import Model.Settings;
import Model.Unit.SpriteParser;
import View.SoundManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ZombieRandom extends Zombie
{
  private static final BufferedImage[] WALK_UP_IMAGE;
  private static final BufferedImage[] WALK_RIGHT_IMAGE;
  private static final BufferedImage[] WALK_LEFT_IMAGE;
  private static final BufferedImage[] WALK_DOWN_IMAGE;

  private boolean collided;

  static
  {
    BufferedImage imageTemp = null;
    int spriteHeight;
    int spriteWidth;

    try
    {
      imageTemp = ImageIO.read(ZombieRandom.class.getResourceAsStream("zombieRandom.png"));
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
  public ZombieRandom(int x, int y, double heading)
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
    lastDecision += deltaTime;
    if (lastDecision >= Settings.zombieDecisionRate)
    {
      lastDecision = 0;
      canSmellPlayer();

      if (knowsPlayerLocation)
      {
        collided = false;
        heading = level.ASTAR.getHeading(getCenterLocation());
        setHeadingVector();
      } else
      {
        makeDecision();
        setHeadingVector();
      }
    }

    move(Settings.zombieSpeed, heading, deltaTime);

    direction = null;
    if (headingVector.y > 0)
    {
      direction = Direction.SOUTH;
    } else if (headingVector.y < 0)
    {
      direction = Direction.NORTH;
    } else if (headingVector.x > 0)
    {
      direction = Direction.EAST;
    } else if (headingVector.x < 0)
    {
      direction = Direction.WEST;
    }

    if (direction != null)
    {
      if(this.collided) SoundManager.playZombieThud(this.getCenterLocation(), level.PLAYER.getCenterLocation());
      SoundManager.playZombieWalk(this.getCenterLocation(), level.PLAYER.getCenterLocation());
      spriteState++;
      if (spriteState >= WALK_SPRITE_COUNT)
      {
        spriteState = 0;
      }
    }
  }

  private void makeDecision()
  {
    if (collided)
    {
      heading = (heading + 180) % 360;
      collided = false;
    } else
    {
      heading = (Settings.RANDOM.nextInt(360) + Settings.RANDOM.nextDouble());
    }
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
