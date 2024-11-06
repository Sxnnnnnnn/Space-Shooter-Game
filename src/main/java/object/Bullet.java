/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.Shape;

public class Bullet {

    private double x;
    private double y;
    private Shape shape;
    private Color color = new Color(255, 255, 255);
    private float angle;
    private double size;
    private float speed = 1.0f;

    public Bullet(double x, double y, float angle, double size, float speed) {

        x += Math.cos(Math.toRadians(angle)) * (Player.PLAYER_SIZE / 2);
        y += Math.sin(Math.toRadians(angle)) * (Player.PLAYER_SIZE / 2);

        x -= size / 2;
        y -= size / 2;

        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;
        shape = new Ellipse2D.Double(0, 0, size, size);
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;

    }

    public boolean check(int width, int height) {
        if (x <= -size || y < -size || x >= width + size || y >= height) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.setColor(color);
        g2.translate(x, y);
        g2.fill(shape);
        g2.setTransform(old);

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getcenterX() {
        return x + size / 2;
    }

    public double getcenterY() {
        return y + size / 2;
    }

    public Shape getShape() {
        return new Area(new Ellipse2D.Double(x, y, size, size));
    }

}
