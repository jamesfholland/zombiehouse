package View;

import java.awt.*;
import java.util.Comparator;

/**
 * This class compares Point objects in order to sort them in a clockwise fashion around a CENTER point.
 * This is a utility class used when ordering points in shadow detection.
 */
class ClockwisePointComparator implements Comparator<Point>
{
  private final Point CENTER;

  /**
   * Sets up a new comparator with CENTER to sort around.
   * @param center
   */
  ClockwisePointComparator(Point center)
  {
    this.CENTER = center;
  }

  /**
   * Compare two points angles from the predetermined CENTER
   * @param pointA
   * @param pointB
   * @return -1 to 1 if pointA is less than to greater than pointB on a clockwise angle.  If both angles are the same, it returns a Double.compare of the distanceSq(A,B)
   */
  @Override
  public int compare(Point pointA, Point pointB)
  {
    int angleCompare = Double.compare(
        Math.atan2(pointA.y - CENTER.y, pointA.x - CENTER.x),
        Math.atan2(pointB.y - CENTER.y, pointB.x - CENTER.x));

    if (angleCompare == 0)
    {
      return Double.compare(CENTER.distanceSq(pointA), CENTER.distanceSq(pointB));
    }
    return angleCompare;
  }

}
