package eightpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private static final String LINE_SEPARATOR = "\n";
    private static final int EMPTY_TILE_VALUE = 0;

    private final int[][] array;
    private final int n;

    private int[] zeroPosition;

    private int hamming = -1;
    private int manhattan = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (null == tiles) {
            throw new IllegalArgumentException();
        }
        array = tiles;
        n = tiles.length;
    }

    private Board(Board original, int firstLineIndex, int secondLineIndex) {
        array = new int[original.array.length][];
        for (int i = 0; i < array.length; i++) {
            if (i != firstLineIndex && i != secondLineIndex) {
                array[i] = original.array[i];
            } else {
                // only in case of changed rows we should create new arrays rahter than just copy the old ones
                array[i] = new int[array.length];
                for (int j = 0; j < array.length; j++) {
                    array[i][j] = original.array[i][j];
                }
            }
        }
        n = original.n;
        if (null != original.zeroPosition) {
            zeroPosition = original.zeroPosition.clone();
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n);
        appendSeparator(builder);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(String.format("%4d ", array[i][j]));
            }
            appendSeparator(builder);
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (-1 != hamming) {
            // already calculated
            return hamming;
        }
        // we start with -1, since "0" is always out of place
        int currentHamming = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentHamming += (array[i][j] == getExpectedValue(i, j)) ? 0 : 1;
            }
        }
        hamming = currentHamming;
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (-1 != manhattan) {
            // already calculated
            return manhattan;
        }
        int currentManhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (EMPTY_TILE_VALUE != array[i][j]) {
                    int[] expectedPosition = getExpectedPosition(array[i][j]);
                    currentManhattan += Math.abs(i - expectedPosition[0]) + Math.abs(j - expectedPosition[1]);
                }
            }
        }
        manhattan = currentManhattan;
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return 0 == manhattan();
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        return n == board.n &&
                Arrays.deepEquals(array, board.array);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (null == zeroPosition) {
            findZeroPosition();
        }
        List<int[]> positions = new ArrayList<>();
        positions.add(new int[] {zeroPosition[0] - 1, zeroPosition[1]});
        positions.add(new int[] {zeroPosition[0] + 1, zeroPosition[1]});
        positions.add(new int[] {zeroPosition[0], zeroPosition[1] - 1});
        positions.add(new int[] {zeroPosition[0], zeroPosition[1] + 1});

        List<Board> neighbours = new ArrayList<>();
        for (int[] position : positions) {
            if (validatePosition(position)) {
                Board nextBoard = swap(this, zeroPosition, position);
                nextBoard.zeroPosition = position;
                neighbours.add(nextBoard);
            }
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        findZeroPosition();
        int randomValue = 0;
        int[] randomPosition = null;
        int[] updatedPosition = null;
        do {
            randomPosition = getExpectedPosition(randomValue++);
            // we will always swap with the next tile on the right (if it's possible)
            updatedPosition = new int[] {randomPosition[0], randomPosition[1] + 1};
        } while (!validatePosition(updatedPosition)
                || Arrays.equals(zeroPosition, updatedPosition)
                || Arrays.equals(zeroPosition, randomPosition));
        return swap(this, randomPosition, updatedPosition);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] testArray = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board testBoard = new Board(testArray);
        System.out.println(testBoard.hamming());
        System.out.println(testBoard.manhattan());

        int[][] testArray2 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board testBoard2 = new Board(testArray2);
        System.out.println(testBoard2);

        System.out.println("////////////////////////");

        for (Board neighbour : testBoard2.neighbors()) {
            System.out.println(neighbour);
        }
    }

    private int getExpectedValue(int i, int j) {
        return n * i + j + 1;
    }

    private int[] getExpectedPosition(int value) {
        int horizontalIndex = value / n;
        int verticalIndex = value % n - 1;
        if (verticalIndex < 0) {
            horizontalIndex--;
            verticalIndex += n;
        }
        return new int[] {horizontalIndex, verticalIndex};
    }

    private void findZeroPosition() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (EMPTY_TILE_VALUE == array[i][j]) {
                    zeroPosition = new int[] {i, j};
                    return;
                }
            }
        }
    }

    private boolean validatePosition(int[] position) {
        return position[0] >= 0 && position[0] < n && position[1] >= 0 && position[1] < n;
    }

    private Board swap(Board original, int[] first, int[] second) {
        Board copy = new Board(original, first[0], second[0]);
        int temp = copy.array[first[0]][first[1]];
        copy.array[first[0]][first[1]] = copy.array[second[0]][second[1]];
        copy.array[second[0]][second[1]] = temp;
        return copy;
    }

    private static void appendSeparator(StringBuilder builder) {
        builder.append(LINE_SEPARATOR);
    }
}