package com.eincs.yammyfish.event;

import com.eincs.yammyfish.FishEater;
import com.eincs.yammyfish.FishManager;
import com.eincs.yammyfish.GameScreen;
import com.eincs.yammyfish.GameStartEvent;

public class GameEventManager {
    private FishEater fishEater;
    private FishManager fishManager;
    private GameScreen gameScreen;

    public GameEventManager(FishEater fishEater, FishManager fishManager, GameScreen gameScreen) {
        this.fishEater = fishEater;
        this.fishManager = fishManager;
        this.gameScreen = gameScreen;
    }

    public void throwEvent(GameSetEvent e) {
        fishManager.gameEnd();
        fishEater.gameLoad();
        gameScreen.reset();
    }

    public void throwEvent(GameStartEvent e) {
        if (e.getEventType() == GameEvent.GAME_START_1P) {
            fishManager.gameStart(FishEater.GAME_RUN_1P, e.getPlayer1Fish(), e.getPlayerFish2());
            fishEater.gameRun(FishEater.GAME_RUN_1P);
            gameScreen.gameStart(FishEater.GAME_RUN_1P);
        } else {
            fishManager.gameStart(FishEater.GAME_RUN_2P, e.getPlayer1Fish(), e.getPlayerFish2());
            fishEater.gameRun(FishEater.GAME_RUN_2P);
            gameScreen.gameStart(FishEater.GAME_RUN_2P);
        }
    }

    public void throwEvent(GameEndEvent e) {
        if (e.getEventType() == GameEvent.GAME_END_PLAYER1) {
            fishManager.gameEnd(GameEvent.GAME_END_PLAYER1);
            fishEater.gameEnd();
            gameScreen.gameEnd();
        } else if (e.getEventType() == GameEvent.GAME_END_PLAYER2) {
            fishManager.gameEnd(GameEvent.GAME_END_PLAYER2);
            fishEater.gameEnd();
            gameScreen.gameEnd();
        }
    }
}
