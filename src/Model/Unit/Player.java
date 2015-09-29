package Model.Unit;

import Model.Direction;
import Model.Settings;
import View.SoundManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Player class holds the functions and member variables necessary to move/keep track of all the player actions.
 *
 * The player is held in the data class level which also holds all the other game objects and units.
 *
 * On a program level, Player is part of the model and has some peripheral interactions with the controller.
 * On a class level, most of Player's interactions are with other GameObjects and calculated within the model. The controller reads in the user input
 * and tells Player where to go.
 */
public class Player extends Unit
{

  private static final BufferedImage[] WALK_UP_IMAGE;
  private static final BufferedImage[] WALK_RIGHT_IMAGE;
  private static final BufferedImage[] WALK_LEFT_IMAGE;
  private static final BufferedImage[] WALK_DOWN_IMAGE;

  private Point inputVector;

  private boolean running;
  private double stamina;

  private boolean gettingFireTrap;
  private boolean placingFireTrap;

  private double fireTrapTime;

  private Firetrap firetrap;

  static
  {
    BufferedImage imageTemp = null;
    int spriteHeight;
    int spriteWidth;

    try
    {
      imageTemp = ImageIO.read(Player.class.getResourceAsStream("heroSprites.png"));
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
   * Constuctor for Player
   * A new player is created for every level
   * @param location a point location. Which x,y pixel to make the player at on the map
   */
  public Player(Point location)
  {
    this.location = location;

    this.speed = Settings.walkSpeed;
    this.running = false;
    this.size = Settings.PLAYER_SIZE;
    this.stamina = Settings.playerStamina;

    this.nextHitbox = new Rectangle(location, size);
    this.headingVector = new Point(0, 0);
    this.inputVector = new Point(0, 0);
  }

  /**
   * sets the PLAYER's heading based on what direction was inputted on the keyboard
   *
   * @param p direction the of the PLAYER's input
   */
  private void setHeading(Point p)
  {
    if (p.x == 0 & p.y == 0)
    {
      heading = -1;
    } else if (p.x == 1 && p.y == 0)
    {
      heading = 0.0;
    } else if (p.x == 1 && p.y == 1)
    {
      heading = 45.0;
    } else if (p.x == 0 && p.y == 1)
    {
      heading = 90.0;
    } else if (p.x == -1 && p.y == 1)
    {
      heading = 135.0;
    } else if (p.x == -1 && p.y == 0)
    {
      heading = 180.0;
    } else if (p.x == -1 && p.y == -1)
    {
      heading = 225.0;
    } else if (p.x == 0 && p.y == -1)
    {
      heading = 270.0;
    } else if (p.x == 1 && p.y == -1)
    {
      heading = 315.0;
    }
  }

  /**
   * called by the controller to pass the PLAYER the keyboard input
   *
   * @param p the vector direction where the user is trying to move to e.g 1,0 for east
   */
  public void setInputVector(Point p)
  {
    inputVector.setLocation(p);
  }

  public void setSpeedRun()
  {
    if (stamina > 0)
    {
      speed = Settings.runSpeed;
      running = true;
    }
  }

  /**
   * Returns if the player is set to running or not
   * @return running - boolean
   */
  public boolean isRunning()
  {
    return running;
  }

  /**
   * Sets the player walk speed (based on the value saved in Settings)
   * and turns off running
   */
  public void setSpeedWalk()
  {
    speed = Settings.walkSpeed;
    running = false;
  }

  /**
   * used for player to place a fire trap from inventory on to map
   */
  public void placeFireTrap()
  {
    placingFireTrap = true;
    fireTrapTime = 0.0;
  }

  /**
   * used to pick up a fire trap from the map and take into inventory
   * @param firetrap - is the firetrap object currently being picked up
   */
  public void pickUpFireTrap(Firetrap firetrap)
  {
    gettingFireTrap = true;
    this.firetrap = firetrap;
    fireTrapTime = 0.0;
  }

  /**
   * returns if the player is activly picking up or setting a firetrap
   * (if so the player can't move)
   * @return pickup || placing - booleans
   */
  public boolean pickingOrPlacing()
  {
    return (placingFireTrap || gettingFireTrap);
  }


  /**
   * updates the player character
   * counts the time associated with placing/picking up traps
   * and changes heading/position on map
   * @param deltaTime the time since last update
   */
  @Override
  public void update(long deltaTime)
  {
    //if getting a fire trap and still picking up the firetrap, don't move the character
    if (gettingFireTrap && fireTrapTime < 5000)
    {
      fireTrapTime += deltaTime;
      return;
    }

    //if player has spent 5 secs getting a fire trap, remove firetrap from the level and add one to the fireTrapCount
    else if (gettingFireTrap && fireTrapTime >= 5000)
    {
      fireTrapTime = 0;
      gettingFireTrap = false;
      ++level.fireTrapCount;
      level.FIRETRAPS.remove(firetrap);
      this.firetrap = null;
    }

    //if player is placing a firetrap and still less than 5 secs, don't move the character
    if (!gettingFireTrap && placingFireTrap && fireTrapTime < 5000)
    {
      fireTrapTime += deltaTime;
      return;
    }

    //if player has spent 5 secs placing a firetrap, add a firetrap to the tile at the players center location
    else if (!gettingFireTrap && placingFireTrap && fireTrapTime >= 5000)
    {
      int playerCenterTileX = getCenterLocation().x / Settings.TILE_SIZE;
      int playerCenterTileY = getCenterLocation().y / Settings.TILE_SIZE;

      fireTrapTime = 0;
      placingFireTrap = false;
      --level.fireTrapCount;

      Point p = new Point(playerCenterTileX * Settings.TILE_SIZE, playerCenterTileY * Settings.TILE_SIZE);

      level.FIRETRAPS.add(new Firetrap(p));
    }

    //get what direction the player is moving from the controller
    setHeading(inputVector);

    //play walk sound if moving
    if (inputVector.x != 0 || inputVector.y != 0)
    {
      SoundManager.playWalk(isRunning());
    } else
    {
      SoundManager.stopWalk();
    }

    //do stamina logic here
    if (!running)
    {
      if (stamina < Settings.playerStamina)
      {
        stamina += (deltaTime * Settings.playerRegen);
      }
    }

    if (stamina == 0)
    {
      speed = Settings.walkSpeed;
      running = false;
    }

    if (running)
    {
      stamina = Math.max(stamina - (int) deltaTime, 0);
    }

    //move the character according to his speed, heading, and deltatime
    move(speed, heading, deltaTime);

    direction = null;
    if (inputVector.y > 0)
    {
      direction = Direction.SOUTH;
    } else if (inputVector.y < 0)
    {
      direction = Direction.NORTH;
    } else if (inputVector.x > 0)
    {
      direction = Direction.EAST;
    } else if (inputVector.x < 0)
    {
      direction = Direction.WEST;
    }

    if (direction != null)
    {
      spriteState++;
      if (spriteState >= WALK_SPRITE_COUNT)
      {
        spriteState = 0;
      }
    }
  }

  /**
   * returns the current image for the player (based on stage of animation)
   * @return img - BufferedImage to be drawn
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

  /**
   * returns the amount of stamina the player currently has
   * @return stamina - double value
   */
  public double getStamina()
  {
    return stamina;
  }
}
