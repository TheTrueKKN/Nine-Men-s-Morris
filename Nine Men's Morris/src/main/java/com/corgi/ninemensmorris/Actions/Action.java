package com.corgi.ninemensmorris.Actions;

import com.corgi.ninemensmorris.Game.Board;

/**
 *
 * This abstract class for an action.
 *
 */
public abstract class Action {

    /**
     * Executes the action.
     * @param board The board on which the action is executed.
     * @return True if the action was executed successfully, false otherwise.
     */
    public abstract boolean execute(Board board);

    public abstract boolean isValid(Board board);

    /**
     * Returns a description of the action.
     * @param success True if the action was executed successfully, false otherwise.
     * @return A description of the action.
     */
    public abstract String description(boolean success);
}
