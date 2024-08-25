package com.corgi.ninemensmorris.Actions;

import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;
import com.corgi.ninemensmorris.Game.Token;

/**
 *
 * This class represent the Fly Action
 *
 */

public class FlyAction extends Action {
    private final Token token;
    private final Position moveToPosition;
    private final Position initialPosition;

    /**
     * Constructor to create a Fly Action
     * @param token The token to apply the Fly Action on
     * @param initialPosition The initial position of the selected token
     * @param moveToPosition The selected position for the token to be moved to
     */
    public FlyAction(Token token, Position initialPosition, Position moveToPosition) {
        this.token = token;
        this.moveToPosition = moveToPosition;
        this.initialPosition = initialPosition;
    }

    /**
     * Method to execute the Fly action
     * @param board The board on which the action is executed.
     * @return a boolean to indicate if the action is executed on the board
     */
    @Override
    public boolean execute(Board board) {
        boolean success;
        success = isValid(board);
        if (success) {
            board.moveToken(token, initialPosition, moveToPosition);
        }

        return success;
    }

    /**
     * Checks if the Fly action is valid
     * @param board The board on which the action is executed.
     * @return a boolean to indicate if the action is valid on the board
     */
    @Override
    public boolean isValid(Board board) {
        boolean success;
        success = board.isPositionEmpty(moveToPosition);

        return success;
    }

    /**
     * Returns a description after the Player attempts to 'fly' the token
     * @param success True if the action was executed successfully, false otherwise.
     * @return A string description of the Fly action
     */
    @Override
    public String description(boolean success) {
        if (success) {
            return "Move (fly) token " + token.getOwner().getTokenColor() + " from " + initialPosition + " to " + moveToPosition;
        } else {
            return "Invalid move";
        }
    }
}
