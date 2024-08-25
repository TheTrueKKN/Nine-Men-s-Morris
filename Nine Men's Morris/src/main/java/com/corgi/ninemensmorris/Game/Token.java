package com.corgi.ninemensmorris.Game;

import com.corgi.ninemensmorris.Players.Player;

/**
 *
 * Token is the object that the Players can interact with on the board during the Game
 *
 */

public class Token {


    private Player owner;
    private Position position;

    /**
     * Constructor to create a new token
     * @param owner The Player that is creating the token
     * @param position The selected position to perform an action
     */
    public Token(Player owner, Position position) {
        this.owner = owner;
        this.position = position;
    }

    /**
     * Get the owner of the token
     * @return a Player object
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Method to retrive the current position of the selected token
     * @return A Position object of the selected token
     */
    public Position getPosition() { return position; }

    /**
     * A setter method to set the position of the selected token
     * @param position The selected Position on the board
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * A method to check if the token is movable
     * @return True if the token is movable, false otherwise
     */
    public boolean isMovable() {
        boolean movable = false;
        for (Position position: this.getPosition().getAdjacentPositions()) {
            if (!position.hasToken()) {
                movable = true;
                break;
            }
        }

        return movable;
    }
}
