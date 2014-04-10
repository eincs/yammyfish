package com.eincs.yammyfish;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Player {

    // 플레이어의 상태를 나타내는 상수
    public static final int STATE_NORMAL = 0;
    public static final int STATE_POWER = 1;
    public static final int STATE_STOP = 2;
    public static final int STATE_DEAD = 3;

    private static final int PLAYER_EAT_SCORE = 1000;
    private static final int COMFISH_EAT_SCORE = 10;
    private static final int PLAYER_EAT_POWER = 50;
    private static final int COMFISH_EAT_POWER = 1;
    private static final int COMBO_DELAY_TIME = 2000;
    private static final int RESPOWN_DELAY_TIME = 3000;
    private static final int FLICKING_TIME = 200;

    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;

    private static final int FISH_ICON_WIDTH = 40;
    private static final int FISH_ICON_HEIGHT = 36;

    private static final int INFOBAR_STRAT_X = 50;
    private static final int INFOBAR_START_Y = 27;

    private static final int INFOBAR_END_X = 125;
    private static final int INFOBAR_END_Y = 21;

    private static final int STR_LEVEL_X = 88;
    private static final int STR_LEVEL_Y = 44;
    private static final int STR_LIFE_X = 80;
    private static final int STR_LIFE_Y = 70;
    private static final int STR_SCORE_X = 80;
    private static final int STR_SCORE_Y = 20;

    private static final Font STR_FONT = new Font("SansSerif", Font.BOLD, 18);

    private Timer timer = new Timer(COMBO_DELAY_TIME, new ComboTimer());
    private Timer respown = new Timer(RESPOWN_DELAY_TIME, new ComboTimer());
    private Timer flick = new Timer(FLICKING_TIME, new ComboTimer());
    private int playerID;

    // 현재 플레이어의 정보를 저장할 변수
    private int score;
    private int life;
    private int power;

    // 플레이어의 누적 기록을 저장할 변수
    // 이 변수들은 게임이 끝났을때 정보를 표시할 때 사용된다
    private int numberFishEaten; // 전체 물고기를 먹은 횟수
    private int playerFishEaten; // 상대 플레이어의 물고기를 먹은 횟수

    // 콤보와 관련된 변수들
    private boolean comboState;
    private int curCombo;
    private int maxCombo;

    private int flickCnt;

    // 플레이어 물고기를 참조할 변수
    private Fish fish;
    private int fishState;
    private GameEventManager eventManager;

    private boolean winner;
    private ImageIcon infoBar;
    private ImageIcon fishIcon;

    public Player(int playerID, GameEventManager eventManager) {
        flickCnt = 0;
        this.eventManager = eventManager;
        this.playerID = playerID;
        if (!(playerID == Player.PLAYER1 || playerID == Player.PLAYER2)) {
            playerID = Player.PLAYER1;
        }
        if (playerID == Player.PLAYER1) {
            infoBar = new ImageIcon("img\\fullscr\\player_info_bar1.png");
        } else {
            infoBar = new ImageIcon("img\\fullscr\\player_info_bar2.png");
        }
        timer.start();
        flick.start();
    }

    public void setFish(Fish fish) {
        winner = false;

        score = 0;
        life = 3;
        power = 0;

        curCombo = 0;
        maxCombo = 0;
        comboState = false;

        numberFishEaten = 0;
        playerFishEaten = 0;

        this.fish = fish;

        int fishType = fish.getType();

        if (playerID == Player.PLAYER1) {
            fish.setMessage("PLAYER1");
            if (fishType == Fish.VF_CHICKEN) {
                fishIcon = new ImageIcon("img\\fish\\Chicken\\Chicken_5_left.png");
            } else if (fishType == Fish.VF_FOOTBALL) {
                fishIcon = new ImageIcon("img\\fish\\Football\\Football_5_left.png");
            } else if (fishType == Fish.VF_PICACHU) {
                fishIcon = new ImageIcon("img\\fish\\Pica\\Pica_5_left.png");
            } else {
                fishIcon = new ImageIcon("img\\fish\\Pina\\Pina_5_left.png");
            }
        } else {
            fish.setMessage("PLAYER2");
            if (fishType == Fish.VF_CHICKEN) {
                fishIcon = new ImageIcon("img\\fish\\Chicken\\Chicken_5_right.png");
            } else if (fishType == Fish.VF_FOOTBALL) {
                fishIcon = new ImageIcon("img\\fish\\Football\\Football_5_right.png");
            } else if (fishType == Fish.VF_PICACHU) {
                fishIcon = new ImageIcon("img\\fish\\Pica\\Pica_5_right.png");
            } else {
                fishIcon = new ImageIcon("img\\fish\\Pina\\Pina_5_right.png");
            }
        }
    }

    public boolean isEatable(Fish fish) {
        if (fishState == Player.STATE_DEAD || fishState == Player.STATE_STOP) {
            return false;
        }
        return this.fish.isEatable(fish);
    }

    public boolean isCollition(Fish fish) {
        return this.fish.isCollition(fish);
    }

    public void eaten() {
        if (fishState == Player.STATE_NORMAL) {
            if (!(life == 1)) {
                life--;
                fishState = Player.STATE_DEAD;

                if (playerID == Player.PLAYER1) {
                    fish.respown(FishManager.START_POINT1);
                } else {
                    fish.respown(FishManager.START_POINT2);
                }
                respown.start();
            } else {
                life--;
                if (playerID == Player.PLAYER1) {
                    eventManager.throwEvent(new GameEndEvent(GameEvent.GAME_END_PLAYER2));
                } else {
                    eventManager.throwEvent(new GameEndEvent(GameEvent.GAME_END_PLAYER1));
                }
                fishState = Player.STATE_STOP;
            }
        }
    }

    public int getLevel() {
        return this.fish.getLevel();
    }

    public void eat(Fish fish) {
        if (fishState == Player.STATE_NORMAL || fishState == Player.STATE_POWER) {
            if (comboState) {
                curCombo++;
                if (curCombo > maxCombo) {
                    maxCombo = curCombo;
                }
                timer.restart();
                this.fish.setMessage(curCombo + " Combo!");
            }
            if (fish.isPlayer()) {
                score += this.PLAYER_EAT_SCORE;
                this.power += this.PLAYER_EAT_POWER;
                this.playerFishEaten++;
            } else {
                score += (curCombo + 1) * this.COMFISH_EAT_SCORE;
                this.power += this.COMFISH_EAT_POWER * (1 + curCombo);
            }
            comboState = true;
            this.numberFishEaten++;

            if (power > 100) {
                power = 0;
                life++;
            }

            this.fish.eat(fish);
        }
    }

    public void move() {
        if (fishState == Player.STATE_NORMAL || fishState == Player.STATE_POWER) {
            fish.move();
        }
    }

    public Fish getFish() {
        return fish;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    public int getNumberFishEaten() {
        return numberFishEaten;
    }

    public int getPlayerFishEaten() {
        return playerFishEaten;
    }

    public void draw(Graphics g) {
        g.setFont(Player.STR_FONT);

        if (playerID == Player.PLAYER2) {
            g.setColor(new Color(29, 118, 252));
            if (power > 60 && power < 80) {
                g.setColor(new Color(248, 236, 36));
            } else if (power >= 80) {
                g.setColor(new Color(248, 36, 36));
            }
            g.fillRoundRect(Player.INFOBAR_STRAT_X, Player.INFOBAR_START_Y, Player.INFOBAR_END_X * power / 100, Player.INFOBAR_END_Y, 15, 15);
            g.drawImage(infoBar.getImage(), 0, 0, null);
            g.drawImage(fishIcon.getImage(), Player.FISH_ICON_WIDTH - fishIcon.getIconWidth() / 2, Player.FISH_ICON_HEIGHT - fishIcon.getIconHeight() / 2, null);
            g.setColor(Color.black);
            g.drawString("" + score, Player.STR_SCORE_X, Player.STR_SCORE_Y);
            if (fish.getLevel() == 9) {
                g.drawString("MAX", 800 - Player.STR_LEVEL_X - 35, Player.STR_LEVEL_Y);
            } else {
                g.drawString("Lv. " + (fish.getLevel() - 4), Player.STR_LEVEL_X, Player.STR_LEVEL_Y);
            }
            g.drawString("x" + life, Player.STR_LIFE_X, Player.STR_LIFE_Y);
            g.drawString("Combo : " + curCombo, 20, 100);
        } else {
            g.setColor(new Color(29, 118, 252));
            if (power > 50 && power < 80) {
                g.setColor(new Color(248, 236, 36));
            } else if (power >= 80) {
                g.setColor(new Color(248, 36, 36));
            }
            g.fillRoundRect(800 - Player.INFOBAR_STRAT_X - Player.INFOBAR_END_X * power / 100, Player.INFOBAR_START_Y, Player.INFOBAR_END_X * power / 100, Player.INFOBAR_END_Y, 15, 15);
            g.drawImage(infoBar.getImage(), 800 - infoBar.getIconWidth(), 0, null);
            g.drawImage(fishIcon.getImage(), 800 - (Player.FISH_ICON_WIDTH + fishIcon.getIconWidth() / 2), Player.FISH_ICON_HEIGHT - fishIcon.getIconHeight() / 2, null);
            g.setColor(Color.black);
            g.drawString("" + score, 800 - Player.STR_SCORE_X - 40, Player.STR_SCORE_Y);
            if (fish.getLevel() == 9) {
                g.drawString("MAX", 800 - Player.STR_LEVEL_X - 35, Player.STR_LEVEL_Y);
            } else {
                g.drawString("Lv. " + (fish.getLevel() - 4), 800 - Player.STR_LEVEL_X - 35, Player.STR_LEVEL_Y);
            }
            g.drawString("x" + life, 800 - Player.STR_LIFE_X, Player.STR_LIFE_Y);
            g.drawString("Combo : " + curCombo, 800 - 100, 100);
        }

        if (!(fishState == Player.STATE_DEAD || fishState == Player.STATE_STOP)) {
            if (fishState == Player.STATE_POWER) {
                if (flickCnt == 0) {
                    this.fish.draw(g);
                }
            } else {
                this.fish.draw(g);
            }
        }
        if (winner) {
            ImageIcon black = new ImageIcon("img\\fullscr\\black.png");
            g.drawImage(black.getImage(), 0, 0, null);
            g.setFont(new Font("SansSerif", Font.BOLD, 80));
            g.drawString("GAME OVER", 150, 110);
            g.setFont(new Font("SansSerif", Font.BOLD, 30));
            g.drawString("Score     : " + score, 100, 200);
            g.drawString("Max Combo : " + maxCombo, 100, 250);
            g.drawString("eatenFish : " + numberFishEaten, 100, 300);
            g.drawString("eatPlayer : " + playerFishEaten, 100, 350);

            g.setFont(new Font("SansSerif", Font.BOLD, 80));
            if (playerID == Player.PLAYER1) {
                g.drawString("1P is Winner!!", 130, 450);
            } else {
                g.drawString("2P is Winner!!", 130, 450);
            }
            int fishType = fish.getType();
            ImageIcon icon;

            if (fishType == Fish.VF_CHICKEN) {
                icon = new ImageIcon("img\\fish\\Chicken\\Chicken_9_left.png");
            } else if (fishType == Fish.VF_FOOTBALL) {
                icon = new ImageIcon("img\\fish\\Football\\Football_9_left.png");
            } else if (fishType == Fish.VF_PICACHU) {
                icon = new ImageIcon("img\\fish\\Pica\\Pica_9_left.png");
            } else {
                icon = new ImageIcon("img\\fish\\Pina\\Pina_9_left.png");
            }
            g.drawImage(icon.getImage(), 350, 130, null);
        }
    }

    private class ComboTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == timer) {
                if (comboState) {
                    comboState = false;
                    curCombo = 0;
                    if (playerID == Player.PLAYER1) {
                        fish.setMessage("PLAYER1");
                    } else {
                        fish.setMessage("PLAYER2");
                    }
                    timer.stop();
                }
            }
            if (e.getSource() == respown) {
                if (fishState == Player.STATE_DEAD) {
                    fishState = Player.STATE_POWER;
                } else if (fishState == Player.STATE_POWER) {
                    fishState = Player.STATE_NORMAL;
                }
            }
            if (e.getSource() == flick) {
                if (flickCnt == 0) {
                    flickCnt = 1;
                } else {
                    flickCnt = 0;
                }
            }
        }
    }

    public void setDone() {
        fishState = Player.STATE_POWER;
        respown.stop();
        flick.stop();
        flickCnt = 0;

        winner = true;
    }

    public boolean isEatable(Player player) {
        if (player.getState() == Player.STATE_DEAD || player.getState() == Player.STATE_STOP) {
            return false;
        } else {
            return this.isEatable(player.getFish());
        }
    }

    public int getState() {
        return this.fishState;
    }
}
