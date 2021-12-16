package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BruteCollinearPoints {
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

        // filter repeated point (done in such a way, since set usage is not allowed by the assignment
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (0 == points[i].compareTo(points[i + 1])) {
                throw new IllegalArgumentException();
            }
        }

        // find 4 collinear points in a row (by assignment we're assured that 4
        // is the maxim possible number of collinear points
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                double slopeBetweenFirstAndSecondPoints = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length - 1; k++) {
                    double slopeBetweenFirstAndThirdPoints = points[i].slopeTo(points[k]);
                    if (slopeBetweenFirstAndSecondPoints != slopeBetweenFirstAndThirdPoints) {
                        continue;
                    }
                    for (int l = k + 1; l < points.length; l++) {
                        double slopeBetweenFirstAndFourthPoints = points[i].slopeTo(points[l]);
                        if (slopeBetweenFirstAndSecondPoints == slopeBetweenFirstAndFourthPoints) {
                            collinearCount++;

                            List<Point> currentCollinearPoints = new ArrayList<Point>();
                            currentCollinearPoints.add(points[i]);
                            currentCollinearPoints.add(points[j]);
                            currentCollinearPoints.add(points[k]);
                            currentCollinearPoints.add(points[l]);
                            Collections.sort(currentCollinearPoints);

                            segments.add(new LineSegment(currentCollinearPoints.get(0), currentCollinearPoints.get(3)));
                        }
                    }
                }
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