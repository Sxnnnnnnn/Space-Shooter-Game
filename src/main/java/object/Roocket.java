/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.AffineTransform;
import java.awt.Shape;
import java.awt.Rectangle;

public class Roocket extends HpRender {

    public static final double ROCKET_SIZE = 50;
    private double x;
    private double y;
    private final float speed = 1.5f;
    private float angle = 0;
    private final Image image;
    private final Area roocketshape;

    public Roocket() {
        super(new HP(100, 100));
        this.image = new ImageIcon(getClass().getResource("/game/img/2.PNG")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, 0);
        p.lineTo(ROCKET_SIZE - 5, 13);
        p.lineTo(ROCKET_SIZE + 10, ROCKET_SIZE / 2);
        p.lineTo(ROCKET_SIZE - 5, ROCKET_SIZE - 13);
        p.lineTo(15, ROCKET_SIZE - 10);

        roocketshape = new Area(p);
    }

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void changeImage(float angle) {
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void draw(java.awt.Graphics2D g2) {
        java.awt.geom.AffineTransform old = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle + 45), ROCKET_SIZE / 2, ROCKET_SIZE / 2);
        Shape Shap = getShape();

        render(g2, Shap, y);
        g2.drawImage(image, tran, null);
        g2.setTransform(old);

    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public Area getShape() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), ROCKET_SIZE / 2, ROCKET_SIZE / 2);

        return new Area(at.createTransformedShape(roocketshape));
    }

    public boolean checkCollision(int width, int height) {
        Rectangle size = getShape().getBounds();
        if (x <= -size.width || y < -size.height || x > width || y > height) {
            return false;
        } else {
            return true;
        }
    }

}