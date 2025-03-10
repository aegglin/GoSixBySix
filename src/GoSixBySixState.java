
public class GoSixBySixState implements Cloneable {

    private static int EMPTY = 0;
    private static int BLACK = 1;
    private static int WHITE = 2;

    private int[][] board;

    private static int BOARD_LENGTH = 6;


    private int currentPlayer;

    public GoSixBySixState() {
        board = new int[BOARD_LENGTH][BOARD_LENGTH];
        currentPlayer = BLACK;

    }

    public GoSixBySixState(int[][] board, int currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    public boolean isValidMove(int row, int column) {
        return row < BOARD_LENGTH && column < BOARD_LENGTH && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column, boolean isPass) {
        if (isValidMove(row, column)) {
            if (!isPass) {
                board[row][column] = currentPlayer;
            }
            currentPlayer = currentPlayer == BLACK ? WHITE : BLACK;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public GoSixBySixState clone() {

        int[][] newBoard = new int[BOARD_LENGTH][BOARD_LENGTH];
        for (int r = 0; r < BOARD_LENGTH; r++)
            for (int c = 0; c < BOARD_LENGTH; c++)
                newBoard[r][c] = board[r][c];

        GoSixBySixState newState = new GoSixBySixState(newBoard, currentPlayer);
        return newState;

    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        int currentSquare;

        for (int rowLabel = 0; rowLabel < BOARD_LENGTH; rowLabel++)
            boardString.append("  " + rowLabel + "  ");
        boardString.append("\n");

        for (int r = 0; r < BOARD_LENGTH; r++) {
            boardString.append(r + " ");
            for (int c = 0; c < BOARD_LENGTH - 1; c++) {
                currentSquare = board[r][c];

                if (currentSquare == BLACK) {
                    boardString.append("B");
                } else if (currentSquare == WHITE) {
                    boardString.append("W");
                }

                // boardString.append(" -- ");
                if (currentSquare == EMPTY) {
                    boardString.append("  -- ");
                } else {
                    boardString.append(" -- ");
                }
            }
            boardString.append("\n");

            if (r < BOARD_LENGTH - 1) {
                for (int c = 0; c < BOARD_LENGTH; c++) {
                    boardString.append("  |  ");
                }
                boardString.append("\n");
            }

        }
        return boardString.toString();
    }

    public static void main(String[] args) {
        GoSixBySixState state = new GoSixBySixState();
        System.out.println(state);
        state.makeMove(0, 0, false);
        System.out.println(state);
        state.makeMove(3, 3, false);
        System.out.println(state);
        state.makeMove(5, 2, false);
        System.out.println(state);
        state.makeMove(4, 1, false);
        System.out.println(state);
        state.makeMove(2, 2, false);
        System.out.println(state);
    }

}
