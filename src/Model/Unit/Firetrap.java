package Model.Unit;

import Model.GameObject;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class Firetrap extends Unit
{

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
