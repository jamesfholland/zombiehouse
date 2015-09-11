package Model.Tile;

import Model.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Pillar is a derivative of floor and wall
 */
public class Pillar extends Tile
{

  public Pillar(Point location)
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
  public String toString()
  {
    return "I";
  }
}
