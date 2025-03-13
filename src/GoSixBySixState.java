
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

    // TODO: can this be done with recursion? Must a better way of doing this
    private int getNumberOfLiberties(int r, int c) {

        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;

        if (!(r - 1 < 0) && board[r - 1][c] == EMPTY)
            top++;
        if (!(r + 1 >= BOARD_LENGTH) && board[r + 1][c] == EMPTY)
            bottom++;
        if (!(c - 1 < 0) && board[r][c - 1] == EMPTY)
            left++;
        if (!(c + 1 >= BOARD_LENGTH) && board[r][c + 1] == EMPTY)
            right++;

        return top + bottom + left + right;
    }

    private int getGroupSize(int r, int c, int currentPlayer) {
        if (r < 0 || c < 0 || r >= BOARD_LENGTH || c >= BOARD_LENGTH) {
            return 0;
        }

        int groupAmount = 0;

        if (board[r][c] == currentPlayer) {
            groupAmount = 1;
        }

        int aboveStones = getGroupSize(r - 1, c, currentPlayer);
        int belowStones = getGroupSize(r + 1, c, currentPlayer);
        int leftStones = getGroupSize(r, c - 1, currentPlayer);
        int rightStones = getGroupSize(r, c + 1, currentPlayer);

        return groupAmount + aboveStones + belowStones + leftStones + rightStones;
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
        state.makeMove(0, 0, false); // black
        System.out.println(state);
        state.makeMove(3, 3, false); // white
        System.out.println(state);
        state.makeMove(5, 2, false); // black
        System.out.println(state);
        state.makeMove(4, 1, false); // white
        System.out.println(state);
        state.makeMove(2, 2, false); // black
        System.out.println(state);

        System.out.println("Number of liberties at (3, 3): " + state.getNumberOfLiberties(3, 3));
        System.out.println("Number of liberties at (0, 0): " + state.getNumberOfLiberties(0, 0));
        System.out.println("Number of liberties at (3, 2): " + state.getNumberOfLiberties(3, 2));
        System.out.println("Number of liberties at (2, 1): " + state.getNumberOfLiberties(2, 1));

        state.makeMove(3, 4, false); // white
        System.out.println(state);
        state.makeMove(1, 2, false); // black
        System.out.println(state);
        state.makeMove(4, 0, false); // white
        System.out.println(state);
        state.makeMove(5, 3, false); // black
        System.out.println(state);
        state.makeMove(5, 0, false); // white
        System.out.println(state);
        state.makeMove(1, 3, false); // black
        System.out.println(state);
        state.makeMove(3, 0, false); // white
        System.out.println(state);
        state.makeMove(5, 4, false); // black
        System.out.println(state);

        System.out.println(state.getGroupSize(4, 0, WHITE));
        // System.out.println(state.getGroupSize(4, 0, BLACK));
    }
}
