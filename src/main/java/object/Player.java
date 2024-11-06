/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.geom.Area;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class Player extends HpRender {

    public Player() {
        super(new HP(100, 100));
        this.image = new ImageIcon(getClass().getResource("/game/img/401.PNG")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/game/img/402.PNG")).getImage();
        Path2D p = new Path2D.Double();
        p.moveTo(0, 15);
        p.lineTo(20, 5);
        p.lineTo(PLAYER_SIZE + 15, PLAYER_SIZE / 2);
        p.lineTo(20, PLAYER_SIZE - 5);
        p.lineTo(0, PLAYER_SIZE - 15);
        playerShap = new Area(p);

    }

    public static final double PLAYER_SIZE = 64;

    private double x;
    private double y;
    private float angle = 0;
    private final float MAX_SPEED = 1.0f;
    private float speed = 0.0f;
    private boolean speedUp;
    private final Area playerShap;
    private boolean alive = true;

    private Image image;
    private Image image_speed;

    public Area getShape() {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        at.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);

        return new Area(at.createTransformedShape(playerShap));
    }

    public void changeImage(float angle) {
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void drawPlayer(Graphics2D g2d) {

        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(angle + 45), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        g2d.drawImage(speedUp ? image_speed : image, at, null);
        render(g2d, getShape(), y);
        g2d.setTransform(oldTransform);

        //test
        // g2d.setColor( new Color(12,173,84));
        // g2d.draw(getShape());
        // g2d.draw(getShape().getBounds());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImageSpeed() {
        return image_speed;
    }

    public void setImageSpeed(Image image_speed) {
        this.image_speed = image_speed;
    }

    public void SpeedUp(boolean speedUp) {
        this.speedUp = speedUp;
        if (speed >= MAX_SPEED) {
            speed = MAX_SPEED;
        } else {
            speed += 0.01f;
        }
    }

    public void SpeedDown(boolean speedUp) {
        this.speedUp = speedUp;
        if (speed <= 0) {
            speed = 0;
        } else {
            speed -= 0.003f;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;

    }

    public void reset() {
        speed = 0;
        angle = 0;
        resetHp();
        alive = true;

    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }
}
