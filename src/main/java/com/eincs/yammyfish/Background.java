package com.eincs.yammyfish;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Background implements Drawable {

    public static final int PRN_LOGO = 0;
    public static final int PRN_BACKGROUND = 1;
    private ImageIcon curImage;

    private ImageIcon gameLogo;
    private ImageIcon gameBackground;

    public Background() {
        gameLogo = new ImageIcon("img/fullscr/fullscr_logo.png");
        gameBackground = new ImageIcon("img/fullscr/fullscr_back.png");

        curImage = gameLogo;
    }

    public void setMode(int mode) {
        if (mode == PRN_BACKGROUND) {
            curImage = gameBackground;
        } else {
            curImage = gameLogo;
        }
    }

    @Override
    public ImageIcon toImageIcon() {
        return curImage;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(curImage.getImage(), 0, 0, null);
    }
}
