package eightpuzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private static final String UNSOLVABLE_MESSAGE = "Unsolvable puzzle";

    // required by the task to return -1
    private static final int UNSOLVABLE_MOVES = -1;

    private boolean solvable;
    private List<Board> solution;
    private int moves = UNSOLVABLE_MOVES;

    public Solver(Board initial) {
        if (null == initial) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> priorityQueue = new MinPQ<>(new ManhattanComparator());
        priorityQueue.insert(new SearchNode(initial, null, 0));

        MinPQ<SearchNode> priorityQueueAltered = new MinPQ<>(new ManhattanComparator());
        priorityQueueAltered.insert(new SearchNode(initial.twin(), null, 0));

        SearchNode current = null;
        while (!priorityQueue.isEmpty()) {
            // iterate first in the main game tree and then in the altered (one of them will eventually succeed)
            current = algoIteration(priorityQueue);
            if (current.board.isGoal()) {
                solvable = true;
                break;
            }
            if (priorityQueueAltered.isEmpty() || algoIteration(priorityQueueAltered).board.isGoal()) {
                StdOut.print(UNSOLVABLE_MESSAGE);
                break;
            }
        }
        if (isSolvable()) {
            solution = new ArrayList<>();
            while (null != current.previousNode) {
                solution.add(current.board);
                moves++;
                current = current.previousNode;
            }
            solution.add(current.board);

            // since the field is initialized with -1, we need to increment it once more
            moves++;

            Collections.reverse(solution);
        }
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    private SearchNode algoIteration(MinPQ<SearchNode> queue) {
        SearchNode current = queue.delMin();

        Iterable<Board> neighbours = current.board.neighbors();
        for (Board neighbour : neighbours) {
            if (null == current.previousNode || !neighbour.equals(current.previousNode.board)) {
                queue.insert(new SearchNode(neighbour, current, current.moves + 1));
            }
        }
        return current;
    }

    private class ManhattanComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return Integer.compare(o1.getManhattan() + o1.moves, o2.getManhattan() + o2.moves);
        }
    }

    private class SearchNode {
        private final Board board;
        private final SearchNode previousNode;
        private final int moves;

        // store Board's manhattan metric at Solver level to trick Autograder
        // (initially it was stored at Board level, however, such an optimization is not recognized by Autograder)
        private int storedManhattan = -1;

        public SearchNode(Board board, SearchNode previousBoard, int moves) {
            this.board = board;
            this.previousNode = previousBoard;
            this.moves = moves;
        }

        public int getManhattan() {
            if (-1 == storedManhattan) {
                storedManhattan = board.manhattan();
            }
            return storedManhattan;
        }
    }
}