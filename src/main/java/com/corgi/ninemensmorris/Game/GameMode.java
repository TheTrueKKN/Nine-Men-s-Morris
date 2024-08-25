package com.corgi.ninemensmorris.Game;

public class GameMode {
    private static GameMode instance = null;
    private int gameMode;
    private boolean startAsRed;

    private GameMode(){

    }

    public static GameMode getInstance() {
        if (instance == null) {
            instance = new GameMode();
        }

        return instance;
    }

    public void setGameMode(int mode){
        gameMode = mode;
    }

    public int getGameMode(){
        return gameMode;
    }

    public void setStartAsRed(boolean start){
        startAsRed = start;
    }

    public boolean getStartAsRed(){
        return startAsRed;
    }
}
