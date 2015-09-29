package Model.Unit.Zombie;

import Model.Settings;
import Model.Unit.SpriteParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ZombieMaster is the class which is instantiated when a master zombie is added to the game
 * If a ZombieMaster cannot smell the player and another zombie cannot smell the player then the ZombieMaster behaves like a random zombie.
 * If a ZombieMaster can smell the player then it knows the quickest route to the player.
 * If a ZombieMaster cannot smell the player but an other zombie can smell the player, the the ZombieMaster knows the quickest route to the player.
 * The master zombie is immune to fire and starts at the exit
 * <p>
 * From the program perspective, ZombieMaster is added once to the level in HouseGeneration.
 * The controller updates the data in the level on each update cycle. The view draws the ZombieMaster based on the data held in the level.
 */
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
   *
   * @param x       - pixel x cooridnate
   * @param y       - pixel y coordinate
   * @param heading - 360 degree double
   */
  public ZombieMaster(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  /**
   * Zombie update() changes the zombie's position as well as checks for a new heading
   *
   * @param deltaTime - millisecond time since last update call
   */
  @Override
  public void update(long deltaTime)
  {
    if (anOtherZombieKnows) //if another zombie knows where the player is
    {
      heading = level.ASTAR.getHeading(getCenterLocation()); //calcuate the quickest path to the player
      move(Settings.zombieSpeed, heading, deltaTime); //move
    }
    else
    {
      super.update(deltaTime);
    }
  }

  /**
   * when any other zombie on map knows player location, master zombie knows too
   * used to turn on member variable anOtherZombieKnows
   */
  public void setAnOtherZombieKnowsTrue()
  {
    anOtherZombieKnows = true;
  }

  /**
   * returns the current image of the zombie (based on stage of animation)
   *
   * @return img - BufferedImage to be drawn
   * <p>
   * If no other zombie knows where the player is set the player to false
   */
  public void setAnOtherZombieKnowsFalse()
  {
    anOtherZombieKnows = false;
  }

  /**
   * returns the appropriate sprite based on the direction the zombie is walking and the moment of animation
   *
   * @return BufferedImage of the sprite
   */
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
