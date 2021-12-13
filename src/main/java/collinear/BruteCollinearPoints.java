package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BruteCollinearPoints {
    private static final int REQUESTED_COLLINEAR_COUNT = 4;

    private int collinearCount;
    private final List<LineSegment> segments = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] pointsA) {
        if (null == pointsA) {
            throw new IllegalArgumentException();
        }
        Point[] points = new Point[pointsA.length];
        for (int i = 0; i < points.length; i++) {
            points[i] = pointsA[i];
            if (null == points[i]) {
                throw new IllegalArgumentException();
            }
        }

        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (0 == points[i].compareTo(points[i + 1])) {
                throw new IllegalArgumentException();
            }
        }

        // find 4 collinear points in a row
        for (int i = 0; i < points.length - 3; i++) {
            List<Point> currentCollinearPoints = new ArrayList<Point>();
            currentCollinearPoints.add(points[i]);
            out:
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        double testSlope = points[i].slopeTo(points[j]);
                        if (testSlope == points[i].slopeTo(points[k]) &&
                                testSlope == points[i].slopeTo(points[l])) {
                            currentCollinearPoints.add(points[j]);
                            currentCollinearPoints.add(points[k]);
                            currentCollinearPoints.add(points[l]);
                            break out;
                        }
                    }
                }
            }
            if (REQUESTED_COLLINEAR_COUNT == currentCollinearPoints.size()) {
                collinearCount++;
                Collections.sort(currentCollinearPoints);
                segments.add(new LineSegment(currentCollinearPoints.get(0), currentCollinearPoints.get(3)));
            }
        }
    }

    public int numberOfSegments() {
        return collinearCount;
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }
}