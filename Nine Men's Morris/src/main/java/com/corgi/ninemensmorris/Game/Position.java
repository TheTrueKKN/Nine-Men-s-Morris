package com.corgi.ninemensmorris.Game;

import java.util.ArrayList;


/**
 *
 * This class represents a position on the board.
 *
 */
public class Position {
    private boolean hasToken;
    private final int x;
    private final int y;
    private Token occupyingToken;
    private ArrayList<Position> adjacentPositions;

    /**
     * Constructor for a position.
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasToken = false;
        this.occupyingToken = null;
        adjacentPositions = new ArrayList<>();
    }

    /**
     * Returns whether the position has a token or not.
     * @return True if the position has a token, false otherwise.
     */
    public boolean hasToken() {
        return hasToken;
    }

    /**
     * Returns the x coordinate of the position.
     * @return The x coordinate of the position.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the position.
     * @return The y coordinate of the position.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the token occupying the position.
     * @return The token occupying the position.
     */
    public Token getOccupyingToken() {
        return occupyingToken;
    }

    /**
     * Removes the token from the position.
     */
    public void removeToken() {
        this.hasToken = false;
        this.occupyingToken = null;
    }

    /**
     * Places a token on the position.
     * @param token The token to be placed on the position.
     */
    public void placeToken(Token token, Position position) {
        if (this.hasToken) {
            System.out.println("There is already a token on this position");
        } else {
            this.hasToken = true;
            this.occupyingToken = token;
            token.setPosition(position);
        }
    }

    /**
     * Returns all the positions adjacent to this position.
     * @return List of adjacent positions.
     */
    public ArrayList<Position> getAdjacentPositions() {
        return adjacentPositions;
    }


    /**
     * Adds a position to the list of adjacent positions.
     * @param pos The position to add.
     */
    public void addAdjacentPosition(Position pos) {
        if (!adjacentPositions.contains(pos)) {
            adjacentPositions.add(pos);
        } else {
            System.out.println("Position already adjacent");
        }
    }

    /**
     * Returns whether the given position is adjacent to this position.
     * @param position The position to check.
     * @return True if the given position is adjacent to this position, false otherwise.
     */
    public boolean isAdjacentTo(Position position) {
        return adjacentPositions.contains(position);
    }

    /**
     * toString method for the position.
     * @return x and y coordinates of the position.
     */
    public String toString() {
        return "Position: " + x + ", " + y;
    }
}
