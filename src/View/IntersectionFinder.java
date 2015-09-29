package View;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * This class returns intersection points between 2 Point objects(forming a line) and Rectangle2D objects.
 *
 * This is a utility class used in shadow detection.
 */
class IntersectionFinder
{

  /**
   * We want to only return the point closest to the source location.
   *
   * @param source      The source point we are eminating from.
   * @param destination the destination coordinate
   * @param rectangle   The rectangle we are intersecting.
   * @return The closest point to the source that intersects the rectangle, null if no intersection is found.
   */
  static Point getIntersections(Point source, Point destination, Rectangle2D rectangle)
  {
    //Compute intersections for 4 cardinal corners of rectangle.
    Point topLeft = new Point((int) rectangle.getX(), (int) rectangle.getY());
    Point topRight = new Point((int) rectangle.getMaxX(), (int) rectangle.getY());
    Point bottomLeft = new Point((int) rectangle.getX(), (int) rectangle.getMaxY());
    Point bottomRight = new Point((int) rectangle.getMaxX(), (int) rectangle.getMaxY());

    Point top;
    Point left;
    Point right;
    Point bottom;
    Point closest = null;
    double distanceSquared = Double.POSITIVE_INFINITY;

    //Up
    top = getLineIntersection(source, destination, topLeft, topRight);
    //Left
    left = getLineIntersection(source, destination, topLeft, bottomLeft);
    //Right
    right = getLineIntersection(source, destination, topRight, bottomRight);
    //Bottom
    bottom = getLineIntersection(source, destination, bottomLeft, bottomRight);

    if (top != null)
    {
      closest = top;
      distanceSquared = source.distanceSq(top);
    }

    if (left != null)
    {
      if (closest == null || source.distanceSq(left) < distanceSquared)
      {
        closest = left;
        distanceSquared = source.distanceSq(left);
      }
    }

    if (right != null)
    {
      if (closest == null || source.distanceSq(right) < distanceSquared)
      {
        closest = right;
        distanceSquared = source.distanceSq(right);
      }
    }

    if (bottom != null)
    {
      if (closest == null || source.distanceSq(bottom) < distanceSquared)
      {
        closest = bottom;
      }
    }

    return closest;

  }

  /**
   * Return a Point if the lines intersect.
   *
   * @param source      the source point (in Zombiehouse, usually the PLAYER)
   * @param destination the destination (usually box corner)
   * @param lineA       first point in line we may intersect
   * @param lineB       second point in line we may intersect
   * @return the intersection as a Point, or null if no intersection found.
   */
  private static Point getLineIntersection(Point source, Point destination, Point lineA, Point lineB)
  {
    if (!Line2D.linesIntersect(source.x, source.y, destination.x, destination.y, lineA.x, lineA.y, lineB.x, lineB.y))
    {
      return null; //We don't have an intersection at all.
    }

    //We intersect so compute the point.
    double m;
    double b;
    try
    {
      m = (source.y - destination.y) / (double) (source.x - destination.x);
      b = destination.y - m * destination.x;
    } //Catch divide by zero if vertical line.
    catch (ArithmeticException error)
    {
      //Since we have a vertical line, compute other way with forced x
      try
      {
        double mPrime = (lineA.y - lineB.y) / (double) (lineA.x - lineB.x);
        double bPrime = lineA.y - mPrime * lineB.x;

        double y = mPrime * source.x + bPrime;
        return new Point(source.x, (int) y);
      }
      catch (ArithmeticException error2)
      {
        //Two vertical lines have no intersection unless on the same line meaning infinite intersections
        return null;
      }
    }

    //Check if on horizontal line, if so we can simply by using lineA.x as the horizontal
    if (lineA.x == lineB.x)
    {
      double y = m * lineA.x + b;
      return new Point(lineA.x, (int) y);
    }//Check if vertical line, if so use lineA.y to simplify.
    else if (lineA.y == lineB.y)
    {
      double x = (lineA.y - b) / m;
      return new Point((int) x, lineA.y);
    }//Simple math won't work use complicated math to find intersection. Also means we aren't on rectangle anymore.
    else
    {
      double mPrime = (lineA.y - lineB.y) / (double) (lineA.x - lineB.x);
      double bPrime = lineA.y - mPrime * lineB.x;

      //mx + b = m'x + b' is the basis for this computation.

      double x = (bPrime - b) / (m - mPrime);
      double y = m * x + b;
      return new Point((int) x, (int) y);
    }
  }
}
