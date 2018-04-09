import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Board board;
    private boolean isSolvable = false;
    private Stack<Board> solution = new Stack<>();

    private class ManhattanBoard implements Comparable<ManhattanBoard> {
        Board board;
        int moves;
        ManhattanBoard prev;
        int manhattan;

        public ManhattanBoard(Board b) {
            this.board = b;
            this.moves = 0;
            this.manhattan = b.manhattan();
        }

        public ManhattanBoard(Board b, ManhattanBoard prevBoard) {
            this.board = b;
            this.prev = prevBoard;
            this.moves = prevBoard.moves + 1;
            this.manhattan = b.manhattan();
        }

        public int compareTo(ManhattanBoard manhattanBoard) {
            return this.manhattan - manhattanBoard.manhattan + this.moves - manhattanBoard.moves;
        }
    }

    public Solver(Board initial) {
        if(initial == null) throw new IllegalArgumentException();
        board = initial;
        Board twinBoard = board.twin();
        MinPQ<ManhattanBoard> queueBoard = new MinPQ<>();
        MinPQ<ManhattanBoard> queueTwinBoard = new MinPQ<>();
        queueBoard.insert(new ManhattanBoard(board));
        queueTwinBoard.insert(new ManhattanBoard(twinBoard));

        while (true) {
            ManhattanBoard topBoard = queueBoard.delMin();
            ManhattanBoard topTwinBoard = queueTwinBoard.delMin();


//            System.out.println("twin board" + topTwinBoard.board.toString());
            if (topBoard.board.isGoal() || topTwinBoard.board.isGoal()) {
                if (topBoard.board.isGoal()) {
                    isSolvable = true;
                    while (topBoard != null) {
                        solution.push(topBoard.board);
                        topBoard = topBoard.prev;
                    }
                }
                break;
            }
//            System.out.println("top board is :");
//            System.out.println(topBoard.board.toString());
            for (Board neightbor : topBoard.board.neighbors()) {
                if (topBoard.prev == null || !neightbor.equals(topBoard.prev.board)) {
                    queueBoard.insert(new ManhattanBoard(neightbor, topBoard));

//                    System.out.println("top board neighbor is :");
//                    System.out.println(neightbor.toString());
//                    System.out.println("neighbor manhaten :" + neightbor.manhattan());
//                    System.out.println("move :" + topBoard.moves);
                }
            }
            for (Board neightbor : topTwinBoard.board.neighbors()) {
                if (topTwinBoard.prev == null || !neightbor.equals(topTwinBoard.prev.board)) {
                    queueTwinBoard.insert(new ManhattanBoard(neightbor, topTwinBoard));
                }
            }
        }
        if (!isSolvable) solution = null;

    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return isSolvable;
    }            // is the initial board solvable?

    public int moves() {
        if(solution == null) return -1;
        return solution.size() - 1;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        return solution;
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
//        int[][] blocks = new int[][]{{1, 2, 3}, {0, 7, 6}, {5, 4, 8}};
//        Board initial = new Board(blocks);
//        Solver solver = new Solver(initial);
//        System.out.println("move is :" + solver.moves());
//        for (Board board : initial.neighbors())
//            StdOut.println(board);
        if (args == null) throw new IllegalArgumentException();
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        if(blocks == null) throw new IllegalArgumentException();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

//         print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }


    } // solve a slider puzzle (given below)
}
