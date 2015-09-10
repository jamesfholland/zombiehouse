package Model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject
{
  /**
   * Height and width of object.
   */
  protected Dimension size;
  /**
   * Center of object in pixels
   */
  protected Point location;

  /**
   * Our hitbox, assumed rectangular for simplicity
   */
  protected Rectangle2D hitbox;

   /**
   * Pass object a hitbox to check intersection with. Intersection means collision.
   * @return true if a collision occurred, otherwise false
   */
  public boolean checkCollision(Rectangle2D otherHitbox)
  {
    return this.hitbox.intersects(otherHitbox);
  }

  /**
   * Get the size of a game object.
   * @return Dimension of object.
   */
  public Dimension getSize()
  {
    return new Dimension(this.size);
  }

  /**
   * Get the center of the object.
   * @return Point where object is centered.
   */
  public Point getLocation()
  {
    return new Point(this.location);
  }

  /**
   * Get the image of the object to draw.
   * Animation state and direction are determined within the object instance.
   * @return BufferedImage with object to draw.
   */
  public abstract BufferedImage getImage();

  /**
   * Collide with other object and perform actions called for.
   */
  public abstract void collide(GameObject other);
}
