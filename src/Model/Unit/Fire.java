package Model.Unit;

import Model.GameObject;
import Model.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Fire extends Unit
{
  private boolean isBurning;
  private int timeBurning;

  public Fire(int tileX, int tileY)
  {
    this.location = new Point(tileX* Settings.TILE_SIZE, tileY*Settings.TILE_SIZE);
    this.size = new Dimension(Settings.TILE_SIZE, Settings.TILE_SIZE);
    this.getHitbox();

    this.timeBurning = 0;
  }

  @Override
  public void update(long deltaTime, long secondsFromStart)
  {
    if (isBurning && timeBurning < 15000)
    {
      timeBurning += deltaTime;
    }
    else if (isBurning && timeBurning >= 15000)
    {
      isBurning = false;
    }
  }

  public boolean isBurning()
  {
    return isBurning;
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
