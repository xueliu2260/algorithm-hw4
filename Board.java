import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] board;
    private int manhattan = 0;
    private int updateManhattan = 0;

    public Board(int[][] blocks) {
        if(blocks == null) throw new IllegalArgumentException();
        this.board = copy(blocks);
        this.manhattan = calculateMan();
        this.updateManhattan = manhattan;
    }           // construct a board from an n-by-n array of blocks

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        return board.length;
    }                 // board dimension n

    public int hamming() {
        int hamming = 0;
        int row = board.length;
        int col = board.length;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == row - 1 && j == col - 1) continue;
                if (this.board[i][j] != (i) * col + j + 1) hamming += 1;
            }
        }
        return hamming;
    }                   // number of blocks out of place

    public int manhattan() {
        return updateManhattan;
    }                 // sum of Manhattan distances between blocks and goal

    private int calculateMan() {
        int row = board.length;
        int col = board.length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == i * row + j + 1 || board[i][j] == 0) continue;
//                System.out.println("cur row : " + i);
//                System.out.println("cur col : " + j);
//                System.out.println("cur val : " + board[i][j]);
                int goalRow = 0;
                int goalCol = 0;
                if (board[i][j] % row == 0) goalRow = board[i][j] / row - 1;
                else goalRow = board[i][j] / row;
                goalCol = (board[i][j] - 1) - row * goalRow;
//                System.out.println("goal row : " + goalRow);
//                System.out.println("goal col : " + goalCol);
                manhattan += Math.abs(i - goalRow) + Math.abs(j - goalCol);
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        if (manhattan() == 0) return true;
        return false;
    }                // is this board the goal board?

    public Board twin() {
        int row = board.length;
        int col = board.length;
        int[][] twinBoard = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                twinBoard[i][j] = board[i][j];
            }
        }
        if (board[0][0] != 0 && board[0][1] != 0) exchange(twinBoard, 0, 0, 0, 1);
        else if (board[0][0] != 0 && board[1][0] != 0) exchange(twinBoard, 0, 0, 1, 0);
        else exchange(twinBoard, 0, 1, 1, 0);

        return new Board(twinBoard);
    }                    // a board that is obtained by exchanging any pair of blocks

    private void exchange(int[][] newBoard, int newRow, int newCol, int curRow, int curCol) {
        int value = newBoard[newRow][newCol];
        newBoard[newRow][newCol] = newBoard[curRow][curCol];
        newBoard[curRow][curCol] = value;
    }

    public boolean equals(Object y) {
        if (y == null || !(y instanceof Board) || ((Board) y).board.length != board.length) return false;
        Board yBoard = (Board) y;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != yBoard.board[i][j]) return false;
            }
        }
        return true;
    }        // does this board equal y?

    private Board(int[][] b, int newRow, int newCol, int curRow, int curCol) {
        if(b == null) throw new IllegalArgumentException();
        this.board = copy(b);
        this.manhattan = calculateMan();
        this.updateManhattan = manhattan;
        int goalRow = 0;
        int goalCol = 0;
        if (board[newRow][newCol] % board.length == 0) goalRow = board[newRow][newCol] / board.length - 1;
        else goalRow = board[newRow][newCol] / board.length;
        goalCol = (board[newRow][newCol] - 1) - board.length * goalRow;
//                System.out.println("goal row : " + goalRow);
//                System.out.println("goal col : " + goalCol);
        int oldMan = Math.abs(newRow - goalRow) + Math.abs(newCol - goalCol);
        exchange(board, newRow, newCol, curRow, curCol);
        if (board[curRow][curCol] % board.length == 0) goalRow = board[curRow][curCol] / board.length - 1;
        else goalRow = board[curRow][curCol] / board.length;
        goalCol = (board[curRow][curCol] - 1) - board.length * goalRow;
//                System.out.println("goal row : " + goalRow);
//                System.out.println("goal col : " + goalCol);
        int newMan = Math.abs(curRow - goalRow) + Math.abs(curCol - goalCol);


        this.updateManhattan = this.updateManhattan - oldMan + newMan;


    }

    private int[][] copy(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        return copy;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<>();
        int blanki = 0;
        int blankj = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    blanki = i;
                    blankj = j;
                    break;
                }
            }
        }
        if (blanki > 0) queue.enqueue(new Board(board, blanki - 1, blankj, blanki, blankj));
        if (blanki < board.length - 1) queue.enqueue(new Board(board, blanki + 1, blankj, blanki, blankj));
        if (blankj > 0) queue.enqueue(new Board(board, blanki, blankj - 1, blanki, blankj));
        if (blankj < board.length - 1) queue.enqueue(new Board(board, blanki, blankj + 1, blanki, blankj));
        return queue;
    }     // all neighboring boards

    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(board.length);
        boardString.append("\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                boardString.append(String.format("% 4d", board[i][j]));
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }               // string representation of this board (in the output format specified below)

    public static void main(String[] args) {
        int[][] blocks = new int[][]{{0, 1, 2}, {4, 5, 3}, {7, 8, 6}};
        int[][] blocks3 = new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] blocks2 = new int[][]{{3, 1, 2, 9}, {4, 5, 0, 10}, {7, 8, 6, 11}, {12, 13, 14, 15}};
        Board initial = new Board(blocks);
//        Board initial2 = new Board(blocks2);
//        Board initial3 = new Board(blocks3);
        System.out.println("org man is : " + initial.manhattan());
//        System.out.println(initial.hamming());
//        System.out.println(initial2.hamming());
//        System.out.println(initial3.hamming());
//        for(Board b : initial.neighbors()){
//            System.out.println(b.toString());
//            System.out.println(b.hamming());
//        }
        for (Board b : initial.neighbors()) {
            System.out.println(b.toString());
            System.out.println("man is : " + b.manhattan());
        }
    } // unit tests (not graded)
}
