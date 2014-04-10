package com.eincs.yammyfish;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public interface Drawable {

    public void draw(Graphics g);

    public ImageIcon toImageIcon();

}
