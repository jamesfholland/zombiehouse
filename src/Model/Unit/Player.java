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

  private static final BufferedImage[] WALK_UP_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_RIGHT_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_LEFT_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_DOWN_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final double SQRT2 = Math.sqrt(2);
  private Point testPoint;

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
    spriteHeight = imageTemp.getHeight()/SPRITES_ROWS;
    spriteWidth = imageTemp.getWidth()/SPRITES_PER_ROW;

    int spriteRow = WALK_SPRITE_ROW;
    //UP
    for (int i = 0; i < WALK_SPRITE_COUNT; i++)
    {
      WALK_UP_IMAGE[i] = imageTemp.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);
    }
    //LEFT
    spriteRow++; //Sprites rows are always in this order.
    for (int i = 0; i < WALK_SPRITE_COUNT; i++)
    {
      WALK_LEFT_IMAGE[i] = imageTemp.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);
    }
    //DOWN
    spriteRow++; //Sprites rows are always in this order.
    for (int i = 0; i < WALK_SPRITE_COUNT; i++)
    {
      WALK_DOWN_IMAGE[i] = imageTemp.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);
    }
    //RIGHT
    spriteRow++; //Sprites rows are always in this order.
    for (int i = 0; i < WALK_SPRITE_COUNT; i++)
    {
      WALK_RIGHT_IMAGE[i] = imageTemp.getSubimage(i*spriteWidth, spriteRow*spriteHeight, spriteWidth, spriteHeight);
    }

  }

  public Player(Point location, Level level)
  {
    this.location = location;
    this.level = level;
    //this.speed = Settings.TILE_SIZE/1000.0;

    this.speed = Settings.TILE_SIZE/60.0;
    this.size = new Dimension(60, 70);
    this.hitbox = new Rectangle(location,size);
    this.nextHitbox = new Rectangle(location, size);
    this.testPoint = new Point(0,0);

  }

  public void move(Point p, long deltaTime)
  {
    double newLocationX;
    double newLocationY;

    /*
    if (p.x != 0 && p.y != 0)
    {
      newLocationX = (p.x*deltaTime*(speed/SQRT2)) + location.x;
      newLocationY = (p.y*deltaTime*(speed/SQRT2)) + location.y;

      nextHitbox.setFrame(newLocationX,newLocationY,size.getWidth(),size.getHeight());
    }
    else
    {
      newLocationX = (p.x*speed*deltaTime) + location.x;
      newLocationY = (p.y*speed*deltaTime) + location.y;
      nextHitbox.setFrame(newLocationX,newLocationY,size.getWidth(),size.getHeight());
    }

    if (p.x == -1 && checkCollideLeft())
    {
      return;
    }

    if (p.x == 1 && checkCollideRight())
    {
      return;
    }

    if (p.y == -1 && checkCollideUp())
    {
      return;
    }

    if (p.y == 1 && checkCollideDown())
    {
      return;
    }
    else{
      location.setLocation(newLocationX, newLocationY);
      nextHitbox.setFrame(0,0,0,0);
      hitbox.setFrame(location, size);
    }*/


    if (p.x != 0 && p.y != 0)
    {
      newLocationX = (p.x*deltaTime*(speed/SQRT2)) + location.x;
      newLocationY = (p.y*deltaTime*(speed/SQRT2)) + location.y;

      nextHitbox.setFrame(newLocationX,newLocationY,size.getWidth(),size.getHeight());
      testPoint = checkCollisionsDiag(p);

      newLocationX = (Math.abs(testPoint.x)*(p.x*deltaTime*(speed/SQRT2)) + location.x);
      newLocationY = (Math.abs(testPoint.y)*(p.y*deltaTime*(speed/SQRT2)) + location.y);

      location.setLocation(newLocationX,newLocationY);
      hitbox.setFrame(location, size);

    }


    else if (p.x ==0 || p.y==0)
    {
      newLocationX = (p.x*speed*deltaTime) + location.x;
      newLocationY = (p.y*speed*deltaTime) + location.y;
      nextHitbox.setFrame(newLocationX,newLocationY,size.getWidth(),size.getHeight());
      testPoint = checkCollisionsCardinal(p);

      newLocationX = (Math.abs(testPoint.x)*(p.x*speed*deltaTime)) + location.x;
      newLocationY = (Math.abs(testPoint.y)*(p.y*speed*deltaTime)) + location.y;


      location.setLocation(newLocationX,newLocationY);
      hitbox.setFrame(location, size);
    }

    //Direction Setting
    direction = null;
    if(p.y > 0 )     direction = Direction.DOWN;
    else if(p.y < 0) direction = Direction.UP;
    else if(p.x > 0) direction = Direction.RIGHT;
    else if(p.x < 0) direction = Direction.LEFT;

    if(direction != null)
    {
      spriteState++;
      if(spriteState >= WALK_SPRITE_COUNT) spriteState = 0;
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
    if(direction == null) return WALK_DOWN_IMAGE[0];

    switch(direction)
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
