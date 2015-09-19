package Model.Unit.Zombie;

import Model.GameObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ZombieLine extends Zombie
{

  private final static BufferedImage ZOMBIE_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(ZombieMaster.class.getResourceAsStream("zombieRight.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    ZOMBIE_IMAGE = imageTemp;
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
    return ZOMBIE_IMAGE;
  }

  @Override
  public void collide(GameObject other)
  {
  }
}
