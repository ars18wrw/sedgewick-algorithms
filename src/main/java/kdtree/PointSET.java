package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Set;
import java.util.TreeSet;

/**
 * Brute-force implementation
 */
public class PointSET {
    private final Set<Point2D> points;

    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        validateArgument(p);

        points.add(p);
    }

    public boolean contains(Point2D p) {
        validateArgument(p);

        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validateArgument(rect);

        Set<Point2D> resultantPoints = new TreeSet<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                resultantPoints.add(point);
            }
        }
        return resultantPoints;
    }

    public Point2D nearest(Point2D p) {
        validateArgument(p);

        Point2D champion = null;
        double championsDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double currentSquaredDistance = point.distanceSquaredTo(p);
            if (currentSquaredDistance < championsDistance) {
                championsDistance = currentSquaredDistance;
                champion = point;
            }
        }
        return champion;
    }

    private static void validateArgument(Object anyObject) {
        if (null == anyObject) {
            throw new IllegalArgumentException();
        }
    }
}