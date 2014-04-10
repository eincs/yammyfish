package com.eincs.yammyfish;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.Timer;

/*
 * 컴퓨터 물고기와 플레이어 물고기를 관리하는 물고기 매니저 클래스
 * 물고기와 관련된 모든 제어는 이 클래스를 통해 일어난다
 * 물고기의 인스턴스를 만드는 것도 이 클래스를 통해 일어난다.
 */
public class FishManager {

    // 물고기 리스폰과 관련된 상수 변수
    private static final int MAX_FISH = 20; // 최대로 만들어 질 수 있는 컴퓨터  물고기의 수.
    private static final int MIN_FISH = 10;
    private static final int RESPOWN_DELAY = 1000;
    private static final int RESPOWN_MAXVELOCITY = 2;

    // 물고기 리스폰 위치와 관련된 상수 변수
    private static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
    private static final int UP_RESPOWN = 0 + 75;
    private static final int DOWN_RESPOWN = SCREEN_HEIGHT - 75;
    private static final int RIGHT_RESPOWN = SCREEN_WIDTH + 100;
    private static final int LEFT_RESPOWN = 0 - 100;

    public static final Pos START_POINT1 = new Pos(600, 300);
    public static final Pos START_POINT2 = new Pos(200, 300);

    // 물고기 들의 정보를 저장하는 변수
    private Vector<Fish> fishList; // 물고기 객체를 저장할 벡터 자료구조
    private Player playerFish1;
    private Player playerFish2;

    // 컴퓨터 물고기들의 리스폰에 관여하는 변수
    private Random rand = new Random();
    private Timer respownTimer;

    private int curFishNumber;
    private int curLevel;
    // 현재 플레이어의 수를 저장하는 변수
    // 1p, 2p의 게임 모드에 따라 작동한다.

    // 게임 이벤트 처리를 위한 이벤트 매니저
    private GameEventManager eventManager;
    private int gameMode;

    public FishManager() {
        // 컴퓨터 물고기들을 저장할 벡터를 생성한다
        fishList = new Vector<Fish>();
        gameMode = FishEater.GAME_BEGINNING;
    }

    public void setEventManager(GameEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void gameStart(int gameMode, int fishType1, int fishType2) {
        playerFish1 = new Player(Player.PLAYER1, eventManager);
        playerFish2 = new Player(Player.PLAYER2, eventManager);

        fishList.clear();

        if (!(gameMode == FishEater.GAME_RUN_1P || gameMode == FishEater.GAME_RUN_2P)) {
            gameMode = FishEater.GAME_RUN_2P;
        }
        playerFish1.setFish(createPlayerFish(fishType1, START_POINT1));
        this.gameMode = FishEater.GAME_RUN_1P;
        if (gameMode == FishEater.GAME_RUN_2P) {
            playerFish2.setFish(createPlayerFish(fishType2, START_POINT2));
            this.gameMode = FishEater.GAME_RUN_2P;
        }
        // 물고기들의 리스폰에 관련된 변수를 지정한뒤 리스폰을 시작한다
        curFishNumber = 0;
        curLevel = 3;

        respownTimer = new Timer(RESPOWN_DELAY, new RespownListener());

        respownTimer.start();
    }

    public void gameEnd(int whoWin) {
        if (whoWin == GameEvent.GAME_END_PLAYER1) {
            playerFish1.setDone();
        } else {
            playerFish2.setDone();
        }
    }

    public void gameEnd() {
        playerFish1.setDone();
        playerFish2.setDone();
    }

    public Fish getPlayer1Fish() {
        return playerFish1.getFish();
    }

    public Fish getPlayer2Fish() {
        return playerFish2.getFish();
    }

    private void respownFish() {
        // 맵에 맞게 적당히 물고기 수를 조절하여 리스폰 하는 함수
        while (true) {
            if (curFishNumber >= MAX_FISH) {
                break;
            }
            createComFish();
            if (curFishNumber <= MIN_FISH || rand.nextInt() % 5 == 0) {
                continue;
            }
            break;
        }
    }

    public static Fish createPlayerFish(int fishType, Pos startPoint) {
        Fish fish;
        switch (fishType) {
        case Fish.VF_FOOTBALL:
            fish = new Football(startPoint);
            break;
        case Fish.VF_CHICKEN:
            fish = new Chicken(startPoint);
            break;
        case Fish.VF_PIRANHA:
            fish = new Piranha(startPoint);
            break;
        case Fish.VF_PICACHU:
            fish = new Pica(startPoint);
            break;
        default:
            fish = null;
        }
        return fish;
    }

    private void createComFish() {
        //위치 렌덤, 가속도, 속도, 물저항 렌덤으로 컴퓨터 물고기 생성

        // moveState info
        // Stop -1, Up 0, UpRight 1, Right 2,
        // DownRight 3, Down 4, DownLeft 5, Left 6, UpLeft 7
        int respownX = Math.abs(rand.nextInt()) % 2 == 0 ? LEFT_RESPOWN : RIGHT_RESPOWN;
        int respownY = Math.abs(rand.nextInt()) % (DOWN_RESPOWN - 2 * UP_RESPOWN) + UP_RESPOWN;
        int respownLevel = Math.abs(rand.nextInt()) % (curLevel + 2) + 1;
        int respownMaxVelocity = Math.abs(rand.nextInt()) % RESPOWN_MAXVELOCITY + 1;
        int respownAccelation = 1;
        int respownWaterResitance = 0;

        Fish fish = new NormalFish(respownX, respownY, respownMaxVelocity, respownAccelation, respownWaterResitance);
        fish.setLevel(respownLevel);
        if (respownX == RIGHT_RESPOWN) {
            fish.setMoveState(6);
        } else {
            fish.setMoveState(2);
        }

        fishList.add(fish);
        curFishNumber++;
    }

    public void allMove() {
        // 물고기들을 이동시키고 충돌확인하고 이벤트를 처리하는 메소드

        // 컴퓨터 물고기와 플레이어 물고기를 모두 논리이동시킨다
        for (int i = 0; i < curFishNumber; i++) {
            fishList.elementAt(i).move();
        }
        this.playerFish1.move();
        if (this.gameMode == FishEater.GAME_RUN_2P) {
            this.playerFish2.move();
        }

        // 컴퓨터 물고기가 화면 밖으로 나갔을 경우 remove한다.
        for (int i = 0; i < curFishNumber; i++) {
            if (fishList.elementAt(i).isOutOfbound()) {
                fishList.removeElementAt(i);
                curFishNumber--;
            }
        }

        // 플레이어 물고기와  컴퓨터 물고기가 충돌했는지 확인하고 충돌 할 경우
        // 먹을 수 있다면, 먹고, 먹을 수 없다면 먹지 않고, 먹혀야 한다면 먹힌다.
        allCollisionTest(playerFish1);
        if (gameMode == FishEater.GAME_RUN_2P) {
            allCollisionTest(playerFish2);
        }

        // 플레이어 물고기 끼리 충돌했는지 검사한뒤 적당한 이벤트를 발생한다
        // curLevel를 설정하여 리스폰 되는 컴퓨터 물고기의 레벨을 조정한다
        // 게임 진행 모드의 플레이어의 수에 따라 정해진다
        if (gameMode == FishEater.GAME_RUN_2P) {
            if (playerFish1.isCollition(playerFish2.getFish())) {
                if (playerFish1.isEatable(playerFish2)) {
                    playerFish1.eat(playerFish2.getFish());
                    playerFish2.eaten();
                } else if (playerFish2.isEatable(playerFish1)) {
                    playerFish2.eat(playerFish1.getFish());
                    playerFish1.eaten();
                }
            }
            curLevel = (playerFish1.getLevel() + playerFish2.getLevel()) / 2;
        } else {
            curLevel = playerFish1.getLevel();
        }
    }

    private void allCollisionTest(Player player) {
        // 플레이어 물고기와  컴퓨터 물고기가 충돌했는지 확인하고 충돌 할 경우
        // 먹을 수 있다면, 먹고, 먹을 수 없다면 먹지 않고, 먹혀야 한다면 먹힌다.
        for (int i = 0; i < curFishNumber; i++) {
            if (player.isCollition(fishList.elementAt(i))) {
                if (player.isEatable(fishList.elementAt(i))) {
                    player.eat(fishList.elementAt(i));
                    fishList.removeElementAt(i);
                    curFishNumber--;
                } else if (fishList.elementAt(i).isEatable(player.getFish())) {
                    player.eaten();
                }
            }
        }
    }

    public void allDraw(Graphics g) {
        // 물고기들을 모두 GameScreen에 그리는 메소드
        for (int i = 0; i < curFishNumber; i++) {
            fishList.elementAt(i).draw(g);
        }
        this.playerFish1.draw(g);
        if (gameMode == FishEater.GAME_RUN_2P) {
            return;
        }
        this.playerFish2.draw(g);
    }

    private class RespownListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // RESPOWN_DELAY 마다 컴퓨터 물고기를 리스폰한다.
            respownFish();
        }
    }
}
