package Model.Unit.Zombie;

import Model.GameObject;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class ZombieLine extends Zombie
{

  private boolean collided = false;

  public ZombieLine(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  @Override
  public void update(long deltaTime, long secondsFromStart)
  {
    if ((secondsFromStart%2)==0 && collided)
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
    return null;
  }

  @Override
  public void collide(GameObject other)
  {
  }
}
