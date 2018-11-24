package sorts;

import java.awt.geom.Point2D;

public class Interpola
{
  public static double cos(double first, double second, double loc)
  {
    if (loc>1||loc<0)
      throw new IllegalArgumentException("[Interpolation] Location out of bounds.");
    return first+((first-second)*((Math.cos(Math.PI*loc)+1)/2));
  }

  public static double linear(double first, double second, double loc)
  {
    if (loc>1||loc<0)
      throw new IllegalArgumentException("[Interpolation] Location out of bounds.");
    return first+(first-second)*loc;
  }

  public static double piecewise(double first, double second, double loc)
  {
    if (loc>=.5)
      return second;
    return first;
  }

  /*public static double cubic(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double loc)
  {
    if (loc>x3||loc<x2)
      throw new IllegalArgumentException("[Interpolation] Location out of bounds.");

    double slopeA = (y3-y1)/(x3-x1);
    double slopeB = (y4-y2)/(x4-x2);
    return cubic(slopeA, x2, y2, x3, y3, slopeB, loc);
  }

  public static double cubic(double slope1, double x2, double y2, double x3, double y3, double slope2, double loc)
  {
    // TODO
  }*/

  public static Point2D.Double bezeir(Point2D.Double[] points, double loc)
  {
    // One point -> one point
    if (points.length == 1)
      return (Point2D.Double) points[0].clone();

    if (points.length == 0)
      throw new IllegalArgumentException("[Interpolation] No points to interpolate between.");

    if (loc > 1 || loc < 0)
      throw new IllegalArgumentException("[Interpolation] Location out of bounds.");

    // Base case
    if (points.length == 2)
      return new Point2D.Double(linear(points[0].x, points[1].x, loc), linear(points[0].y, points[1].y, loc));

    // Recursive: try next one
    Point2D.Double[] next = new Point2D.Double[points.length - 1];
    for (int i = 0; i < points.length - 1; i++)
      next[i] = new Point2D.Double(linear(points[i].x, points[i + 1].x, loc), linear(points[i].y, points[i + 1].y, loc));
    return bezeir(next, loc);
  }
}
