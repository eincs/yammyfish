package com.eincs.yammyfish;

import javax.swing.ImageIcon;

public class FishSprite extends Sprite {

    private int direction;
    private int curLevel;

    public FishSprite() {
        super();
        direction = 0;
        curLevel = 5;
    }

    public void addImageIcon(ImageIcon icon) {
        sprite.add(icon);
    }

    @Override
    public ImageIcon getSprite() {
        return sprite.elementAt(2 * (curLevel - 1) + direction);
    }

    public void setDirection(int direction) {
        // direction = 0 : Right, direction = 1 : Left
        if (!(direction == 0 || direction == 1)) {
            direction = 0;
        }
        this.direction = direction;
    }

    public void setCurLevel(int curLevel) {
        if (!(0 <= curLevel && curLevel <= 10)) {
            curLevel = 10;
        }
        this.curLevel = curLevel;
    }
}
