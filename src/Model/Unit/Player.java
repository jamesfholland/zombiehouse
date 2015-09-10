package Model.Unit;

import Model.GameObject;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Unit
{

  public Player(Point location)
  {
    this.location = location;
    this.size = new Dimension(60, 70);

  }

  @Override
  public void update()
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
