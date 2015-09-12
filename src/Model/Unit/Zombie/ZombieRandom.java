package Model.Unit.Zombie;

import Model.GameObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ZombieRandom extends Zombie
{

  private boolean collided;
  private final static BufferedImage ZOMBIE_IMAGE;

  static
  {BufferedImage imageTemp = null;

    try
    {
      imageTemp = ImageIO.read(ZombieMaster.class.getResourceAsStream("zombieLeft.png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    ZOMBIE_IMAGE = imageTemp;
  }
  public ZombieRandom(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  @Override
  public void update(long deltaTime, long secondsFromStart)
  {
    if ((secondsFromStart%2)==0)
    {
      makeDecision();
      collided = false;
    }

    double y = getLocation().getY() + (Math.sin(this.heading)*speed/deltaTime);
    double x = getLocation().getX() + (Math.cos(this.heading)*speed/deltaTime);
    location.setLocation(x, y);
  }

  private void makeDecision()
  {
    if (collided)
    {
      heading = (heading+180)%360;
    }
    else
    {
      heading = (RAND.nextInt(360) + RAND.nextDouble());
    }
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
