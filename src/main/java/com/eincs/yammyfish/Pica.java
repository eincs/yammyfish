package com.eincs.yammyfish;

import javax.swing.ImageIcon;

public class Pica extends Fish {

    public Pica(Pos startPoint) {
        super((int) startPoint.getX(), (int) startPoint.getY(), 6, 0.5, 0.05);
        this.setLevel(5);

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_left.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_3_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_4_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_4_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_5_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_5_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_6_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_6_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_7_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_7_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_8_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_8_left.png"));

        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_9_right.png"));
        imageFish.addImageIcon(new ImageIcon("img/fish/Pica/Pica_9_left.png"));
    }

    @Override
    public void useAbility() {}

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isOutOfbound() {
        if (!(0 < location.getX() && location.getX() < 800) || !(0 < location.getY() && location.getY() < 600)) {
            return true;
        }
        return false;
    }

    @Override
    public void move() {
        super.move();

        double flocateX;
        double flocateY;

        flocateX = location.getX();
        flocateY = location.getY();

        if (flocateX < 0) {
            flocateX = 800;
        } else if (flocateX > 800) {
            flocateX = 0;
        }
        if (flocateY < 0) {
            flocateY = 0;
        } else if (flocateY > 600) {
            flocateY = 600;
        }
        location.setValue(flocateX, flocateY);
        colCenter.setValue(flocateX, flocateY);
    }

    @Override
    public int getType() {
        return Fish.VF_PICACHU;
    }
}
