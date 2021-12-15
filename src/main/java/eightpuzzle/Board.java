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
                // only in case of changed rows we should create new arrays rather than just copy the old ones
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

    public int dimension() {
        return n;
    }

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

    public int manhattan() {
        if (-1 != manhattan) {
            // already calculated
            return manhattan;
        }
        int currentManhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                currentManhattan += manhattanForTile(i, j);
            }
        }
        manhattan = currentManhattan;
        return manhattan;
    }

    private int manhattanForTile(int i, int j) {
        int currentManhattan = 0;
        if (EMPTY_TILE_VALUE != array[i][j]) {
            int[] expectedPosition = getExpectedPosition(array[i][j]);
            currentManhattan = Math.abs(i - expectedPosition[0]) + Math.abs(j - expectedPosition[1]);
        }
        return currentManhattan;
    }

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

    public Iterable<Board> neighbors() {
        if (null == zeroPosition) {
            findZeroPosition();
        }
        List<int[]> movedZeroPositions = new ArrayList<>();
        movedZeroPositions.add(new int[] {zeroPosition[0] - 1, zeroPosition[1]});
        movedZeroPositions.add(new int[] {zeroPosition[0] + 1, zeroPosition[1]});
        movedZeroPositions.add(new int[] {zeroPosition[0], zeroPosition[1] - 1});
        movedZeroPositions.add(new int[] {zeroPosition[0], zeroPosition[1] + 1});

        List<Board> neighbours = new ArrayList<>();
        for (int[] movedZeroPosition : movedZeroPositions) {
            if (validatePosition(movedZeroPosition)) {
                Board nextBoard = swap(this, zeroPosition, movedZeroPosition);
                nextBoard.zeroPosition = movedZeroPosition;
                neighbours.add(nextBoard);
            }
        }
        return neighbours;
    }

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

    private Board swap(Board original, int[] thisZeroPosition, int[] nextZeroPosition) {
        Board copy = new Board(original, thisZeroPosition[0], nextZeroPosition[0]);
        int temp = copy.array[thisZeroPosition[0]][thisZeroPosition[1]];
        copy.array[thisZeroPosition[0]][thisZeroPosition[1]] = copy.array[nextZeroPosition[0]][nextZeroPosition[1]];
        copy.array[nextZeroPosition[0]][nextZeroPosition[1]] = temp;

        if (-1 != manhattan) {
            // Manhattan metrics of the copy can be different only in the tile, which is moved
            copy.manhattan = this.manhattan
                    - manhattanForTile(nextZeroPosition[0], nextZeroPosition[1])
                    + copy.manhattanForTile(thisZeroPosition[0], nextZeroPosition[1]);
        }
        return copy;
    }

    private static void appendSeparator(StringBuilder builder) {
        builder.append(LINE_SEPARATOR);
    }
}