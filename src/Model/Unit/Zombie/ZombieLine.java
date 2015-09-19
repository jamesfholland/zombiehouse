package Model.Unit.Zombie;

import Model.GameObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ZombieLine extends Zombie
{
  private static final BufferedImage[] WALK_UP_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_RIGHT_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_LEFT_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];
  private static final BufferedImage[] WALK_DOWN_IMAGE = new BufferedImage[WALK_SPRITE_COUNT];

  private boolean collided = false;

  static
  {
    BufferedImage imageTemp = null;
    int spriteHeight;
    int spriteWidth;

    try
    {
      imageTemp = ImageIO.read(ZombieLine.class.getResourceAsStream("zombieLine.png"));
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
  public ZombieLine(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  @Override
  public void update(long deltaTime, long secondsFromStart)
  {
    double nextLocationX;
    double nextLocationY;
    double headingR;

    if ((secondsFromStart%2)==0 && collided)
    {
      makeDecision();
      collided = false;
    }

    headingR = toRadians();

    nextLocationY = (Math.sin(headingR)*speed*deltaTime) + location.y;
    nextLocationX = (Math.cos(headingR)*speed*deltaTime) + location.x;

    doubleY = (Math.sin(headingR)*speed*deltaTime) + doubleY;
    doubleX = (Math.cos(headingR)*speed*deltaTime) + doubleX;


    //System.out.println();
    //System.out.println(""+ heading);
    //System.out.println(""+ nextLocationX);
    //System.out.println(""+ nextLocationY);


    nextHitbox.setFrame(nextLocationX,nextLocationY,size.width,size.height);

    //if (direction.x != 0 && direction.y != 0)
    //{
      //testPoint = checkCollisionsDiag(direction);
      //if (testPoint.x == 0 || testPoint.y == 0) collided = true;

      //nextLocationX = (Math.abs(testPoint.x)*(Math.cos(this.heading)*speed*deltaTime) + location.x);
      //nextLocationY = (Math.abs(testPoint.y)*(Math.sin(this.heading)*speed*deltaTime) + location.y);
    //}
    //else
    //{
      //testPoint = checkCollisionsCardinal(direction);

      //if (testPoint.x == 0 && testPoint.y ==0) collided = true;

      //nextLocationX = (Math.abs(testPoint.x)*(Math.cos(this.heading)*speed*deltaTime) + location.x);
      //nextLocationY = (Math.abs(testPoint.y)*(Math.sin(this.heading)*speed*deltaTime) + location.y);
    //}

    location.setLocation(doubleX,doubleY);
    //hitbox.setFrame(location, size);
  }

  private void makeDecision()
  {
    heading = (RAND.nextInt(360) + RAND.nextDouble());
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
