package com.eincs.yammyfish;

public class Pos {

    private double x;
    private double y;

    public Pos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setValue(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Pos getInstance() {
        Pos temp = new Pos(this.getX(), this.getY());
        return temp;
    }

    public double getDistance(Pos pos) {
        double res;
        res = Math.sqrt(Math.pow(this.x - pos.getX(), 2) + Math.pow(this.y - pos.getY(), 2));
        return res;
    }
}
