package Model.Unit;

import Model.GameObject;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Unit
{
  private static BufferedImage playerFront;
  private int speed = 1;

  public Player(Point location)
  {
    this.location = location;
    this.size = new Dimension(60, 70);

    if(playerFront == null)
    {
      try
      {
        playerFront = ImageIO.read(this.getClass().getResourceAsStream("playerFront.png"));
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

  }

  /**
   * get the players speed
   * @return int value of speed
   */
  public int getSpeed()
  {
    return speed;
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
    return playerFront;
  }

  @Override
  public void collide(GameObject other)
  {

  }
}
