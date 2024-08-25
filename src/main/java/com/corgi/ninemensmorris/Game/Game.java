package com.corgi.ninemensmorris.Game;

import com.corgi.ninemensmorris.Actions.*;
import com.corgi.ninemensmorris.BoardUI;
import com.corgi.ninemensmorris.Enum.Color;
import com.corgi.ninemensmorris.Enum.PlayerState;
import com.corgi.ninemensmorris.Players.AI;
import com.corgi.ninemensmorris.Players.Human;
import com.corgi.ninemensmorris.Players.Player;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 *
 * This is the class to get the actual game started and determines the current phase of each Player
 *
 */

public class Game {

    private int turn;
    private Player playerRed;
    private Player playerYellow;
    Player currentPlayer;
    Player opponent;
    private final Board board;
    private final BoardUI board_ui;
    CountDownLatch latch;

    private static final String White = "#ffffff";
    private static final String Red = "#ff0000";
    private static final String Yellow = "#fffd00";
    private static final String LightBlue = "#68aafc";
    private static final String Green = "#00ff00";

    private static final int TUTORIAL = 0;
    private static final int PVE = 1;
    private static final int PVP = 2;

    /**
     * Constructor for a game.
     * @param board_ui The board UI.
     */
    public Game(BoardUI board_ui) {
        this.board_ui = board_ui;
        board = Board.getInstance();

        int gameMode = GameMode.getInstance().getGameMode();
        boolean startAsRed = GameMode.getInstance().getStartAsRed();

        if (gameMode == TUTORIAL){
            playerRed = new Human(Color.RED);
            playerYellow = new Human(Color.YELLOW);
            currentPlayer = playerRed;
        }
        else if (gameMode == PVE){
            if (startAsRed){
                playerRed = new Human(Color.RED);
                playerYellow = new AI(Color.YELLOW);
            } else{
                playerRed = new AI(Color.RED);
                playerYellow = new Human(Color.YELLOW);
            }
        }
        else if (gameMode == PVP){
            playerRed = new Human(Color.RED);
            playerYellow = new Human(Color.YELLOW);
        }

        playerRed.setOpponent(playerYellow);
        playerYellow.setOpponent(playerRed);
        turn = 0;
    }

    /**
     * Reset the game board and the mill detector instance.
     */
    public void resetBoard(){
        board.resetInstance();
        MillDetector.getInstance().resetInstance();
    }


    /**
     * Start the game in tutorial mode.
     */
    public void startTutorial(){
        System.out.println("Tutorial started");

        //Disable all positions
        for (int i = 0; i < board.getPositions().length; i++) {
            for (int j = 0; j < board.getPositions()[i].length; j++) {
                if (board.getPosition(i, j) != null){
                    board_ui.disablePosition(i, j);
                }
            }
        }

        //Welcome
        board_ui.updateTextBox("Welcome to tutorial mode. Click next to continue.");
        startLatch();
        //Start as red
        board_ui.updateTextBox("You will start as red! Click next to continue.");
        startLatch();

        //Placing phase, user places their first token
        board_ui.disableNextBtn();
        board_ui.updateTextBox("It is placing phase now, click on the highlighted position to place your token!");
        board_ui.highlightPosition(0, 0, Green);
        board_ui.enablePosition(0, 0);
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 0);
        //Update boardUI
        Position selectedPos = getClickedPosition();
        PlaceAction placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("You have successfully placed your first token! Click next to continue.");
        board_ui.enableNextBtn();
        startLatch();
        //Opponent places their token
        board_ui.updateTextBox("Now your opponent will place their own token. Click next to continue.");
        startLatch();
        selectedPos = board.getPosition(0, 6);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("Your opponent has placed their token at row 0 col 6. Each player must place " +
                "9 tokens at the start of the game. Click next to continue.");
        startLatch();
        board_ui.updateTextBox("Now let's talk about mill. Click next to continue.");
        startLatch();

        //Mill, Remove opponents token
        board_ui.updateTextBox("Mill is formed after aligning 3 same tokens. After forming mill, you can remove one opponent token that " +
                "is not a mill. Click next to continue.");
        startLatch();
        //Update board
        selectedPos = board.getPosition(3, 0);
        placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(3, 6);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        updateBoardUI();
        //Asking user to click on position 6, 0
        board_ui.updateTextBox("Here is an example. Click on the highlighted position to form a mill.");
        board_ui.disableNextBtn();
        board_ui.highlightPosition(6, 0, Green);
        board_ui.enablePosition(6, 0);
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(6, 0);
        //Update board
        selectedPos = board.getPosition(6, 0);
        placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("You have formed a mill, you can remove one of your opponents token that is not a mill. Click " +
                "on the highlighted position to remove a token.");
        //Highlight and enable yellow tokens/position
        board_ui.disableNextBtn();
        board_ui.highlightPosition(0, 6, Green);
        board_ui.highlightPosition(3, 6, Green);
        board_ui.enablePosition(0, 6);
        board_ui.enablePosition(3, 6);
        //Ask user to click on yellow tokens
        latch = new CountDownLatch(1);
        selectedPos = getClickedPosition();
        RemoveAction removeAction = new RemoveAction(playerYellow, selectedPos);
        removeAction.execute(board);
        updateBoardUI();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 6);
        board_ui.disablePosition(3, 6);
        board_ui.updateTextBox("You have removed the opponents token! Now lets move on to moving phase. Click " +
                "next to continue.");
        board_ui.enableNextBtn();

        //Moving phase
        board_ui.updateTextBox("After both player finishes placing their 9 tokens, they can now move their tokens. Click next to continue.");
        startLatch();
        //Update board
        selectedPos = board.getPosition(6, 3);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(3, 2);
        placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(5, 1);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(2, 3);
        placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(5, 3);
        placeAction = new PlaceAction(playerRed, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(5, 3);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(2, 4);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        selectedPos = board.getPosition(5, 5);
        placeAction = new PlaceAction(playerYellow, selectedPos);
        placeAction.execute(board);
        updateBoardUI();
        //Highlight position 0, 0, ask user to click on it
        board_ui.disableNextBtn();
        board_ui.highlightPosition(0, 0, Green);
        board_ui.enablePosition(0, 0);
        board_ui.updateTextBox("Here is an example. Let's move the highlighted token to the right! Click on the highlighted position.");
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 0);
        //Highlight position 3, 0, ask user to click on it
        board_ui.highlightPosition(0, 3, Green);
        board_ui.enablePosition(0, 3);
        board_ui.updateTextBox("Now click on the highlighted position to move your red token.");
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 3);
        //Move the red token
        MoveAction moveAction = new MoveAction(board.getPosition(0, 0).getOccupyingToken(), board.getPosition(0, 0), board.getPosition(0, 3));
        moveAction.execute(board);
        updateBoardUI();
        board_ui.enableNextBtn();
        board_ui.updateTextBox("You have successfully moved your token! Now let's move on to the flying phase. Click next to continue.");
        startLatch();

        //Flying phase
        board_ui.updateTextBox("You will enter the flying phase when you have 3 tokens left. Click next to continue.");
        startLatch();
        //Update board, remove red tokens and move yellow tokens
        selectedPos = board.getPosition(5, 3);
        removeAction = new RemoveAction(playerRed, selectedPos);
        removeAction.execute(board);
        selectedPos = board.getPosition(3, 2);
        removeAction = new RemoveAction(playerRed, selectedPos);
        removeAction.execute(board);
        selectedPos = board.getPosition(0, 3);
        removeAction = new RemoveAction(playerRed, selectedPos);
        removeAction.execute(board);
        moveAction = new MoveAction(board.getPosition(6, 3).getOccupyingToken(), board.getPosition(6, 3), board.getPosition(5, 3));
        moveAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("Here is an example. Your opponent has formed a mill and took your red token, and your " +
                "left with 3 tokens. Now you enter the flying phase. Click next to continue.");
        startLatch();
        board_ui.updateTextBox("Now you can fly your token to any empty position. Click on the highlighted position to select your token.");
        //Ask the user to click on highlighted red token
        board_ui.disableNextBtn();
        board_ui.highlightPosition(2, 3, Green);
        board_ui.enablePosition(2, 3);
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(2, 3);
        //Ask the user to click on the highlighted empty position
        board_ui.highlightPosition(0, 0, Green);
        board_ui.enablePosition(0, 0);
        board_ui.updateTextBox("Now click on the highlighted position to fly the token.");
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 0);
        //Fly the token
        FlyAction flyAction = new FlyAction(board.getPosition(2, 3).getOccupyingToken(), board.getPosition(2, 3), board.getPosition(0, 0));
        flyAction.execute(board);
        updateBoardUI();
        board_ui.enableNextBtn();
        board_ui.updateTextBox("You have successfully fly your token! The last lesson will be the end game. Click next to continue.");
        startLatch();

        //End game
        //Update the board
        flyAction = new FlyAction(board.getPosition(0, 0).getOccupyingToken(), board.getPosition(0, 0), board.getPosition(0, 3));
        flyAction.execute(board);
        moveAction = new MoveAction(board.getPosition(5, 1).getOccupyingToken(), board.getPosition(5, 1), board.getPosition(3, 1));
        moveAction.execute(board);
        selectedPos = board.getPosition(3, 1);
        removeAction = new RemoveAction(playerYellow, selectedPos);
        removeAction.execute(board);
        selectedPos = board.getPosition(5, 3);
        removeAction = new RemoveAction(playerYellow, selectedPos);
        removeAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("Here is an example. Player loses when they have 2 tokens or when they have no valid moves. Click next to continue.");
        startLatch();
        board_ui.disableNextBtn();
        board_ui.updateTextBox("It is currently your turn and you can win by forming a mill and removing their tokens. Click on " +
                "the highlighted token to move to your left.");
        //Ask the user to select the highlighted token
        board_ui.highlightPosition(0, 3, Green);
        board_ui.enablePosition(0, 3);
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 3);
        //Ask the user to select the empty position 0, 0
        board_ui.highlightPosition(0, 0, Green);
        board_ui.enablePosition(0, 0);
        board_ui.updateTextBox("Now, click on the highlighted empty position to form a mill.");
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(0, 0);
        //Update the red token
        flyAction = new FlyAction(board.getPosition(0, 3).getOccupyingToken(), board.getPosition(0, 3), board.getPosition(0, 0));
        flyAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("Mill has formed. Now lets remove the opponent piece at row 2, column 4");
        //Ask the user to click on the opponent token
        board_ui.highlightPosition(2, 4, Green);
        board_ui.enablePosition(2, 4);
        startLatch();
        board_ui.unhighlightAllPositions();
        board_ui.disablePosition(2, 4);
        //Remove the token and update the board. Show the winner at the end.
        selectedPos = board.getPosition(2, 4);
        removeAction = new RemoveAction(playerYellow, selectedPos);
        removeAction.execute(board);
        updateBoardUI();
        board_ui.updateTextBox("Since the opponent have less than 3 pieces left, you have won the game! You can click " +
                "Return to Main Menu on the left to go back to the main page.");
    }

    /**
     * Starts the game.
     */
    public void start(){
        MillDetector millDetector = MillDetector.getInstance();
        PositionFinder positionFinder = PositionFinder.getInstance();
        System.out.println("Game started");

        ArrayList<Position> highlightedPos;

        while (gameActive()) {
            // Initialize local variables
            boolean moveMade = false;
            Position moveMadePos = null;
            PlayerState currentPhase;

            //Initialize CountDownLatch
            latch = new CountDownLatch(1);

            //Update UI
            String turnColor;

            // Determine whose turn it is
            if (turn % 2 == 0) {
                currentPlayer = playerRed;
                opponent = playerYellow;

                currentPhase = currentPlayer.getPlayerState();
                turnColor = Red;
                System.out.println("Red's turn");

            } else {
                currentPlayer = playerYellow;
                opponent = playerRed;

                currentPhase = currentPlayer.getPlayerState();
                turnColor = Yellow;
                System.out.println("Yellow's turn");
            }
            currentPlayer.setRemoving(false);
            currentPlayer.setHasSelectedToken(false);
            currentPlayer.setSelectedToken(null);

            // Update UI with current phase
            String phaseText = "";
            if (currentPhase.equals(PlayerState.PLACING)){
                phaseText = "Place a token";
            } else if (currentPhase.equals(PlayerState.MOVING)){
                phaseText = "Move a token";
            } else if (currentPhase.equals(PlayerState.FLYING)){
                phaseText = "Fly a token";
            }

            //Update UI Components
            board_ui.updateTurnCircle(turnColor);
            board_ui.updateTextBox(phaseText);

            board_ui.updateRedPiecesLeft(9 - playerRed.getTokensPlaced());
            board_ui.updateYellowPiecesLeft(9 - playerYellow.getTokensPlaced());

            // PLACING PHASE =====================================================
            Token selectedToken;
            if (currentPhase == PlayerState.PLACING) {
                System.out.println("Select a position to place your token");

                Position selectedPos = getClickedPosition();

                PlaceAction placeAction = new PlaceAction(currentPlayer, selectedPos);
                moveMade = placeAction.execute(board);
                moveMadePos = selectedPos;

                // MOVING PHASE =====================================================
            } else if (currentPhase == PlayerState.MOVING) {
                System.out.println("Select a token to move");

                Position selectedPos1 = getClickedPosition();



                selectedToken = selectedPos1.getOccupyingToken();
                if (selectedToken == null || selectedToken.getOwner() != currentPlayer) {
                    System.out.println("Please select your token");
                    System.out.println("Selected token at " + selectedPos1);
                    continue;
                } else {
                    board_ui.unhighlightAllPositions();

                    //Highlight circle in the UI
                    board_ui.highlightPosition(selectedPos1.getX(), selectedPos1.getY(), LightBlue);
                    highlightedPos = positionFinder.getPositions(board, currentPlayer, selectedPos1);
                    if (board_ui.enableHint()) {
                        for (Position pos : highlightedPos) {
                            board_ui.highlightPosition(pos.getX(), pos.getY(), Green);
                        }
                    }

                    System.out.println("Selected token at " + selectedPos1);
                }

                currentPlayer.setHasSelectedToken(true);
                latch = new CountDownLatch(1);

                Position selectedPos2 = getClickedPosition();

                MoveAction moveAction = new MoveAction(selectedToken, selectedPos1, selectedPos2);
                moveMade = moveAction.execute(board);
                moveMadePos = selectedPos2;

                //Unhighlight circle in the UI
                board_ui.unhighlightPosition(selectedPos1.getX(), selectedPos1.getY());
                board_ui.unhighlightAllPositions();

                // FLYING PHASE =====================================================
            } else if (currentPhase == PlayerState.FLYING) {
                System.out.println("Select a token to fly");

                Position selectedPos1 = getClickedPosition();

                selectedToken = selectedPos1.getOccupyingToken();
                if (selectedToken == null || selectedToken.getOwner() != currentPlayer) {
                    System.out.println("Please select your token");
                    System.out.println("Selected token at " + selectedPos1);
                    continue;
                } else {
                    board_ui.unhighlightAllPositions();

                    //Highlight circle in the UI
                    board_ui.highlightPosition(selectedPos1.getX(), selectedPos1.getY(), LightBlue);
                    highlightedPos = positionFinder.getPositions(board, currentPlayer, selectedPos1);
                    if (board_ui.enableHint()) {
                        for (Position pos : highlightedPos) {
                            board_ui.highlightPosition(pos.getX(), pos.getY(), Green);
                        }
                    }
                    System.out.println("Selected token at " + selectedPos1);
                }
                currentPlayer.setHasSelectedToken(true);
                latch = new CountDownLatch(1);

                Position selectedPos2 = getClickedPosition();


                FlyAction flyAction = new FlyAction(selectedToken, selectedPos1, selectedPos2);
                moveMade = flyAction.execute(board);
                moveMadePos = selectedPos2;

                //Unhighlight circle in the UI
                board_ui.unhighlightPosition(selectedPos1.getX(), selectedPos1.getY());
                board_ui.unhighlightAllPositions();
            }

            updatePlayerState();

            // Update the board UI
            updateBoardUI();

            // If a move was made, check if a mill was formed
            if (moveMade) {
                System.out.println("token placed/moved at " + moveMadePos);
                if (millDetector.isMill(moveMadePos)) {
                    System.out.println("Mill formed");
                    currentPlayer.setRemoving(true);

                    // highlight all opponent tokens
                    highlightedPos = positionFinder.getRemovablePos(board, opponent);
                    if (board_ui.enableHint()) {
                        for (Position pos : highlightedPos) {
                            board_ui.highlightPosition(pos.getX(), pos.getY(), Green);
                        }
                    }

                    boolean removeMade = false;

                    while (!removeMade) {
                        System.out.println("Select a token to remove");
                        board_ui.updateTextBox("Remove a token");
                        latch = new CountDownLatch(1);
                        Position selectedPos = getClickedPosition();
                        RemoveAction removeAction = new RemoveAction(opponent, selectedPos);
                        removeMade = removeAction.execute(board);
                    }

                    // Unhighlight all positions
                    board_ui.unhighlightAllPositions();

                    // Update the board UI
                    updatePlayerState();
                    updateBoardUI();
                }
                turn++;
                System.out.println("Player Red tokens: " + playerRed.getTokenCount() + " Player Yellow tokens: " + playerYellow.getTokenCount());
            }
            System.out.println("Red state: " + playerRed.getPlayerState());
            System.out.println("Yellow state: " + playerYellow.getPlayerState());
        }
    }


    /**
     * Updates the UI to reflect the board state.
     */
    private void updateBoardUI(){
        for (int i = 0; i < board.getPositions().length; i++) {
            for (int j = 0; j < board.getPositions()[i].length; j++) {
                if (board.getPosition(i, j) != null){
                    Token token = board.getPosition(i, j).getOccupyingToken();
                    if (token == null){
                        board_ui.updatePositionFill(i, j, White);
                    }
                    else if (token.getOwner().getTokenColor().equals(Color.RED)){
                        board_ui.updatePositionFill(i, j, Red);
                    }
                    else if (token.getOwner().getTokenColor().equals(Color.YELLOW)){
                        board_ui.updatePositionFill(i, j, Yellow);
                    }
                }
            }
        }
    }

    /**
     * Gets the position that the user clicks on.
     */
    public void positionSelected(){
        // If a latch exists, count down by 1 to release the blocked thread
        if (latch != null) {
            latch.countDown();
        }
    }


    /**
     * Checks if the game is active.
     * @return true if the game is active, false otherwise.
     */
    public boolean gameActive() {
        boolean active = true;

        if (playerRed.getPlayerState() != PlayerState.PLACING)
            if (playerRed.getTokenCount() < 3) {
                System.out.println("Game ended, Yellow wins");
                board_ui.showWinner("YELLOW WON");
                active = false;
            } else if (playerYellow.getPlayerState() != PlayerState.PLACING)
                if (playerYellow.getTokenCount() < 3) {
                    System.out.println("Game ended, Red wins");
                    board_ui.showWinner("RED WON");
                    active = false;
                }

        if (playerRed.cannotMove()) {
            System.out.println("Game ended, Yellow wins");
            board_ui.showWinner("YELLOW WON");
            active = false;
        } else if (playerYellow.cannotMove()) {
            System.out.println("Game ended, Red wins");
            board_ui.showWinner("RED WON");
            active = false;
        }

        return active;
    }

    /**
     * Updates Red and Yellow player states.
     */
    public void updatePlayerState() {
        playerRed.updateSelfState();
        playerYellow.updateSelfState();
    }

    /**
     * Gets the position that the user clicks on.
     * @return the position that the user clicks on.
     */
    private Position getClickedPosition(){

        return currentPlayer.getClickedPosition(latch, board, board_ui);
    }

    /**
     * Starts the latch.
     */
    private void startLatch(){
        //Wait for the user to click on a button
        latch = new CountDownLatch(1);

        try {
            latch.await();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
