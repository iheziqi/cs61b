package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        // first consider which keystroke direction
        if (side.toString() == "NORTH") {
            this.board.setViewingPerspective(Side.NORTH);
        } else if (side.toString() == "SOUTH") {
            this.board.setViewingPerspective(Side.SOUTH);
        } else if (side.toString() == "WEST") {
            this.board.setViewingPerspective(Side.WEST);
        } else {
            this.board.setViewingPerspective(Side.EAST);
        }

        int size = this.board.size();
        boolean columnChanged;
        for (int c = 0; c < size; c++) {
            // get values of one column of the board
            columnArray = getOneColumnValues(this.board, c);
            // before the check, set the merge status to default
            setMergeStatusDefault();
            // before the check, set the score of every column to 0
            scoreOfCurrentColumn = 0;
            for (int i = 1; i < size; i++) {
                for (int j = 0; j < i; j++) {
                    columnChanged = tiltOneColumn(i, j, c, this.board);
                    if (columnChanged){
                        changed = true;
                        score += scoreOfCurrentColumn;
                        scoreOfCurrentColumn = 0;
                        break;
                    }
                }
            }
        }
        // change the direction back to north
        this.board.setViewingPerspective(Side.NORTH);


        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }


    public static int[] columnArray = new int[4];

    public static boolean[] mergeStatusTrack = {false, false, false, false};

    public static int scoreOfCurrentColumn = 0;

    public static void setMergeStatusDefault () {
        for (int i = 0; i < mergeStatusTrack.length; i++) {
            mergeStatusTrack[i] = false;
        }
    }

    /** Helper method one
     * get one column of the board per column at first time
    * */
    public static int[] getOneColumnValues(Board b, int columnIndex) {
        // use one array to store the values of one column
        int[] oneColumnOfBoard = new int[b.size()];
        for (int r = 0; r < b.size(); r++) {
            if (b.tile(columnIndex, r) == null) {
                oneColumnOfBoard[3 - r] = 0;
                continue;
            }
            oneColumnOfBoard[3 - r] = b.tile(columnIndex, r).value();
        }
       return oneColumnOfBoard; 
    }

    /** Helper method two
     * split the action "tilt one column" into multiple atom actions
     * 
     * */
    public static boolean tiltOneColumn(int rowIndexCurrent, int rowIndexCompare, int columnIndex, Board b) {
        int rowIndexOnBoard = 3 - rowIndexCurrent;
        int rowIndexOnBoardCompared = 3 - rowIndexCompare;
        Tile currentTile = b.tile(columnIndex, rowIndexOnBoard);
        Tile comparedTile = b.tile(columnIndex, rowIndexOnBoardCompared);

        if (currentTile == null) {
            return false;
        }

        if (columnArray[rowIndexCompare] == columnArray[rowIndexCurrent] && mergeStatusTrack[rowIndexCompare] == false) {
            b.move(columnIndex, rowIndexOnBoardCompared, currentTile);
            columnArray[rowIndexCompare] = columnArray[rowIndexCurrent] * 2;
            columnArray[rowIndexCurrent] = 0;
            scoreOfCurrentColumn = columnArray[rowIndexCompare];
            mergeStatusTrack[rowIndexCompare] = true;
            return true;
        }

        if (comparedTile == null) {
            b.move(columnIndex, rowIndexOnBoardCompared, currentTile);
            columnArray[rowIndexCompare] = columnArray[rowIndexCurrent];
            columnArray[rowIndexCurrent] = 0;
            return true;
        }

        return false;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // skip if there is no value at this spot
                if (b.tile(i, j) == null) {
                    continue;
                }
                if (b.tile(i, j).value() == MAX_PIECE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();

        // check whether there is at least one empty space on the board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (b.tile(i, j) == null) {
                    return true;
                }
            }
        }

        // check whether there are two adjacent tiles with the same value
        // check in row direction
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                int value = b.tile(i, j).value();
                int adjacentValue = b.tile(i, j + 1).value();
                if (value == adjacentValue) {
                    return true;
                }
            }
        }
        // check in column direction
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                int value = b.tile(j, i).value();
                int adjacentValue = b.tile(j + 1, i).value();
                if(value == adjacentValue) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
