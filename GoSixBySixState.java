public class GoSixBySixState {

    private static int EMPTY = 0;
    private static int WHITE = 1;
    private static int BLACK = 2;

    private int[][] board;

    private static int BOARD_LENGTH = 6;

    private int currentPlayer;

    public GoSixBySixState() {
        board = new int[BOARD_LENGTH][BOARD_LENGTH];
        currentPlayer = WHITE;
    }

    public boolean isValidMove(int row, int column) {
        return row < BOARD_LENGTH && column < BOARD_LENGTH && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column, int currentPlayer) {
        if (isValidMove(row, column)) {
            board[row][column] = currentPlayer;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder("  ");
        int currentSquare;
        
        for (int rowLabel = 0; rowLabel < BOARD_LENGTH; rowLabel++)
            boardString.append(rowLabel + " ");
        boardString.append("\n");

        for (int r = 0; r < BOARD_LENGTH; r++) {
            boardString.append(r + " ");
            for (int c = 0; c < BOARD_LENGTH; c++) {
                currentSquare = board[r][c];
                if (currentSquare == EMPTY) {
                    boardString.append("- ");
                }
                else if (board[r][c] == WHITE) {
                    boardString.append("W ");
                }
                else {
                    boardString.append("B ");
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    public static void main(String[] args) {
        GoSixBySixState state = new GoSixBySixState();
        System.out.println(state);
        state.makeMove(4, 3, 1);
        state.makeMove(5, 2, 2);
        System.out.println(state);
    }

}
