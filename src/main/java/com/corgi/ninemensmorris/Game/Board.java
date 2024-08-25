package com.corgi.ninemensmorris.Game;

import com.corgi.ninemensmorris.Players.Player;

import java.util.ArrayList;

/**
 *
 * This class represents the board for a game of Nine Men's Morris.
 *
 */
public class Board {
    private static Board instance = null;
    protected ArrayList<Token> tokens = new ArrayList<>();
    private final Position[][] positions = new Position[7][7];

    /**
     * Constructor for the board. Creates the board and populates
     * it with positions, then connects all adjacent positions.
     */
    private Board() {
        // Row 0
        positions[0][0] = new Position(0, 0);
        positions[0][3] = new Position(0, 3);
        positions[0][6] = new Position(0, 6);

        // Row 1
        positions[1][1] = new Position(1, 1);
        positions[1][3] = new Position(1, 3);
        positions[1][5] = new Position(1, 5);

        // Row 2
        positions[2][2] = new Position(2, 2);
        positions[2][3] = new Position(2, 3);
        positions[2][4] = new Position(2, 4);

        // Row 3
        positions[3][0] = new Position(3, 0);
        positions[3][1] = new Position(3, 1);
        positions[3][2] = new Position(3, 2);
        positions[3][4] = new Position(3, 4);
        positions[3][5] = new Position(3, 5);
        positions[3][6] = new Position(3, 6);

        // Row 4
        positions[4][2] = new Position(4, 2);
        positions[4][3] = new Position(4, 3);
        positions[4][4] = new Position(4, 4);

        // Row 5
        positions[5][1] = new Position(5, 1);
        positions[5][3] = new Position(5, 3);
        positions[5][5] = new Position(5, 5);

        // Row 6
        positions[6][0] = new Position(6, 0);
        positions[6][3] = new Position(6, 3);
        positions[6][6] = new Position(6, 6);

        // Connect adjacent positions
        connectPositions(positions[0][0], positions[0][3]);
        connectPositions(positions[0][0], positions[3][0]);
        connectPositions(positions[0][3], positions[0][6]);
        connectPositions(positions[0][3], positions[1][3]);
        connectPositions(positions[0][6], positions[3][6]);
        connectPositions(positions[1][1], positions[1][3]);
        connectPositions(positions[1][1], positions[3][1]);
        connectPositions(positions[1][3], positions[1][5]);
        connectPositions(positions[1][3], positions[2][3]);
        connectPositions(positions[1][5], positions[3][5]);
        connectPositions(positions[2][2], positions[2][3]);
        connectPositions(positions[2][2], positions[3][2]);
        connectPositions(positions[2][3], positions[2][4]);
        connectPositions(positions[2][4], positions[3][4]);
        connectPositions(positions[3][0], positions[3][1]);
        connectPositions(positions[3][0], positions[6][0]);
        connectPositions(positions[3][1], positions[3][2]);
        connectPositions(positions[3][1], positions[5][1]);
        connectPositions(positions[3][2], positions[4][2]);
        connectPositions(positions[3][4], positions[3][5]);
        connectPositions(positions[3][4], positions[4][4]);
        connectPositions(positions[3][5], positions[3][6]);
        connectPositions(positions[3][5], positions[5][5]);
        connectPositions(positions[3][6], positions[6][6]);
        connectPositions(positions[4][2], positions[4][3]);
        connectPositions(positions[4][3], positions[4][4]);
        connectPositions(positions[4][3], positions[5][3]);
        connectPositions(positions[5][1], positions[5][3]);
        connectPositions(positions[5][3], positions[5][5]);
        connectPositions(positions[5][3], positions[6][3]);
        connectPositions(positions[6][0], positions[6][3]);
        connectPositions(positions[6][3], positions[6][6]);
    }

    public Position[][] getPositions() {
        return positions;
    }


    /**
     * Gets the instance of the board. If the board has not been
     * created yet, it will create it.
     * @return The instance of the board.
     */
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    public void resetInstance(){
        instance = null;
    }

    /**
     * Gets the position at the given coordinates.
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return The position at the given coordinates.
     */
    public Position getPosition(int x, int y) {
        return positions[x][y];
    }

    /**
     * Get all the empty positions on the board.
     * @return An ArrayList of all the empty positions on the board.
     */
    public ArrayList<Position> getEmptyPositions() {
        ArrayList<Position> emptyPositions = new ArrayList<>();
        for (Position[] position : positions) {
            for (Position value : position) {
                if (value != null && !value.hasToken()) {
                    emptyPositions.add(value);
                }
            }
        }
        return emptyPositions;
    }

    /**
     * Get all the positions that are occupied by the given player.
     * @param player The player to get the positions for.
    * @return An ArrayList of all the positions that are occupied by the given player.
    */
    public ArrayList<Position> getOccupiedPositions(Player player) {
        ArrayList<Position> playerPositions = new ArrayList<>();
        for (Position[] position : positions) {
            for (Position value : position) {
                if (value != null && value.hasToken()) {
                    Token token = value.getOccupyingToken();
                        if (player.hasToken(token)) {
                        playerPositions.add(value);
                    }
                }
            }
        }
        return playerPositions;
    }

    /**
     * Get all the positions on the board.
     * @return An ArrayList of all the positions on the board.
     */
    public ArrayList<Position> getAllPositions() {
        ArrayList<Position> allPositions = new ArrayList<>();
        for (Position[] position : positions) {
            for (Position value : position) {
                if (value != null) {
                    allPositions.add(value);
                }
            }
        }
        return allPositions;
    }

    /**
     * Get the token at the given position.
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return The token at the given position.
     */
    public Token getToken(int x, int y) {
        return positions[x][y].getOccupyingToken();
    }

    /**
     * Gets the token at the given position.
     * @param position The position to get the token from.
     * @return The token at the given position.
     */
    public Token getToken(Position position) {
        return position.getOccupyingToken();
    }

    /**
     * Places a token at the given position.
     * @param token The token to place.
     * @param position The position to place the token at.
     */
    public void placeToken(Token token, Position position) {
        position.placeToken(token, position);
        tokens.add(token);
    }

    /**
     * Moves a token from one position to another.
     * @param token The token to move.
     * @param pos1 The position to move the token from.
     * @param pos2 The position to move the token to.
     * @return True if the token was moved, false otherwise.
     */
    public boolean moveToken(Token token, Position pos1, Position pos2) {
        if (pos1.getOccupyingToken() == token && isPositionEmpty(pos2)) {
            pos1.removeToken();
            pos2.placeToken(token, pos2);
            return true;
        }
        return false;
    }

    /**
     * Removes a token from the given position.
     * @param position The position to remove the token from.
     * @return True if the token was removed, false otherwise.
     */
    public boolean removeToken(Position position) {
        if (position.getOccupyingToken() != null) {
            position.removeToken();
            return true;
        }
        return false;
    }

    /**
     * Connects two positions together.
     * @param pos1 Position 1.
     * @param pos2 Position 2.
     */
    private void connectPositions(Position pos1, Position pos2) {
        pos1.addAdjacentPosition(pos2);
        pos2.addAdjacentPosition(pos1);
    }

    /**
     * Checks if the given position is empty.
     * @param position The position to check.
     * @return True if the position is empty, false otherwise.
     */
    public boolean isPositionEmpty(Position position) {
        return position.getOccupyingToken() == null;
    }

    /**
     * Prints the board to the console.
     */
    public void printBoard() {
        for (Position[] position : positions) {
            for (Position value : position) {
                if (value != null) {
                    System.out.print("0");
                } else {
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
}
