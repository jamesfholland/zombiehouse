package Model.Tile;

import Model.GameObject;
import Model.Tile.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Exit extends Tile
{

  public Exit(Point location)
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

  @Override
  public String toString() { return "f"; }
}
