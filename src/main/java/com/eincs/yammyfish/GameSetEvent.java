package com.eincs.yammyfish;

public class GameSetEvent extends GameEvent {

    public GameSetEvent() {
        super("GameSetEvent");
    }

    @Override
    public int getEventType() {
        return GameEvent.GAME_SET_EVENT;
    }
}
