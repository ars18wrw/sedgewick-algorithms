package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
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

        List<LineSegment> segmentsPool = new ArrayList<>();
        for (int i = 0; i < initial.length; i++) {
            final Point anchor = initial[i];
            Arrays.sort(points, new Comparator<Point>() {
                public int compare(Point o1, Point o2) {
                    int slopeComparison = Double.compare(anchor.slopeTo(o1), anchor.slopeTo(o2));
                    return (0 == slopeComparison) ? o1.compareTo(o2) : slopeComparison;
                }
            });
            List<Point> currentCollinearPoints = new ArrayList<>();
            Point currentStart = points[0];
            currentCollinearPoints.add(anchor);
            currentCollinearPoints.add(currentStart);
            for (int j = 1; j < points.length; j++) {
                if (anchor == points[j]) {
                    continue;
                }
                if (anchor.slopeTo(currentStart) != anchor.slopeTo(points[j])) {
                    if (currentCollinearPoints.size() >= REQUESTED_COLLINEAR_COUNT) {
                        Collections.sort(currentCollinearPoints);
                        segmentsPool.add(new LineSegment(currentCollinearPoints.get(0),
                                currentCollinearPoints.get(currentCollinearPoints.size() - 1)));
                    }
                    currentStart = points[j];
                    currentCollinearPoints = new ArrayList<>();
                    currentCollinearPoints.add(anchor);
                }
                currentCollinearPoints.add(points[j]);
            }
            if (currentCollinearPoints.size() >= REQUESTED_COLLINEAR_COUNT) {
                Collections.sort(currentCollinearPoints);
                segmentsPool.add(new LineSegment(currentCollinearPoints.get(0),
                        currentCollinearPoints.get(currentCollinearPoints.size() - 1)));
            }
        }


        Collections.sort(segmentsPool, new Comparator<LineSegment>() {
            @Override
            public int compare(LineSegment o1, LineSegment o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        for (int i = 0; i < segmentsPool.size() - 1; i++) {
            if (!segmentsPool.get(i).toString().equals(segmentsPool.get(i + 1).toString())) {
                segments.add(segmentsPool.get(i));
            }
        }
        if (!segmentsPool.isEmpty()) {
            segments.add(segmentsPool.get(segmentsPool.size() - 1));
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}