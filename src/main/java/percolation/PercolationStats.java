package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_FACTOR = 1.96;

    private final double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validateInteger(n);
        validateInteger(trials);
        results = new double[trials];

        for (int i = 0; i < results.length; i++) {
            results[i] = (double) performPercolation(n) / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double meanValue = mean();
        double stddevValue = stddev();
        return meanValue - stddevValue * CONFIDENCE_FACTOR / Math.sqrt(results.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double meanValue = mean();
        double stddevValue = stddev();
        return meanValue + stddevValue * CONFIDENCE_FACTOR / Math.sqrt(results.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 0;
        int trials = 0;
        try {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }

    private static int performPercolation(int size) {
        Percolation percolation = new Percolation(size);
        while (!percolation.percolates()) {
            int row = 1 + getRandomInteger(size);
            int col = 1 + getRandomInteger(size);
            while (percolation.isOpen(row, col)) {
                row = 1 + getRandomInteger(size);
                col = 1 + getRandomInteger(size);
            }
            percolation.open(row, col);
        }
        return percolation.numberOfOpenSites();
    }

    private static int getRandomInteger(int upperBound) {
        return StdRandom.uniform(upperBound);
    }

    private void validateInteger(int i) {
        if (0 >= i) {
            throw new IllegalArgumentException();
        }
    }
}