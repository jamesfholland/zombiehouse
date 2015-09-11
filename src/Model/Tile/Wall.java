package Model.Tile;

import Model.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends Tile
{
  public Wall(Point location)
  {
    super(); //Sets up shared Tile settings.
    this.location = location;
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
