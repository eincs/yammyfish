package com.eincs.yammyfish;

import javax.swing.ImageIcon;

public class NormalFish extends Fish {

    public NormalFish(int startX, int startY, double maxVelocity, double accelation, double waterResitance) {
        super(startX, startY, maxVelocity, accelation, waterResitance);

        imageFish = new FishSprite();

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_1_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_1_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_2_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_2_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_3_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_3_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_4_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_4_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_5_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_5_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_6_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_6_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_7_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_7_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_8_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_8_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_9_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_9_left.png"));

        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_10_right.png"));
        imageFish.addImageIcon(new ImageIcon("img\\fish\\NormalFish\\NormalFish_10_left.png"));
    }

    @Override
    public void useAbility() {}

    @Override
    public boolean isPlayer() {
        return false;
    }
}
