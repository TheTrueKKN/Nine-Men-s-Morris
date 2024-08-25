package com.corgi.ninemensmorris.Actions;

import com.corgi.ninemensmorris.Game.Token;
import com.corgi.ninemensmorris.Players.Player;
import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;

/**
 * This class represents a Place action.
 */
public class PlaceAction extends Action {

    public Player owner;
    public Position placeAt;


    /**
     * Constructor for a Place action.
     * @param owner The owner of the token to be placed.
     * @param placeAt The position at which the token is to be placed.
     */
    public PlaceAction(Player owner, Position placeAt) {
        this.owner = owner;
        this.placeAt = placeAt;
    }

    /**
     * Executes the place action.
     * @param board The board on which the action is executed.
     * @return True if the action was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Board board) {
        boolean success;
        success = isValid(board);
        if (success) {
            Token token = new Token(owner, placeAt);
            owner.addToken(token);
            board.placeToken(token, placeAt);
            owner.addTokenCount();
        }

        return success;
    }

    /**
     * Checks if the Place action is valid.
     * @param board The board on which the action is executed.
     * @return True if the action is valid, false otherwise.
     */
    @Override
    public boolean isValid(Board board) {
        boolean success;
        success = board.isPositionEmpty(placeAt);

        return success;
    }

    /**
     * Returns a description of the Place action.
     * @param success True if the action was executed successfully, false otherwise.
     * @return A description of the Place action.
     */
    @Override
    public String description(boolean success) {
        if (success) {
            return "Placed token at " + placeAt;
        } else {
            return "Invalid position";
        }
    }
}
