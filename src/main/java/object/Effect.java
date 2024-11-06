/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import object.ModeBoom;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Effect {

    private final double x;
    private final double y;
    private final int totalEffect;
    private final int max_Size;
    private final double maxDistance;
    private final float speed;
    private final Color color;
    private ModeBoom booms[];
    private double current_distance;
    private float alpha = 1.0f;

    public Effect(double x, double y, int totalEffect, int max_Size, double maxDistance, float speed, Color color) {
        this.x = x;
        this.y = y;
        this.totalEffect = totalEffect;
        this.max_Size = max_Size;
        this.maxDistance = maxDistance;
        this.speed = speed;
        this.color = color;
        this.current_distance = 0.0;
        createRandomBoom();
    }

    private void createRandomBoom() {
        booms = new ModeBoom[totalEffect];
        float pre = 360 / totalEffect;
        Random run = new Random();
        for (int i = 0; i < totalEffect; i++) {
            int r = run.nextInt((int) pre) + 1;
            int boomSize = run.nextInt(max_Size) + 1;
            float angle = i * pre + r;
            booms[i] = new ModeBoom(boomSize, angle);
        }
    }

    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        Composite oldC = g2.getComposite();
        g2.setColor(color);
        g2.translate(x, y);
        for (ModeBoom b : booms) {
            double bx = Math.cos(Math.toRadians(b.getAngle())) * current_distance;
            double by = Math.sin(Math.toRadians(b.getAngle())) * current_distance;
            double boomSize = b.getSize();

            if (current_distance >= maxDistance - (maxDistance * 0.7)) {
                alpha = (float) ((maxDistance - current_distance) / (maxDistance * 0.7));
            }
            if (alpha > 1) {
                alpha = 1;
            } else if (alpha < 0) {
                alpha = 0;
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.fill(new Rectangle2D.Double(bx, by, boomSize, boomSize));
        }

        g2.setComposite(oldC);
        g2.setTransform(old);
    }

    public void update() {
        current_distance += speed;
    }

    public boolean check() {
        return current_distance < maxDistance;
    }
}
