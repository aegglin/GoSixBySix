/*
 * TODO: Count territory
 *      Get board representation for saving the state and checking duplicates
 */

import java.util.ArrayList;

public class GoSixBySixState implements Cloneable {

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final double KOMI = 2.5; //roughly scaled from the 6.5 in the 19 x 19 version
    public static final int BOARD_SIZE = 6;

    private int[][] board;
    private int currentPlayer;
    private int numBlackPiecesCaptured;
    private int numWhitePiecesCaptured;
    private int passStreak;


    public GoSixBySixState() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = BLACK;
        passStreak = 0;
        numBlackPiecesCaptured = 0;
        numWhitePiecesCaptured = 0;
    }

    public GoSixBySixState(int[][] board, int currentPlayer, int passStreak, int numBlackPiecesCaptured, int numWhitePiecesCaptured) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.passStreak = passStreak;
        this.numBlackPiecesCaptured = numBlackPiecesCaptured;
        this.numWhitePiecesCaptured = numWhitePiecesCaptured;
    }

    public int countTerritory(int currentPlayer) {

        int oppositePlayer = currentPlayer == GoSixBySixState.BLACK ? GoSixBySixState.WHITE : GoSixBySixState.BLACK;

        int territory = 0;
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (getPiece(r, c) != oppositePlayer)
                    territory++;
            }
        }
        return territory;
    }

    public int getWinner() {
        //Must be doubles to compare with the komi
        double blackScore = countTerritory(GoSixBySixState.BLACK) - numBlackPiecesCaptured;
        double whiteScore = countTerritory(GoSixBySixState.WHITE) - numWhitePiecesCaptured + GoSixBySixState.KOMI;

        return blackScore > whiteScore ? GoSixBySixState.BLACK : GoSixBySixState.WHITE;
    }

    @Override
    public GoSixBySixState clone() {

        int[][] newBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int r = 0; r < BOARD_SIZE; r++)
            for (int c = 0; c < BOARD_SIZE; c++)
                newBoard[r][c] = board[r][c];

        GoSixBySixState newState = new GoSixBySixState(newBoard, currentPlayer, passStreak, numBlackPiecesCaptured, numWhitePiecesCaptured);
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

    public boolean isGameOver() {
        return passStreak == 3;
    }

    public boolean isValidMove(int row, int col) {
        return row < BOARD_SIZE && col < BOARD_SIZE && getPiece(row, col) == EMPTY;
    }

    public boolean makeMove(int row, int col, boolean isPass) {

        if (isPass) {
            passStreak++;
            return true;
        }
        if (isValidMove(row, col)) {
            if (passStreak != 0)
                passStreak = 0;

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
            numWhitePiecesCaptured++;
        } else {
            numBlackPiecesCaptured++;
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
