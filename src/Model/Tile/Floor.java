package Model.Tile;

import Model.GameObject;

import java.awt.image.BufferedImage;

public class Floor extends Tile
{
  boolean isPillar;
  boolean isBurned;
  boolean isFire;


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
