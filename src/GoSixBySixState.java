
public class GoSixBySixState implements Cloneable {

    public static int EMPTY = 0;
    public static int BLACK = 1;
    public static int WHITE = 2;

    private int[][] board;

    public static int BOARD_SIZE = 6;

    private int currentPlayer;
    private int passStreak;

    public GoSixBySixState() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = BLACK;
        passStreak = 0;
    }

    public GoSixBySixState(int[][] board, int currentPlayer, int passStreak) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.passStreak = passStreak;
    }

    public boolean isValidMove(int row, int column) {
        return row < BOARD_SIZE && column < BOARD_SIZE && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column, boolean isPass) {
        if (isPass){
            passStreak++;
            return true;
        }
        if (isValidMove(row, column)) {
            if (passStreak > 0) {
                passStreak = 0;
            }
            board[row][column] = currentPlayer;
            currentPlayer = currentPlayer == BLACK ? WHITE : BLACK;
            return true;
        } else {
            return false;
        }
    }

    public int getPiece(int row, int col) {
        return board[row][col];
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private int getNumberOfLiberties(int r, int c) {

        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;

        if (!(r - 1 < 0) && board[r - 1][c] == EMPTY)
            top++;
        if (!(r + 1 >= BOARD_SIZE) && board[r + 1][c] == EMPTY)
            bottom++;
        if (!(c - 1 < 0) && board[r][c - 1] == EMPTY)
            left++;
        if (!(c + 1 >= BOARD_SIZE) && board[r][c + 1] == EMPTY)
            right++;

        return top + bottom + left + right;
    }

    private int getGroupSize(int r, int c, int currentPlayer, boolean[][] visitedSquares) {

        if (r < 0 || c < 0 || r >= BOARD_SIZE || c >= BOARD_SIZE) return 0;
        if (board[r][c] != currentPlayer) return 0;
        if (visitedSquares[r][c]) return 0;

        visitedSquares[r][c] = true;
        int groupAmount = 1;

        if (r - 1 >= 0 && board[r - 1][c] == currentPlayer) groupAmount += getGroupSize(r - 1, c, currentPlayer, visitedSquares);
        if (c - 1 >= 0 && board[r][c - 1] == currentPlayer) groupAmount += getGroupSize(r, c - 1, currentPlayer, visitedSquares); 
        if (r + 1 < BOARD_SIZE && board[r + 1][c] == currentPlayer) groupAmount += getGroupSize(r + 1, c, currentPlayer, visitedSquares);
        if (c + 1 < BOARD_SIZE && board[r][c + 1] == currentPlayer) groupAmount += getGroupSize(r, c + 1, currentPlayer, visitedSquares);
 
        return groupAmount;
    }

    @Override
    public GoSixBySixState clone() {

        int[][] newBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++)
            for (int c = 0; c < BOARD_SIZE; c++)
                newBoard[r][c] = board[r][c];

        GoSixBySixState newState = new GoSixBySixState(newBoard, currentPlayer);
        return newState;

    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        int currentSquare;

        for (int rowLabel = 0; rowLabel < BOARD_SIZE; rowLabel++)
            boardString.append("  " + rowLabel + "  ");
        boardString.append("\n");

        for (int r = 0; r < BOARD_SIZE; r++) {
            boardString.append(r + " ");
            for (int c = 0; c < BOARD_SIZE - 1; c++) {
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

            if (r < BOARD_SIZE - 1) {
                for (int c = 0; c < BOARD_SIZE; c++) {
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

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        System.out.println(state.getGroupSize(4, 0, WHITE, visited));

        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        System.out.println(state.getGroupSize(4, 0, BLACK, visited));

        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        System.out.println(state.getGroupSize(5, 3, BLACK, visited));

        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        System.out.println(state.getGroupSize(1, 2, BLACK, visited));

        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        System.out.println(state.getGroupSize(1, 2, WHITE, visited));
    }
}
