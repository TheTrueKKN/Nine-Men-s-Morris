package com.corgi.ninemensmorris.Players;


import com.corgi.ninemensmorris.Actions.PositionFinder;
import com.corgi.ninemensmorris.BoardUI;
import com.corgi.ninemensmorris.Enum.Color;
import com.corgi.ninemensmorris.Enum.PlayerState;
import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;
import com.corgi.ninemensmorris.Game.Token;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * This class represents a human player.
 *
 */
public class AI extends Player {

    /**
     * Constructor for a human player.
     * @param tokenColor The color of the player.
     */
    public AI(Color tokenColor) {
        super(tokenColor);
    }

    /**
     * Returns a random token.
     * @return A random token.
     */
    public Token getRandomToken() {

        int tokenIndex = (int) (Math.random() * this.getTokenCount());

        for (Token token: this.getTokens()) {
            System.out.print(token.getPosition());
        }
        System.out.println();

        return this.getTokens().get(tokenIndex);
    }

    /**
     * Returns a random movable token.
     * @return A random movable token.
     */
    public Token getMovableToken() {
        ArrayList<Token> movableTokens = new ArrayList<>();

        for (Token token: this.getTokens()) {
            if (token.isMovable()) {
                movableTokens.add(token);
            }
        }

        int tokenIndex = (int) (Math.random() * movableTokens.size());

        System.out.println(movableTokens);

        return movableTokens.get(tokenIndex);
    }

    /**
     * Returns the position of the clicked position.
     * @param latch The latch to wait for the user to click a position.
     * @param board The board to check for possible positions.
     * @param board_UI The board UI to check for possible positions.
     * @return The position of the clicked position.
     */
    @Override
    public Position getClickedPosition(CountDownLatch latch, Board board, BoardUI board_UI) {

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PositionFinder positionFinder = PositionFinder.getInstance();
        ArrayList<Position> possiblePositions;

        if (this.getIsRemoving()){
            possiblePositions = positionFinder.getRemovablePos(board, this.getOpponent());
        }
        else if (this.getPlayerState() == PlayerState.PLACING) {
            possiblePositions = board.getEmptyPositions();
        }
        else if (!this.getHasSelectedToken()) {
            Token selectedToken = null;
            if (this.getPlayerState() == PlayerState.MOVING) {
                selectedToken =  getMovableToken();
            }
            else if (this.getPlayerState() == PlayerState.FLYING) {
                selectedToken = getRandomToken();
            }
            this.setSelectedToken(selectedToken);


            assert selectedToken != null;
            return selectedToken.getPosition();
        }
        else {
            possiblePositions = positionFinder.getPositions(board, this, this.getSelectedToken().getPosition());
        }

        int positionIndex = (int) (Math.random() * possiblePositions.size());

        return possiblePositions.get(positionIndex);
    }

}
