package com.corgi.ninemensmorris.Actions;

import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.MillDetector;
import com.corgi.ninemensmorris.Game.Position;
import com.corgi.ninemensmorris.Game.Token;
import com.corgi.ninemensmorris.Players.Player;

import java.util.ArrayList;


/**
 * This class represents a Remove action.
 */
public class RemoveAction extends Action{
    Player opponent;
    Position position;

    /**
     * Constructor for a Remove action.
     * @param opponent The opponent of the player.
     * @param position The position of the token to be removed.
     */
    public RemoveAction(Player opponent, Position position) {
        this.opponent = opponent;
        this.position = position;
    }

    /**
     * Executes the Remove action.
     * @param board The board on which the action is executed.
     * @return True if the action was executed successfully, false otherwise.
     */
    @Override
    public boolean execute(Board board) {
        boolean success;

        PositionFinder positionFinder = PositionFinder.getInstance();
        ArrayList<Position> positions = positionFinder.getRemovablePos(board, opponent);
        success = positions.contains(position);
        if (success) {
            Token tokenRemoved = position.getOccupyingToken();
            board.removeToken(position);
            opponent.removeToken(tokenRemoved);
        }

        return success;
    }

    /**
     * Checks if the Remove action is valid.
     * @param board The board on which the action is executed.
     * @return True if the action is valid, false otherwise.
     */
    @Override
    public boolean isValid(Board board) {
        boolean success;
        MillDetector millDetector = MillDetector.getInstance();

        success = !board.isPositionEmpty(position) && board.getToken(position).getOwner() == opponent &&
                !millDetector.isMill(position);

        return success;
    }

    // A function to verify if there is a token in that position regardless its in a mill or not

    /**
     * Checks if the Remove action is valid but this is also valid for tokens in mills
     * @param board The board on which the action is executed.
     * @return Returs a true values if the Remove action is valid , or else returns a false
     */
    public boolean includeMill(Board board){
        boolean pass;

        pass = !board.isPositionEmpty(position) && board.getToken(position).getOwner() == opponent;

        return pass;
    }

    /**
     * Returns a description of the Remove action.
     * @param success True if the action was executed successfully, false otherwise.
     * @return A description of the Remove action.
     */
    @Override
    public String description(boolean success) {
        if (success) {
            return "Remove token " + opponent.getTokenColor() + " from " + position;
        } else {
            return "Invalid move";
        }
    }
}
