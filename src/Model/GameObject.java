package Model;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * This class is the abstract class for all of the game objects in zombiehouse.
 * <p>
 * This class is the highest class in the model hierarchy. Every other class in the model will eventually extend GameObject.
 * The level data class in the zombiehouse program holds instances of GameObjects which the controller tells the model to update on each refresh.
 * The view reads in from the level the existing game objects and paints them on the screen accordingly.
 * <p>
 * Every zombiehouse element with which the player interacts extends GameObject.
 * This includes every tile type, every zombie type, the player, the firetraps, and the fire.
 * <p>
 * Every class which extends GameObject will have data members such as its size, location, and hitbox
 * Every class which extends GameObject will have the functions necessary to get its current location on the tile array,
 * return the values of its data members, and check if the hitbox of a GameObject is overlapping with the hitbox
 * of another GameObject
 */
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
   * @return returns the tile based location of the object
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

  /**
   * Gives a pointer to the level data class to this instance of GameObject
   *
   * @param level The new level object that all game units should have.
   */
  public void setLevel(Level level)
  {
    GameObject.level = level;
  }
}
