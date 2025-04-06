/*
 * TODO:    
 *          -Make the colors an enum
 * 
 */

import java.util.ArrayList;

public class GoSixBySixAtariState implements Cloneable {

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int BOARD_SIZE = 6;

    //TODO: make this player names and uncomment the int array?
    private String[] players = {"Empty", "Black", "White"};
    // private  int[] players = {EMPTY, WHITE, BLACK};

    private int[][] board;
    private int currentPlayer;

    private boolean blackPieceCaptured;
    private boolean whitePieceCaptured;

    public GoSixBySixAtariState() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = BLACK;

        blackPieceCaptured = false;
        whitePieceCaptured = false;
    }

    public GoSixBySixAtariState(int[][] board, int currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;

        blackPieceCaptured = false;
        whitePieceCaptured = false;
    }

    public ArrayList<int[]> getEmptySquares() {

        ArrayList<int[]> emptySquares = new ArrayList<>();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (getPiece(r, c) == GoSixBySixAtariState.EMPTY) {
                    emptySquares.add(new int[] {r, c});
                }
            }
        }
        return emptySquares;
    }

    public int getWinner() {
        if (blackPieceCaptured) {
            return GoSixBySixAtariState.WHITE;
        } else if (whitePieceCaptured) {
            return GoSixBySixAtariState.BLACK;
        }

        return 0;
    }

    @Override
    public GoSixBySixAtariState clone() {

        int[][] newBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++)
            for (int c = 0; c < BOARD_SIZE; c++)
                newBoard[r][c] = board[r][c];

        GoSixBySixAtariState newState = new GoSixBySixAtariState(newBoard, currentPlayer);
        return newState;

    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private ArrayList<int[]> getGroupCoords(int row, int col, int currentPlayer, boolean[][] visitedSquares,
            ArrayList<int[]> groupCoords) {
        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE)
            return null;
        if (board[row][col] != currentPlayer)
            return null;
        if (visitedSquares[row][col])
            return null;

        visitedSquares[row][col] = true;
        int[] currentCoords = { row, col };
        groupCoords.add(currentCoords);

        if (row - 1 >= 0 && board[row - 1][col] == currentPlayer) {
            getGroupCoords(row - 1, col, currentPlayer, visitedSquares, groupCoords);
        }
        if (col - 1 >= 0 && board[row][col - 1] == currentPlayer) {
            getGroupCoords(row, col - 1, currentPlayer, visitedSquares, groupCoords);
        }
        if (row + 1 < BOARD_SIZE && board[row + 1][col] == currentPlayer) {
            getGroupCoords(row + 1, col, currentPlayer, visitedSquares, groupCoords);
        }
        if (col + 1 < BOARD_SIZE && board[row][col + 1] == currentPlayer) {
            getGroupCoords(row, col + 1, currentPlayer, visitedSquares, groupCoords);
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

    private int getGroupSize(int row, int col, int currentPlayer, boolean[][] visitedSquares) {

        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE)
            return 0;
        if (board[row][col] != currentPlayer)
            return 0;
        if (visitedSquares[row][col])
            return 0;

        visitedSquares[row][col] = true;
        int groupAmount = 1;

        if (row - 1 >= 0 && board[row - 1][col] == currentPlayer) {
            groupAmount += getGroupSize(row - 1, col, currentPlayer, visitedSquares);
        }
        if (col - 1 >= 0 && board[row][col - 1] == currentPlayer) {
            groupAmount += getGroupSize(row, col - 1, currentPlayer, visitedSquares);
        }
        if (row + 1 < BOARD_SIZE && board[row + 1][col] == currentPlayer) {
            groupAmount += getGroupSize(row + 1, col, currentPlayer, visitedSquares);
        }
        if (col + 1 < BOARD_SIZE && board[row][col + 1] == currentPlayer) {
            groupAmount += getGroupSize(row, col + 1, currentPlayer, visitedSquares);
        }

        return groupAmount;
    }

    private int getNumberOfLiberties(int row, int col) {
        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;

        if (!(row - 1 < 0) && board[row - 1][col] == EMPTY)
            top++;
        if (!(row + 1 >= BOARD_SIZE) && board[row + 1][col] == EMPTY)
            bottom++;
        if (!(col - 1 < 0) && board[row][col - 1] == EMPTY)
            left++;
        if (!(col + 1 >= BOARD_SIZE) && board[row][col + 1] == EMPTY)
            right++;

        return top + bottom + left + right;
    }

    public int getPiece(int row, int col) {
        return board[row][col];
    }

    public boolean isValidMove(int row, int col) {
        return row < BOARD_SIZE && col < BOARD_SIZE && getPiece(row, col) == EMPTY;
    }

    public boolean makeMove(int row, int col) {

        if (isValidMove(row, col)) {

            setStone(row, col);

            int oppositePlayer = currentPlayer == BLACK ? WHITE : BLACK;
            boolean[][] visitedSquares;
            ArrayList<int[]> blankGroupCoords;

            if (row - 1 >= 0) {
                int topLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> topGroupCoords = getGroupCoords(row - 1, col, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (topGroupCoords != null) {
                    topLiberties = getGroupNumberOfLiberties(topGroupCoords);
                    if (topLiberties == 0) {
                        for (int[] coord : topGroupCoords) {
                            removeStone(coord[0], coord[1], currentPlayer);
                        }
                    }
                }
            }
            if (row + 1 < BOARD_SIZE) {
                int bottomLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();

                ArrayList<int[]> bottomGroupCoords = getGroupCoords(row + 1, col, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (bottomGroupCoords != null) {
                    bottomLiberties = getGroupNumberOfLiberties(bottomGroupCoords);
                    if (bottomLiberties == 0) {
                        for (int[] coord : bottomGroupCoords) {
                            removeStone(coord[0], coord[1], currentPlayer);
                        }
                    }
                }

            }
            if (col - 1 >= 0) {
                int leftLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> leftGroupCoords = getGroupCoords(row, col - 1, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (leftGroupCoords != null) {
                    leftLiberties = getGroupNumberOfLiberties(leftGroupCoords);
                    if (leftLiberties == 0) {
                        for (int[] coord : leftGroupCoords) {
                            removeStone(coord[0], coord[1], currentPlayer);
                        }
                    }
                }

            }
            if (col + 1 < BOARD_SIZE) {
                int rightLiberties = 0;
                visitedSquares = new boolean[BOARD_SIZE][BOARD_SIZE];
                blankGroupCoords = new ArrayList<>();
                ArrayList<int[]> rightGroupCoords = getGroupCoords(row, col + 1, oppositePlayer, visitedSquares,
                        blankGroupCoords);

                if (rightGroupCoords != null) {
                    rightLiberties = getGroupNumberOfLiberties(rightGroupCoords);
                    if (rightLiberties == 0) {
                        for (int[] coord : rightGroupCoords) {
                            removeStone(coord[0], coord[1], currentPlayer);
                        }
                    }
                }
            }
            //switch players
            currentPlayer = currentPlayer == BLACK ? WHITE : BLACK;
            return true;
        } else {
            return false;
        }
        
    }

    private void removeStone(int row, int col, int currentPlayer) {
        board[row][col] = EMPTY;

        if (currentPlayer == BLACK) {
            whitePieceCaptured = true;
        } else {
            blackPieceCaptured = true;
        }

    }
    
    private void setStone(int row, int col) {
        board[row][col] = currentPlayer;
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


    public static void main(String[] args) {
        GoSixBySixAtariState state = new GoSixBySixAtariState();

        state.makeMove(0, 0); // black
        state.makeMove(3, 3); // white
        state.makeMove(2, 3); // black
        state.makeMove(0, 1); // white
        state.makeMove(3, 2); // black
        state.makeMove(3, 4); // white
        state.makeMove(1, 1); // black
        state.makeMove(5, 0); // white
        state.makeMove(4, 4); // black
        state.makeMove(3, 1); // white
        state.makeMove(4, 3); // black
        state.makeMove(0, 2); // white
        state.makeMove(3, 5); // black
        state.makeMove(1, 0); // white
        state.makeMove(2, 4); // black
        System.out.println(state);
    }
}
