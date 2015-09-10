package Model.Tile;

import Model.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Floor extends Tile
{
  private boolean isPillar;

  public Floor(Point location)
  {
    super(); //Sets up shared Tile settings.
    this.location = location;
    this.isPillar = false;

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
