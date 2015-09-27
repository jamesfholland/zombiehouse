package Model.Unit;

import Model.Direction;
import Model.Settings;
import View.SoundManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
   * @param p
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

  public boolean isRunning()
  {
    return running;
  }

  public void setSpeedWalk()
  {
    speed = Settings.walkSpeed;
    running = false;
  }

  public void placeFireTrap()
  {
    placingFireTrap = true;
    fireTrapTime = 0.0;
  }

  public void pickUpFireTrap(Firetrap firetrap)
  {
    gettingFireTrap = true;
    this.firetrap = firetrap;
    fireTrapTime = 0.0;
  }

  public boolean pickingOrPlacing()
  {
    return (placingFireTrap || gettingFireTrap);
  }


  @Override
  public void update(long deltaTime)
  {
    if (gettingFireTrap && fireTrapTime < 5000)
    {
      fireTrapTime += deltaTime;
      return;
    } else if (gettingFireTrap && fireTrapTime >= 5000)
    {
      fireTrapTime = 0;
      gettingFireTrap = false;
      ++level.fireTrapCount;
      level.FIRETRAPS.remove(firetrap);
      this.firetrap = null;
    }

    if (!gettingFireTrap && placingFireTrap && fireTrapTime < 5000)
    {
      fireTrapTime += deltaTime;
      return;
    } else if (!gettingFireTrap && placingFireTrap && fireTrapTime >= 5000)
    {
      fireTrapTime = 0;
      placingFireTrap = false;
      --level.fireTrapCount;
      int playerCenterTileX = getCenterLocation().x / Settings.TILE_SIZE;
      int playerCenterTileY = getCenterLocation().y / Settings.TILE_SIZE;

      Point p = new Point(playerCenterTileX * Settings.TILE_SIZE, playerCenterTileY * Settings.TILE_SIZE);

      level.FIRETRAPS.add(new Firetrap(p));
    }

    setHeading(inputVector);

    if (inputVector.x != 0 || inputVector.y != 0)
    {
      SoundManager.playWalk(isRunning());
    } else
    {
      SoundManager.stopWalk();
    }

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

    move(speed, heading, deltaTime);

    direction = null;
    if (inputVector.y > 0)
    {
      direction = Direction.DOWN;
    } else if (inputVector.y < 0)
    {
      direction = Direction.UP;
    } else if (inputVector.x > 0)
    {
      direction = Direction.RIGHT;
    } else if (inputVector.x < 0)
    {
      direction = Direction.LEFT;
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

  @Override
  public BufferedImage getImage()
  {
    if (direction == null)
    {
      return WALK_DOWN_IMAGE[0];
    }

    switch (direction)
    {
      case UP:
        return WALK_UP_IMAGE[spriteState];
      case DOWN:
        return WALK_DOWN_IMAGE[spriteState];
      case LEFT:
        return WALK_LEFT_IMAGE[spriteState];
      case RIGHT:
        return WALK_RIGHT_IMAGE[spriteState];
      default:
        return WALK_DOWN_IMAGE[0];
    }
  }

  public double getStamina()
  {
    return stamina;
  }

}
