package eightpuzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private static final String UNSOLVABLE_MESSAGE = "Unsolvable puzzle";

    private final MinPQ<SearchNode> priorityQueue;
    private final MinPQ<SearchNode> priorityQueueAltered;

    private boolean solvable = false;
    private int moves = -1;
    private List<Board> solution = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (null == initial) {
            throw new IllegalArgumentException();
        }
        priorityQueue = new MinPQ<>(new ManhattanComparator());
        priorityQueue.insert(new SearchNode(initial, null, 0));

        priorityQueueAltered = new MinPQ<>(new ManhattanComparator());
        priorityQueueAltered.insert(new SearchNode(initial.twin(), null, 0));

        SearchNode current = null;
        while (!priorityQueue.isEmpty()) {
            current = algoIteration(priorityQueue);
            if (current.board.isGoal()) {
                solvable = true;
                break;
            }
            if (priorityQueueAltered.isEmpty() || algoIteration(priorityQueueAltered).board.isGoal()) {
                System.out.println(UNSOLVABLE_MESSAGE);
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
            moves++;
            Collections.reverse(solution);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private SearchNode algoIteration(MinPQ<SearchNode> queue) {
        SearchNode current = queue.delMin();

        for (Board neighbour : current.board.neighbors()) {
            if (null == current.previousNode || !neighbour.equals(current.previousNode.board)) {
                queue.insert(new SearchNode(neighbour, current, current.moves + 1));
            }
        }
        return current;
    }

    private class ManhattanComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            return Integer.compare(o1.board.manhattan() + o1.moves, o2.board.manhattan() + o2.moves);
        }
    }

    private class SearchNode {
        private Board board;
        private SearchNode previousNode;
        private int moves;

        public SearchNode(Board board, SearchNode previousBoard, int moves) {
            this.board = board;
            this.previousNode = previousBoard;
            this.moves = moves;
        }
    }
}