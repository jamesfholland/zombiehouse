package Model.Unit;

import Model.Direction;
import Model.GameObject;
import Model.Level;
import Model.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Unit
{

  private static final BufferedImage[] WALK_UP_IMAGE;
  private static final BufferedImage[] WALK_RIGHT_IMAGE;
  private static final BufferedImage[] WALK_LEFT_IMAGE;
  private static final BufferedImage[] WALK_DOWN_IMAGE;
  private static final double SQRT2 = Math.sqrt(2);

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

  public Player(Point location, Level level)
  {
    this.location = location;
    this.level = level;
    //this.speed = Settings.TILE_SIZE/1000.0;

    this.speed = Settings.TILE_SIZE / 60.0;
    this.size = Settings.PLAYER_SIZE;
    this.hitbox = new Rectangle(location, size);
    this.nextHitbox = new Rectangle(location, size);
    this.vectorToMove = new Point(0, 0);

  }

  public void move(Point p, long deltaTime)
  {
    double newLocationX;
    double newLocationY;

    if (p.x != 0 && p.y !=0)
    {
      newLocationX = (p.x * deltaTime * (speed / SQRT2)) + location.x;
      newLocationY = (p.y * deltaTime * (speed / SQRT2)) + location.y;
    }

    else {
      newLocationX = (p.x * deltaTime * speed) + location.x;
      newLocationY = (p.y * deltaTime * speed) + location.y;
    }

    nextHitbox.setFrame(newLocationX, newLocationY, size.getWidth(), size.getHeight());

    checkCollisions(p);

    newLocationX = (Math.abs(vectorToMove.x) * (newLocationX - location.x)) + location.x;
    newLocationY = (Math.abs(vectorToMove.y) * (newLocationY - location.y)) + location.y;

    location.setLocation(newLocationX, newLocationY);
    hitbox.setFrame(location,size);
    nextHitbox.setFrame(hitbox);

    //Direction Setting
    direction = null;
    if (p.y > 0)
    {
      direction = Direction.DOWN;
    } else if (p.y < 0)
    {
      direction = Direction.UP;
    } else if (p.x > 0)
    {
      direction = Direction.RIGHT;
    } else if (p.x < 0)
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
  public void update(long deltaTime, long secondsFromStart)
  {

  }

  @Override
  public Clip getSound()
  {
    return null;
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

  @Override
  public void collide(GameObject other)
  {

  }
}
