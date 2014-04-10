package com.eincs.yammyfish;

public abstract class GameEvent {

    public static final int FISH_EAT = 0;

    public static final int GAME_END_DRAW = 1;
    public static final int GAME_END_PLAYER1 = 2;
    public static final int GAME_END_PLAYER2 = 3;

    public static final int GAME_START_1P = 4;
    public static final int GAME_START_2P = 5;

    public static final int GAME_SET_EVENT = 6;

    private String message;

    public GameEvent(String str) {
        message = str;
    }

    public String getMassage() {
        return message;
    }

    public abstract int getEventType();
}
