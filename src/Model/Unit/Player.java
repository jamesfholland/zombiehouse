package Model.Unit;

import Model.GameObject;
import Model.Level;
import Model.Settings;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Unit
{
  private static BufferedImage playerFront;

  public Player(Point location, Level level)
  {
    this.location = location;
    this.level = level;
    this.speed = Settings.TILE_SIZE/1000.0;
    this.size = new Dimension(60, 70);
    this.nextHitbox = new Rectangle(location, size);

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

  public void move(Point p, long deltaTime)
  {
    double newLocationX;
    double newLocationY;

    if (p.x != 0 && p.y != 0)
    {
      newLocationX = ((p.x)*(1/Math.sqrt(2)) + location.x);
      newLocationY = ((p.y)*(1/Math.sqrt(2)) + location.y);
      location.setLocation(newLocationX, newLocationY);
    }
    else
    {
      newLocationX = (p.x*speed*deltaTime) + location.x;
      newLocationY = (p.y*speed*deltaTime) + location.y;
      location.setLocation(newLocationX, newLocationY);
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
    return playerFront;
  }

  @Override
  public void collide(GameObject other)
  {

  }
}
