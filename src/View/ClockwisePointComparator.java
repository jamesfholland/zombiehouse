package View;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * This class compares Point2D objects in order to sort them in a clockwise fashion around a center point.
 */
class ClockwisePointComparator implements Comparator<Point2D>
{
  private Point2D center;
  ClockwisePointComparator(Point2D center)
  {
    this.center = center;
  }
  @Override
  public int compare(Point2D pointA, Point2D pointB)
  {
    return (int)((pointA.getX() - center.getX())*(pointB.getY() - center.getY()) - (pointB.getX() - center.getX()) * (pointA.getY() - center.getY()));
  }
}
