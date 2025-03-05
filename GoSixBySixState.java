public class GoSixBySixState {

    private static int EMPTY = 0;
    private static int WHITE = 1;
    private static int BLACK = 2;

    private int[][] board;

    private static int MAX_LENGTH = 6;

    private int currentPlayer;

    public GoSixBySixState() {
        board = new int[MAX_LENGTH][MAX_LENGTH];

        currentPlayer = WHITE;
    }

    public boolean isValidMove(int row, int column) {
        return row < MAX_LENGTH && column < MAX_LENGTH && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column, int currentPlayer) {
        if (isValidMove(row, column)) {
            board[row][column] = currentPlayer;
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        GoSixBySixState gsbss = new GoSixBySixState();
        System.out.println("hi Go");
    }

}
