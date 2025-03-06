public class GoSixBySixState {

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

    public boolean isValidMove(int row, int column) {
        return row < BOARD_LENGTH && column < BOARD_LENGTH && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column) {
        if (isValidMove(row, column)) {
            board[row][column] = currentPlayer;
            currentPlayer = currentPlayer == BLACK ? WHITE : BLACK;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder("    ");
        int currentSquare;

        for (int rowLabel = 0; rowLabel < BOARD_LENGTH; rowLabel++)
            boardString.append(rowLabel + "   ");
        boardString.append("\n");

        for (int r = 0; r < BOARD_LENGTH; r++) {
            boardString.append(r + " ");
            for (int c = 0; c < BOARD_LENGTH - 1; c++) {
                currentSquare = board[r][c];

                // boardString.append(" -- ");
                if (currentSquare == EMPTY) {
                    boardString.append("  -- ");
                } else {
                    boardString.append("  -- ");
                }
                if (currentSquare == BLACK) {
                    boardString.append("B");
                } else if (currentSquare == WHITE) {
                    boardString.append("W");
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
        state.makeMove(0, 0);
        // state.makeMove(5, 2, 2);
        System.out.println(state);
    }

}
