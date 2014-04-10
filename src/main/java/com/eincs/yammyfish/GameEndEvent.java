package com.eincs.yammyfish;

public class GameEndEvent extends GameEvent {

    private int whoWin;

    public GameEndEvent(int whoWin) {
        super("GameEnd");
        this.whoWin = whoWin;
        if (!(whoWin == GameEvent.GAME_END_DRAW || whoWin == GameEvent.GAME_END_PLAYER1) || whoWin == GameEvent.GAME_END_PLAYER2) {
            whoWin = GameEvent.GAME_END_DRAW;
        }
    }

    @Override
    public int getEventType() {
        return whoWin;
    }
}
