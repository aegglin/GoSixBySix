import java.util.ArrayList;
import java.util.Arrays;

public class GoSixBySixState implements Cloneable {

    public static int EMPTY = 0;
    public static int BLACK = 1;
    public static int WHITE = 2;

    private int[][] board;

    public static int BOARD_SIZE = 6;

    private int currentPlayer;

    public GoSixBySixState() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = BLACK;
    }

    public GoSixBySixState(int[][] board, int currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
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

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private ArrayList<int[]> getGroupCoords(int r, int c, int currentPlayer, boolean[][] visitedSquares,
            ArrayList<int[]> groupCoords) {
        if (r < 0 || c < 0 || r >= BOARD_SIZE || c >= BOARD_SIZE)
            return null;
        if (board[r][c] != currentPlayer)
            return null;
        if (visitedSquares[r][c])
            return null;

        visitedSquares[r][c] = true;
        int[] currentCoords = { r, c };
        groupCoords.add(currentCoords);

        if (r - 1 >= 0 && board[r - 1][c] == currentPlayer) {
            getGroupCoords(r - 1, c, currentPlayer, visitedSquares, groupCoords);
        }
        if (c - 1 >= 0 && board[r][c - 1] == currentPlayer) {
            getGroupCoords(r, c - 1, currentPlayer, visitedSquares, groupCoords);
        }
        if (r + 1 < BOARD_SIZE && board[r + 1][c] == currentPlayer) {
            getGroupCoords(r + 1, c, currentPlayer, visitedSquares, groupCoords);
        }
        if (c + 1 < BOARD_SIZE && board[r][c + 1] == currentPlayer) {
            getGroupCoords(r, c + 1, currentPlayer, visitedSquares, groupCoords);
        }

        return groupCoords;

    }

    private int getGroupNumberOfLiberties(ArrayList<int[]> groupCoords) {

        int totalLiberties = 0;
        for (int[] coord : groupCoords) {
            totalLiberties += getNumberOfLiberties(coord[0], coord[1]);
        }
        return totalLiberties;
    }

    private int getGroupSize(int r, int c, int currentPlayer, boolean[][] visitedSquares) {

        if (r < 0 || c < 0 || r >= BOARD_SIZE || c >= BOARD_SIZE)
            return 0;
        if (board[r][c] != currentPlayer)
            return 0;
        if (visitedSquares[r][c])
            return 0;

        visitedSquares[r][c] = true;
        int groupAmount = 1;

        if (r - 1 >= 0 && board[r - 1][c] == currentPlayer) {
            groupAmount += getGroupSize(r - 1, c, currentPlayer, visitedSquares);
        }
        if (c - 1 >= 0 && board[r][c - 1] == currentPlayer) {
            groupAmount += getGroupSize(r, c - 1, currentPlayer, visitedSquares);
        }
        if (r + 1 < BOARD_SIZE && board[r + 1][c] == currentPlayer) {
            groupAmount += getGroupSize(r + 1, c, currentPlayer, visitedSquares);
        }
        if (c + 1 < BOARD_SIZE && board[r][c + 1] == currentPlayer) {
            groupAmount += getGroupSize(r, c + 1, currentPlayer, visitedSquares);
        }

        return groupAmount;
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

    public int getPiece(int row, int col) {
        return board[row][col];
    }

    public boolean isValidMove(int row, int column) {
        return row < BOARD_SIZE && column < BOARD_SIZE && board[row][column] == EMPTY;
    }

    public boolean makeMove(int row, int column, boolean isPass) {
        if (isValidMove(row, column)) {
            if (!isPass) {
                board[row][column] = currentPlayer;
            }

            int oppositePlayer = currentPlayer == BLACK ? WHITE : BLACK;
            boolean[][] visitedSquares;
            ArrayList<int[]> blankGroupCoords;

            if (row - 1 >= 0) {
                int topLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> topGroupCoords = getGroupCoords(row - 1, column, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (topGroupCoords != null) {
                    topLiberties = getGroupNumberOfLiberties(topGroupCoords);
                    if (topLiberties == 0) {
                        for (int[] coord : topGroupCoords) {
                            removeStone(coord[0], coord[1]);
                        }
                    }
                }
            }
            if (row + 1 < BOARD_SIZE) {
                int bottomLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();

                ArrayList<int[]> bottomGroupCoords = getGroupCoords(row + 1, column, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (bottomGroupCoords != null) {
                    bottomLiberties = getGroupNumberOfLiberties(bottomGroupCoords);
                    if (bottomLiberties == 0) {
                        for (int[] coord : bottomGroupCoords) {
                            removeStone(coord[0], coord[1]);
                        }
                    }
                }

            }
            if (column - 1 >= 0) {
                int leftLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> leftGroupCoords = getGroupCoords(row, column - 1, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (leftGroupCoords != null) {
                    leftLiberties = getGroupNumberOfLiberties(leftGroupCoords);
                    if (leftLiberties == 0) {
                        for (int[] coord : leftGroupCoords) {
                            removeStone(coord[0], coord[1]);
                        }
                    }
                }

            }
            if (column + 1 < BOARD_SIZE) {
                int rightLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> rightGroupCoords = getGroupCoords(row, column + 1, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (rightGroupCoords != null) {
                    rightLiberties = getGroupNumberOfLiberties(rightGroupCoords);
                    if (rightLiberties == 0) {
                        for (int[] coord : rightGroupCoords) {
                            removeStone(coord[0], coord[1]);
                        }
                    }
                }
            }

            currentPlayer = currentPlayer == BLACK ? WHITE : BLACK;
            return true;
        } else {
            return false;
        }
    }

    private void removeStone(int r, int c) {
        board[r][c] = EMPTY;
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
            for (int c = 0; c < BOARD_SIZE; c++) {
                currentSquare = board[r][c];

                if (currentSquare == BLACK) {
                    boardString.append("B");
                } else if (currentSquare == WHITE) {
                    boardString.append("W");
                }

                if (c < BOARD_SIZE - 1) {
                    if (currentSquare == EMPTY) {
                        boardString.append("  -- ");
                    } else {
                        boardString.append(" -- ");
                    }
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

    // Nothing in the last column shows up in the toString

    public static void main(String[] args) {
        GoSixBySixState state = new GoSixBySixState();
        state.makeMove(0, 5, false);
        state.makeMove(1, 5, false);
        state.makeMove(2, 5, false);
        state.makeMove(3, 5, false);
        state.makeMove(4, 5, false);
        state.makeMove(5, 5, false);
        state.makeMove(6, 5, false); // should return false

        // state.makeMove(0, 0, false); // black
        // state.makeMove(3, 3, false); // white
        // state.makeMove(2, 3, false); // black
        // state.makeMove(0, 1, false); // white
        // state.makeMove(3, 2, false); // black
        // state.makeMove(3, 4, false); // white
        // state.makeMove(1, 1, false); // black
        // state.makeMove(5, 0, false); // white
        // state.makeMove(4, 4, false); // black
        // state.makeMove(3, 1, false); // white
        // state.makeMove(4, 3, false); // black
        // state.makeMove(0, 2, false); // white
        // state.makeMove(3, 5, false); // black
        // state.makeMove(1, 0, false); // white
        // state.makeMove(2, 4, false); // black
        System.out.println(state);

    }
}
