package View;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * This class compares Point2D objects in order to sort them in a clockwise fashion around a center point.
 */
class ClockwisePointComparator implements Comparator<Point>
{
  private Point center;

  ClockwisePointComparator(Point center)
  {
    this.center = center;
  }

  @Override
  public int compare(Point pointA, Point pointB)
  {
    return Double.compare(
        Math.atan2(pointA.y - center.y, pointA.x - center.x),
        Math.atan2(pointB.y - center.y, pointB.x - center.x));
  }
}