package Model;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject
{
  /**
   * Height and width of object.
   */
  protected Dimension size;
  /**
   * Top Left corner of object in pixels
   */
  protected Point location;

  /**
   * Our hitbox, assumed rectangular for simplicity
   */
  private Rectangle2D hitbox = new Rectangle();
  protected static Level level;


  /**
   * Computes the hitbox if not already computed or returns the precomputed hitbox.
   */
  private void setHitbox()
  {
    if (location.x != (int) hitbox.getX() || location.y != (int) hitbox.getY())
    {
      hitbox.setFrame(location, size);
    }
  }

  /**
   * Gets the hitbox of the object.
   *
   * @return hitbox
   */
  public Rectangle2D getHitbox()
  {
    setHitbox();
    return this.hitbox;
  }

  /**
   * Pass object a hitbox to check intersection with. Intersection means collision.
   *
   * @return true if a collision occurred, otherwise false
   */
  public boolean checkCollision(Rectangle2D otherHitbox)
  {
    setHitbox();
    return this.getHitbox().intersects(otherHitbox);
  }

  /**
   * Get the size of a game object.
   *
   * @return Dimension of object.
   */
  public Dimension getSize()
  {
    return new Dimension(this.size);
  }

  public Point getCenterLocation()
  {
    Point center = new Point();
    center.setLocation(getHitbox().getCenterX(), getHitbox().getCenterY());
    return center;
  }

  /**
   * Gets the tile coordinates where the center of the object is located.
   *
   * @return
   */
  public Point getTileLocation()
  {
    return new Point(this.getCenterLocation().x / Settings.TILE_SIZE, this.getCenterLocation().y / Settings.TILE_SIZE);
  }

  /**
   * Get the top left of object
   *
   * @return top left point of object
   */
  public Point getLocation()
  {
    return new Point(this.location);
  }

  /**
   * Get the image of the object to draw.
   * Animation state and direction are determined within the object instance.
   *
   * @return BufferedImage with object to draw.
   */
  public abstract BufferedImage getImage();

  public void setLocation(Point location)
  {
    this.location = location;
  }

  public void setLevel(Level level)
  {
    GameObject.level = level;
  }
}
