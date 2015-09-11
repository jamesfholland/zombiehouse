package Model.Unit.Zombie;

import Model.GameObject;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class ZombieRandom extends Zombie
{

  public ZombieRandom(int x, int y, double heading)
  {
    super(x, y, heading);
  }

  @Override
  public void update(long deltaTime)
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
    return null;
  }

  @Override
  public void collide(GameObject other)
  {

  }
}
