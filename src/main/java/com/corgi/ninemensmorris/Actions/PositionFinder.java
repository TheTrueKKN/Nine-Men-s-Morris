package com.corgi.ninemensmorris.Actions;

import com.corgi.ninemensmorris.Enum.PlayerState;
import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;
import com.corgi.ninemensmorris.Game.Token;
import com.corgi.ninemensmorris.Players.Player;

import java.util.ArrayList;

/**
 *
 * Singleton class to find all possible position to place a token.
 *
 */
public class PositionFinder {
    private static PositionFinder instance = null;
    private ArrayList<Position> positions = new ArrayList<>();

    /**
     * Constructor for the action list.
     */
    private PositionFinder() {
    }

    /**
     * Returns the instance of the PositionFinder.
     * @return The instance of the PositionFinder.
     */
    public static PositionFinder getInstance() {
        if (instance == null) {
            instance = new PositionFinder();
        }
        return instance;
    }

    public ArrayList<Position> getRemovablePos(Board board, Player player) {
        positions = new ArrayList<>();
        RemoveAction removeAction;

        for (Position pos : board.getOccupiedPositions(player)) {

            removeAction = new RemoveAction(player, pos);
            if (removeAction.isValid(board)) {
                positions.add(pos);
            }
        }


        // Check if position list is empty, if it's empty meaning only have mill on the board
        // Then reinsert all the occupied place of the opponent's tokens
        if(positions.isEmpty()){
            for (Position pos : board.getOccupiedPositions(player)){
                removeAction = new RemoveAction(player, pos);
                if (removeAction.includeMill(board)){
                    positions.add((pos));
                }
            }
        }

        if (positions.isEmpty()){
            System.out.println("No removable token");
        }

        return positions;
    }

    /**
     * Returns a list of all clickable positions for a player.
     * @param board The board to check for possible positions.
     * @return A list of all possible positions to place a token.
     */
    public ArrayList<Position> getPositions(Board board, Player player, Position position) {
        PlayerState playerState = player.getPlayerState();

        positions = new ArrayList<>();
        Token token;

        switch (playerState) {
            case PLACING:
                throw new IllegalArgumentException("Cannot get positions for placing a token with a position.");
            case MOVING:
                for (Position pos : board.getEmptyPositions()) {
                    token = board.getToken(position);

                    MoveAction moveAction = new MoveAction(token, position, pos);
                    if (moveAction.isValid(board)) {
                        positions.add(pos);
                    }
                }
                break;
            case FLYING:
                for (Position pos : board.getEmptyPositions()) {
                    token = board.getToken(position);

                    FlyAction flyAction = new FlyAction(token, position, pos);
                    if (flyAction.isValid(board)) {
                        positions.add(pos);
                    }
                }
                break;
        }
        return positions;
    }
}
