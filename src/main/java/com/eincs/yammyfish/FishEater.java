package com.eincs.yammyfish;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import com.eincs.yammyfish.event.GameEvent;
import com.eincs.yammyfish.event.GameEventManager;
import com.eincs.yammyfish.event.GameSetEvent;
import com.eincs.yammyfish.fish.Fish;

public class FishEater extends JFrame {

    private static final long serialVersionUID = 1L;

    // 게임의 동작에 필요한 상수들
    private static final int GAME_SPEED = 30;
    private static final int DELAY = 1000 / GAME_SPEED;
    private static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;

    // GameState에 들어갈 상수 들
    public static final int GAME_BEGINNING = 0;
    public static final int GAME_RUN_1P = 1; // 이후에  혼자서 하기 모드도 구현예정
    public static final int GAME_RUN_2P = 2;
    public static final int GAME_END = 3;

    private Timer gameRunTimer; // 게임 RUN에 필요한 타이머
    private Random rand = new Random();

    private GameScreen gameScreen; // 게임 화면을 출력할 스크린
    private FishManager fishes; // 물고기를 처리할 매니저 클래스
    private GameEventManager eventManager;

    private int gameState; // 현재 게임상태를 나타내는 변수

    //private AudioClip music;
    //private File file;

    public FishEater() {
        super("Yammy Fish");

        /*
         * URL url = null;
         * file = new File("music\\back.wav");
         * try {
         * url = new URL("file", "localhost", file.getPath());
         * }
         * catch(Exception e) {}
         * music = JApplet.newAudioClip(url);
         * music.loop();
         */
        // frame의 특성을 설정
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // frame에 GameScreen과 메뉴를  추가
        fishes = new FishManager();
        gameScreen = new GameScreen(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        gameScreen.setManager(fishes);
        this.getContentPane().add(gameScreen);
        this.setJMenuBar(new GameMenuBar());
        this.pack();

        eventManager = new GameEventManager(this, fishes, gameScreen);

        fishes.setEventManager(eventManager);
        gameScreen.setEventManager(eventManager);

        gameRunTimer = new Timer(DELAY, new GameRunTimer());
        gameLoad();

        // frame의 위치를 화면의 정 가운데에 놓기
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        int frameX = (int) (scrSize.getWidth() - frameSize.getWidth()) / 2;
        int frameY = (int) (scrSize.getHeight() - frameSize.getHeight()) / 2;
        this.setLocation(frameX, frameY);

        this.setVisible(true);
    }

    public void gameLoad() {
        gameRunTimer.restart();
        gameState = GAME_BEGINNING;
    }

    public void gameRun(int gameMode) {
        gameState = gameMode;
        this.addKeyListener(new FishKeyListener(fishes.getPlayer1Fish(), KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_ENTER));
        if (gameState == GAME_RUN_2P) {
            this.addKeyListener(new FishKeyListener(fishes.getPlayer2Fish(), KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_Z));
        }
        gameRunTimer.start();
    }

    public void gameEnd() {
        gameState = GAME_END;
    }

    private class GameRunTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameState == GAME_RUN_1P || gameState == GAME_RUN_2P || gameState == GAME_END) {
                fishes.allMove();
                gameScreen.repaint();
            }
        }
    }

    public class FishKeyListener extends KeyAdapter {
        // 플레이어 물고기를 키보드로 조정할수 있게 해주는 KeyListener

        private int[] keySettings = new int[5]; // 컨트롤 키를 저장할 배열
        private int keyState = 0x0000; // 눌려진 키의 상태를 저장
        private Fish controlFish; // 이 리스너로 컨트롤할 물고기를 저장

        /*
         * KeyState는 16진수 형태롤 저장한다
         * 네자리의 16진수중, MSB부터 LSB까지 차례대로
         * F가 저장될 경우: Up, Right, Down, Left 키가 눌려진 상태이고,
         * 0이 저장될 경우: Up, Right, Down, Left 키가 눌려지지 않은 상태이다.
         * 비트연산자를 이용하여 각 경우의 KeyState를 설정한다
         */

        public FishKeyListener(Fish controlUnit, int keyUp, int keyRight, int keyDown, int keyLeft, int keyAbility) {
            // 컨트롤 할 물고기와 그 키 값을 받아 설정한다
            controlFish = controlUnit;

            keySettings[0] = keyUp;
            keySettings[1] = keyRight;
            keySettings[2] = keyDown;
            keySettings[3] = keyLeft;
            keySettings[4] = keyAbility;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int pressedKey = e.getKeyCode();
            if (pressedKey == keySettings[0]) { // Up
                keyState = keyState | 0xF000;
            } else if (pressedKey == keySettings[1]) { // Right
                keyState = keyState | 0x0F00;
            } else if (pressedKey == keySettings[2]) { // Down
                keyState = keyState | 0x00F0;
            } else if (pressedKey == keySettings[3]) { // Left
                keyState = keyState | 0x000F;
            } else if (pressedKey == keySettings[4]) { // Ability
                // Not yes implemented
            }
            setMoveState();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int pressedKey = e.getKeyCode();
            if (pressedKey == keySettings[0]) { // Up
                keyState = keyState & 0x0FFF;
            } else if (pressedKey == keySettings[1]) { // Right
                keyState = keyState & 0xF0FF;
            } else if (pressedKey == keySettings[2]) { // Down
                keyState = keyState & 0xFF0F;
            } else if (pressedKey == keySettings[3]) { // Left
                keyState = keyState & 0xFFF0;
            } else if (pressedKey == keySettings[4]) { // Ability
                // Not yes implemented
            }
            setMoveState();
        }

        private void setMoveState() { // 현재 키 상태에 따라 controlFish의 MoveState를 설정한다
            // moveState info
            // Stop -1, Up 0, UpRight 1, Right 2,
            // DownRight 3, Down 4, DownLeft 5, Left 6, UpLeft 7
            switch (keyState) {
            case 0x0000:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0xF000:
                controlFish.setMoveState(0); // Up
                break;
            case 0x0F00:
                controlFish.setMoveState(2); // Right
                break;
            case 0x00F0:
                controlFish.setMoveState(4); // Down
                break;
            case 0x000F:
                controlFish.setMoveState(6); // Left
                break;
            case 0xFF00:
                controlFish.setMoveState(1); // UpRight
                break;
            case 0xF00F:
                controlFish.setMoveState(7); // UpLeft
                break;
            case 0xF0F0:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0x0FF0:
                controlFish.setMoveState(3); // downRight
                break;
            case 0x00FF:
                controlFish.setMoveState(5); // downLeft
                break;
            case 0x0F0F:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0xFFF0:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0xFF0F:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0xF0FF:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0x0FFF:
                controlFish.setMoveState(-1); // Stop
                break;
            case 0xFFFF:
                controlFish.setMoveState(-1); // Stop
                break;
            }
        }
    }

    public class GameMenuBar extends JMenuBar {

        private static final long serialVersionUID = 1L;

        public GameMenuBar() {
            this.add(new Game());
            this.add(new Help());
        }

        private class Game extends JMenu {
            private static final long serialVersionUID = 1L;

            public Game() {
                super("Game");
                this.setMnemonic('G');

                JMenuItem[] gameMenu = new JMenuItem[5];
                gameMenu[0] = new JMenuItem("Play Alone");
                gameMenu[1] = new JMenuItem("Play Together");

                gameMenu[2] = new JMenuItem("Satistic");
                gameMenu[3] = new JMenuItem("Settings");

                gameMenu[4] = new JMenuItem("Exit");

                gameMenu[0].setEnabled(false);
                gameMenu[2].setEnabled(false);
                gameMenu[3].setEnabled(false);
                gameMenu[4].setMnemonic('x');

                gameMenu[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
                gameMenu[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
                gameMenu[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
                gameMenu[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

                int menuNumber = gameMenu.length;
                for (int i = 0; i < menuNumber; i++) {
                    gameMenu[i].addActionListener(new MenuListener());
                }
                this.add(gameMenu[0]);
                this.add(gameMenu[1]);
                this.addSeparator();
                this.add(gameMenu[2]);
                this.add(gameMenu[3]);
                this.addSeparator();
                this.add(gameMenu[4]);
            }
        }

        private class Help extends JMenu {
            private static final long serialVersionUID = 1L;

            public Help() {

                super("Help");
                this.setMnemonic('G');

                JMenuItem[] gameMenu = new JMenuItem[2];
                gameMenu[0] = new JMenuItem("Help");
                this.addSeparator();
                gameMenu[1] = new JMenuItem("About Game");

                gameMenu[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
                gameMenu[0].setEnabled(false);
                gameMenu[1].setEnabled(false);
                this.add(gameMenu[0]);
                this.add(gameMenu[1]);
            }
        }

        private class MenuListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == "Exit") {
                    System.exit(1);
                } else if (e.getActionCommand() == "Play Alone") {
                    eventManager.throwEvent(new GameStartEvent(GameEvent.GAME_START_1P, Math.abs(rand.nextInt() % 4), 0));
                } else if (e.getActionCommand() == "Play Together") {
                    if (gameState == FishEater.GAME_RUN_1P || gameState == FishEater.GAME_RUN_2P || gameState == FishEater.GAME_END) {
                        eventManager.throwEvent(new GameSetEvent());
                    } else {
                        eventManager.throwEvent(new GameStartEvent(GameEvent.GAME_START_2P, Math.abs(rand.nextInt() % 4), Math.abs(rand.nextInt() % 4)));
                    }
                }
            }
        }
    }
}
