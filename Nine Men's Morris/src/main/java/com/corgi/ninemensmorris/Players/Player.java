package com.corgi.ninemensmorris.Players;

import com.corgi.ninemensmorris.BoardUI;
import com.corgi.ninemensmorris.Enum.Color;
import com.corgi.ninemensmorris.Enum.PlayerState;
import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;
import com.corgi.ninemensmorris.Game.Token;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 *
 * This class represents a player.
 *
 */
public abstract class Player {

    private final Color tokenColor;
    private final ArrayList<Token> tokens;
    private PlayerState playerState;
    private int tokensPlaced;
    private int tokenCount;

    private boolean isRemoving;
    private boolean hasSelectedToken;
    private Token selectedToken;

    private Player opponent;



    /**
     * Constructor for a player.
     * @param tokenColor The color of the player.
     */
    public Player(Color tokenColor) {
        this.tokenColor = tokenColor;
        tokens = new ArrayList<>();
        playerState = PlayerState.PLACING;
        tokensPlaced = 0;
        tokenCount = 0;
    }

    /**
     * Returns the current token left for the player.
     * @return The tokenCount of player.
     */
    public int getTokenCount() {
        return tokenCount;
    }

    /**
     * Returns the color of the player.
     * @return The color of the player.
     */
    public Color getTokenColor() {
        return tokenColor;
    }

    /**
     * Returns the amount of token placed on the board.
     * @return The amount of tokens placed.
     */
    public int getTokensPlaced() { return tokensPlaced; }

    /**
     * Adds a token to the player's tokens.
     */
    public void addToken(Token token) {
        if (token == null) {
            throw new NullPointerException("Unable to add a null token!");
        }
        tokens.add(token);
        tokensPlaced++;
    }

    /**
     * Removes a token from the player's tokens.
     */
    public void removeToken(Token token) {
        if (token == null) {
            throw new NullPointerException("Unable to remove a null token!");
        }
        tokens.remove(token);
        tokenCount--;
    }


    /**
     * Adds one to the token count.
     */
    public void addTokenCount() {
        tokenCount++;
    }

    /**
     * To check if there are still any valid moves left ,and will end the game if there are no more valid moves
     * @return a boolean to check if there are any valid moves left
     */

    public boolean cannotMove() {
        if (playerState == PlayerState.PLACING) {
            return false;
        }

        boolean hasMovesLeft = false;
        for (Token token: tokens) {
            for (Position position: token.getPosition().getAdjacentPositions()) {
                if (!position.hasToken()) {
                    hasMovesLeft = true;
                    break;
                }
            }
        }
        if (!hasMovesLeft) {
            System.out.println("Game ended, " + tokenColor.getLabel() + " loses");
        }
        return !hasMovesLeft;
    }


    /**
     * Checks if the player has a token with the given token.
     * @param token The token to check.
     */
    public boolean hasToken(Token token) {
        if (token == null) {
            throw new NullPointerException("Unable to check for a null token!");
        }
        return tokens.contains(token);
    }

    /**
     * Updates the player's state
     */
    public void updateSelfState() {
        if (playerState == PlayerState.PLACING && tokensPlaced == 9) {
            playerState = PlayerState.MOVING;
        } else if (playerState == PlayerState.MOVING && tokenCount == 3) {
            playerState = PlayerState.FLYING;
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * Returns the player's state.
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    public abstract Position getClickedPosition(CountDownLatch latch, Board board, BoardUI board_UI);

    public boolean getIsRemoving() {
        return isRemoving;
    }

    public void setRemoving(boolean removing) {
        isRemoving = removing;
    }

    public boolean getHasSelectedToken() {
        return hasSelectedToken;
    }

    public void setHasSelectedToken(boolean selectedToken) {
        this.hasSelectedToken = selectedToken;
    }

    public Token getSelectedToken() {
        return selectedToken;
    }

    public void setSelectedToken(Token selectedToken) {
        this.selectedToken = selectedToken;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
}



