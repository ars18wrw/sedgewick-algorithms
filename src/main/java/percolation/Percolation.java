package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;

    private final WeightedQuickUnionUF quickUnionImpl;
    private final int size;
    private final int bottom;

    private final boolean[][] grid;
    private int openSitesCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (0 >= n) {
            throw new IllegalArgumentException();
        }
        size = n;
        bottom = size * size + 1;
        quickUnionImpl = new WeightedQuickUnionUF(n * n + 2);

        // connect the first row with top
        for (int i = 1; i <= size; i++) {
            quickUnionImpl.union(TOP, i);
        }

        // connect the last row with bottom
        for (int i = 1; i <= size; i++) {
            quickUnionImpl.union(bottom, getSiteIndex(size, i));
        }

        grid = new boolean[size][size];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndex(row);
        validateIndex(col);

        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            openSitesCount++;

            // top
            if (row > 1 && grid[row - 2][col - 1]) {
                quickUnionImpl.union(getSiteIndex(row, col), getSiteIndex(row - 1, col));
            }

            // right
            if (col < size && grid[row - 1][col]) {
                quickUnionImpl.union(getSiteIndex(row, col), getSiteIndex(row, col + 1));
            }

            // bottom
            if (row < size && grid[row][col - 1]) {
                quickUnionImpl.union(getSiteIndex(row, col), getSiteIndex(row + 1, col));
            }

            // left
            if (col > 1 && grid[row - 1][col - 2]) {
                quickUnionImpl.union(getSiteIndex(row, col), getSiteIndex(row, col - 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndex(row);
        validateIndex(col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndex(row);
        validateIndex(col);
        return isOpen(row, col) &&
                quickUnionImpl.find(TOP) == quickUnionImpl.find(getSiteIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // in case the grid consists of 1 element only we need to check that the site is open
        return quickUnionImpl.find(TOP) == quickUnionImpl.find(bottom) && openSitesCount > 0;
    }

    private void validateIndex(int i) {
        if (0 >= i || size < i) {
            throw new IllegalArgumentException();
        }
    }

    private int getSiteIndex(int row, int col) {
        return (row - 1) * size + col;
    }
}