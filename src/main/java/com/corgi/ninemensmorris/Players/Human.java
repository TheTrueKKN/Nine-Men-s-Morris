package com.corgi.ninemensmorris.Players;


import com.corgi.ninemensmorris.BoardUI;
import com.corgi.ninemensmorris.Enum.Color;
import com.corgi.ninemensmorris.Game.Board;
import com.corgi.ninemensmorris.Game.Position;

import java.util.concurrent.CountDownLatch;

/**
 *
 * This class represents a human player.
 *
 */
public class Human extends Player {

    /**
     * Constructor for a human player.
     * @param tokenColor The color of the player.
     */
    public Human(Color tokenColor) {
        super(tokenColor);
    }

    /**
     * Returns the position clicked by the player.
     * @param latch The latch used to wait for the player to click.
     * @param board The board on which the action is executed.
     * @param board_ui The board UI.
     * @return The position clicked by the player.
     */
    @Override
    public Position getClickedPosition(CountDownLatch latch, Board board, BoardUI board_ui) {

        try {
            latch.await();

        } catch (InterruptedException e){
            e.printStackTrace();
        }

        int selectedCol = board_ui.getSelectedCol();
        int selectedRow = board_ui.getSelectedRow();

        return board.getPosition(selectedRow, selectedCol);
    }


}
