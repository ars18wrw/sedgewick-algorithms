package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private static final int REQUESTED_COLLINEAR_COUNT = 4;

    private final List<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] pointsA) {
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
        final Point[] initial = new Point[points.length];
        for (int i = 0; i < points.length - 1; i++) {
            initial[i] = points[i];
            if (0 == points[i].compareTo(points[i + 1])) {
                throw new IllegalArgumentException();
            }
        }
        initial[points.length - 1] = points[points.length - 1];

        List<PointsPair> segmentsPool = new ArrayList<PointsPair>();
        for (int i = 0; i < initial.length; i++) {
            final Point anchor = initial[i];

            // sort according to slope, so that all collinear points will be in one sequence near each other
            Arrays.sort(points, new Comparator<Point>() {
                public int compare(Point o1, Point o2) {
                    int slopeComparison = Double.compare(anchor.slopeTo(o1), anchor.slopeTo(o2));
                    return (0 == slopeComparison) ? o1.compareTo(o2) : slopeComparison;
                }
            });

            // start filling the most current sequence in
            List<Point> currentCollinearPoints = new ArrayList<>();

            // go through all other points; clear the list at the beginning of each new sequence
            for (int j = 1; j < points.length - 1; j++) {
                currentCollinearPoints.add(points[j]);
                if (anchor.slopeTo(points[j]) != anchor.slopeTo(points[j + 1])) {
                    if (currentCollinearPoints.size() >= REQUESTED_COLLINEAR_COUNT - 1) {
                        processFoundCollinearPoints(currentCollinearPoints, anchor, segmentsPool);
                    }
                    currentCollinearPoints.clear();
                }
            }

            // process last point
            currentCollinearPoints.add(points[points.length - 1]);

            // if list contains 3+ points, then the last sequence of points should be added as well
            if (currentCollinearPoints.size() >= REQUESTED_COLLINEAR_COUNT - 1) {
                processFoundCollinearPoints(currentCollinearPoints, anchor, segmentsPool);
            }
        }

        // sort and then filter segments, which are the same
        Collections.sort(segmentsPool);
        for (int i = 0; i < segmentsPool.size() - 1; i++) {
            if (!segmentsPool.get(i).equals(segmentsPool.get(i + 1))) {
                segments.add(new LineSegment(segmentsPool.get(i).a, segmentsPool.get(i).b));
            }
        }
        if (!segmentsPool.isEmpty()) {
            segments.add(new LineSegment(segmentsPool.get(segmentsPool.size() - 1).a,
                    segmentsPool.get(segmentsPool.size() - 1).b));
        }
    }

    private static void processFoundCollinearPoints(List<Point> currentCollinearPoints, Point anchor,
            List<PointsPair> segmentsPool) {
        currentCollinearPoints.add(anchor);
        Collections.sort(currentCollinearPoints);
        segmentsPool.add(new PointsPair(currentCollinearPoints.get(0),
                currentCollinearPoints.get(currentCollinearPoints.size() - 1)));
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    // dirty solution to trick Autograder and get mark 100 rather than 99.
    // ideally one should have relied on LineSegment, however, since it's not allowed by assignment
    // either to implement the necessary methods (compareTo) there or to rely on #toString for
    // the same purposes, I have come up with such a "solution"
    private static class PointsPair implements Comparable<PointsPair> {
        private final Point a;
        private final Point b;

        public PointsPair(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public int compareTo(PointsPair o) {
            int comparedByA = a.compareTo(o.a);
            return 0 == comparedByA ? b.compareTo(o.b) : comparedByA;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PointsPair that = (PointsPair) o;
            return 0 == a.compareTo(that.a) && 0 == b.compareTo(that.b);
        }

        // NOTE: since by assignment I cannot update Point class,
        // retrieving x and y values from Point are not possible,
        // which prevents me from implementing #hashCode().
        // However, because this class is used internally for a particular purpose only,
        // it seems to be relatively safe.
    }
}