/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Component;

import javax.swing.JComponent;

import object.Bullet;
import object.Effect;
import object.HP;
import object.Player;
import object.Roocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

public class PanelGame extends JComponent {

    private final int FPS = 60;
    private boolean start = true;
    private final int TAMGET_TIME = 1000000000 / FPS;
    private int width;
    private int height;
    private Thread thread;
    private boolean running = true;
    private Graphics2D g2;
    private BufferedImage image;

    private Player player;
    private Key key;
    private List<Bullet> bullets;
    private int shootTime = 0;
    private List<Roocket> roockets;
    private List<Effect> boomEffect;
    private int score = 0;

    private long startTime;
    private long elapsedTime;
    private int currentLevel = 1;

    public void Start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startTime = System.nanoTime();
                while (running) {
                    long startTime = System.nanoTime();
                    Background();
                    drawGame();
                    Render();
                    long time = System.nanoTime() - startTime;
                    if (time < TAMGET_TIME) {
                        long sleepTime = (TAMGET_TIME - time) / 1000000;
                        sleep(sleepTime);
                    }

                }
            }
        });
        initogjbgame();
        intKey();
        bullets();
        thread.start();
    }

    public void bullets() {
        bullets = new ArrayList<>();
        boomEffect = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkBulleta(bullet);
                            if (bullet.check(width, height)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                    for (int i = 0; i < boomEffect.size(); i++) {
                        Effect effect = boomEffect.get(i);
                        if (effect != null) {
                            effect.update();
                            if (!effect.check()) {
                                boomEffect.remove(effect);
                            }
                        } else {
                            boomEffect.remove(effect);
                        }
                    }
                    sleep(1);

                }
            }
        }).start();
    }

    private void Background() {
        g2.setColor(new Color(65, 65, 65)); // New background c olor
        g2.fillRect(0, 0, width, height);   // Apply the background color to fill the component
    }

//    private void Background() {
//        g2.setColor(new Color(30, 30, 30));
//        g2.fillRect(0, 0, width, height);
//
//    }
    public void resetGame() {
        score = 0;
        player.changeLocation(150, 150);
        roockets.clear();
        bullets.clear();
        player.reset();
        startTime = System.nanoTime();
        start = true;
    }

    private void intKey() {
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(true);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }

        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (true) {

                    if (player.isAlive()) {
                        float angle = player.getAngle();
                        if (key.isKey_right()) {
                            angle += s;
                        }
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_j() || key.isKey_k()) {
                            if (shootTime == 0) {
                                double angleRad = Math.toRadians(player.getAngle());
                                double bulletX = player.getX() + Math.cos(angleRad) * (player.PLAYER_SIZE / 2);
                                double bulletY = player.getY() + Math.sin(angleRad) * (player.PLAYER_SIZE / 2);
                                if (key.isKey_k()) {
                                    bullets.add(0,
                                            new Bullet(bulletX, bulletY, player.getAngle(), 5, 3.0f));
                                } else {
                                    bullets.add(0,
                                            new Bullet(bulletX, bulletY, player.getAngle(), 20, 3.0f));
                                }
                                shootTime++;
                                if (shootTime >= 15) {
                                    shootTime = 0;
                                }
                            } else {
                                shootTime = 0;
                            }
                        }

//                        if (key.isKey_j() || key.isKey_k()) {
//                            if (shootTime == 0) {
//                                if (key.isKey_k()) {
//                                    bullets.add(0,
//                                            new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3.0f));
//                                } else {
//                                    bullets.add(0,
//                                            new Bullet(player.getX(), player.getY(), player.getAngle(), 20, 3.0f));
//                                }
//                                shootTime++;
//                                if (shootTime >= 15) {
//                                    shootTime = 0;
//                                }
//                            } else {
//                                shootTime = 0;
//                            }
//                        }
                        if (key.isKey_space()) {
                            player.SpeedUp(true);
                        } else {
                            player.SpeedDown(true);
                        }

                        player.update();
                        player.changeImage(angle);

                    } else {
                        if (key.isKey_enter()) {
                            resetGame();
                        }

                    }

                    for (int i = 0; i < roockets.size(); i++) {
                        Roocket roocket = roockets.get(i);
                        if (roocket != null) {
                            roocket.update();
                            if (!roocket.checkCollision(width, height)) {
                                roockets.remove(roocket);
                            } else {
                                if (player.isAlive()) {
                                    checkPlayer(roocket);
                                }
                            }

                        }

                    }

                    sleep(5);
                }
            }
        }).start();
    }

    private void addRoocket() {
        Random random = new Random();
        int locationY = random.nextInt(height - 50) + 25;
        Roocket roocket = new Roocket();
        roocket.changeLocation(0, locationY);
        roocket.changeImage(180);
        roockets.add(roocket);

        int locationY2 = random.nextInt(height - 50) + 25;
        Roocket roocket2 = new Roocket();
        roocket2.changeLocation(width, locationY2);
        roocket2.changeImage(180);
        roockets.add(roocket2);
    }

    private void initogjbgame() {
        player = new Player();
        player.changeLocation(150, 150);
        bullets = new ArrayList<>();
        roockets = new ArrayList<>();
        boomEffect = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addRoocket();
                    sleep(3000);
                }

            }
        }).start();
    }

    private void resetTime1(int currentLevel) {
        if (currentLevel == 1) {
            startTime = System.nanoTime();
        } else if (currentLevel == 2) {
            startTime = System.nanoTime();
        } else if (currentLevel == 3) {
            startTime = System.nanoTime();
        }

    }

    private void drawGame() {

        if (player.isAlive()) {
            player.drawPlayer(g2);
        }
        if (bullets != null) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = bullets.get(i);
                if (bullet != null) {
                    bullet.draw(g2);
                }
            }
        }

        for (int i = 0; i < roockets.size(); i++) {
            Roocket roocket = roockets.get(i);
            if (roocket != null) {
                roocket.draw(g2);
            }
        }

        for (int i = 0; i < boomEffect.size(); i++) {
            Effect effect = boomEffect.get(i);
            if (effect != null) {
                effect.draw(g2);
            }
        }

        g2.setColor(Color.white);
        g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
        g2.drawString("Score : " + score, 10, 20);
        g2.drawString("HP : " + player.getHP(), 10, 40);
        g2.drawString("Level : " + currentLevel, 10, 100);

        elapsedTime = (System.nanoTime() - startTime) / 1000000000;
        g2.drawString("Time : " + elapsedTime, 10, 80);

        if (score == 10) {
            String gameOverText = "Congratulations! You passed Level " + (currentLevel - 1) + "!";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            g2.setColor(Color.GREEN);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
            double textWidth = rect.getWidth();
            double textHeight = rect.getHeight();
            double textX = (width - textWidth) / 2;
            double textY = (height - textHeight) / 2;
            g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());
            resetTime1(2);

        }

        if (score == 15) {
            String gameOverText = "Congratulations! You passed Level " + (currentLevel - 1) + "!";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            g2.setColor(Color.GREEN);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
            double textWidth = rect.getWidth();
            double textHeight = rect.getHeight();
            double textX = (width - textWidth) / 2;
            double textY = (height - textHeight) / 2;
            g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());
            resetTime1(3);

        }

        if (score >= 10 && currentLevel == 1) {
            currentLevel++;

        }
        if (score >= 15 && currentLevel == 2) {
            currentLevel++;
        }
        if (score >= 20 && currentLevel == 3) {
            currentLevel++;
        }

        switch (currentLevel) {
            case 1:

                if (elapsedTime <= 30) {
                    String timeText = "Time: " + (30 - elapsedTime) + " seconds";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.GREEN);
                    g2.drawString(timeText, 10, 60);
                } else {

                    String timeText = "Time's up!";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.RED);
                    g2.drawString(timeText, 10, 60);

                    String gameOverText = "Game Over";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
                    g2.setColor(Color.RED);
                    FontMetrics fm = g2.getFontMetrics();
                    Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
                    double textWidth = rect.getWidth();
                    double textHeight = rect.getHeight();
                    double textX = (width - textWidth) / 2;
                    double textY = (height - textHeight) / 2;
                    g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());

                    String restartText = "Press Enter to Restart";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
                    fm = g2.getFontMetrics();
                    rect = fm.getStringBounds(restartText, g2);
                    textWidth = rect.getWidth();
                    textHeight = rect.getHeight();
                    textX = (width - textWidth) / 2;
                    textY = (height - textHeight) / 2;
                    g2.drawString(restartText, (int) textX, (int) textY + fm.getAscent() + 50);

                    if (key.isKey_enter()) {
                        resetGame();

                    }

                }
                break;
            case 2:

                if (elapsedTime <= 20) {

                    String timeText = "Time: " + (20 - elapsedTime) + " seconds";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.GREEN);
                    g2.drawString(timeText, 10, 60);
                } else {

                    String timeText = "Time's up!";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.RED);
                    g2.drawString(timeText, 10, 60);

                    String gameOverText = "Game Over";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
                    g2.setColor(Color.RED);
                    FontMetrics fm = g2.getFontMetrics();
                    Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
                    double textWidth = rect.getWidth();
                    double textHeight = rect.getHeight();
                    double textX = (width - textWidth) / 2;
                    double textY = (height - textHeight) / 2;
                    g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());

                    String restartText = "Press Enter to Restart";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
                    fm = g2.getFontMetrics();
                    rect = fm.getStringBounds(restartText, g2);
                    textWidth = rect.getWidth();
                    textHeight = rect.getHeight();
                    textX = (width - textWidth) / 2;
                    textY = (height - textHeight) / 2;
                    g2.drawString(restartText, (int) textX, (int) textY + fm.getAscent() + 50);

                    if (key.isKey_enter()) {
                        resetGame();

                    }

                }
                break;
            case 3:
                if (elapsedTime <= 10) {
                    String timeText = "Time: " + (10 - elapsedTime) + " seconds";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.GREEN);
                    g2.drawString(timeText, 10, 60);
                } else {
                    String timeText = "Time's up!";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
                    g2.setColor(Color.RED);
                    g2.drawString(timeText, 10, 60);

                    String gameOverText = "Game Over";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
                    g2.setColor(Color.RED);
                    FontMetrics fm = g2.getFontMetrics();
                    Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
                    double textWidth = rect.getWidth();
                    double textHeight = rect.getHeight();
                    double textX = (width - textWidth) / 2;
                    double textY = (height - textHeight) / 2;
                    g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());

                    String restartText = "Press Enter to Restart";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
                    fm = g2.getFontMetrics();
                    rect = fm.getStringBounds(restartText, g2);
                    textWidth = rect.getWidth();
                    textHeight = rect.getHeight();
                    textX = (width - textWidth) / 2;
                    textY = (height - textHeight) / 2;
                    g2.drawString(restartText, (int) textX, (int) textY + fm.getAscent() + 50);

                    if (key.isKey_enter()) {
                        resetGame();

                    }

                }
                break;
            default:

                if (score >= 20) {
                    String gameOverText = "Congratulations You Win!  ";
                    g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
                    g2.setColor(Color.BLACK);
                    FontMetrics fm = g2.getFontMetrics();
                    Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
                    double textWidth = rect.getWidth();
                    double textHeight = rect.getHeight();
                    double textX = (width - textWidth) / 2;
                    double textY = (height - textHeight) / 2;
                    g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());
                    resetTime1(currentLevel);
                    start = false;

                    if (key.isKey_enter()) {
                        System.exit(0);
                    }
                }

                if (currentLevel < 3) {
                    currentLevel++;
                }
                break;
        }

        if (!player.isAlive()) {
            String gameOverText = "Game Over";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            g2.setColor(Color.RED);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(gameOverText, g2);
            double textWidth = rect.getWidth();
            double textHeight = rect.getHeight();
            double textX = (width - textWidth) / 2;
            double textY = (height - textHeight) / 2;
            g2.drawString(gameOverText, (int) textX, (int) textY + fm.getAscent());

            String restartText = "Press Enter to restart";
            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            rect = fm.getStringBounds(restartText, g2);
            textWidth = rect.getWidth();
            textHeight = rect.getHeight();
            textX = (width - textWidth) / 2;
            textY = (height - textHeight) / 2;
            g2.drawString(restartText, (int) textX, (int) textY + fm.getAscent() + 50);
        }

    }

    private void Render() {
        Graphics2D g = (Graphics2D) getGraphics();
        g.drawImage(image, 0, 0, null);
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void checkBulleta(Bullet bullet) {
        for (int i = 0; i < roockets.size(); i++) {
            Roocket roocket = roockets.get(i);
            if (roocket != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(roocket.getShape());
                if (!area.isEmpty()) {
                    boomEffect.add(new Effect(bullet.getcenterX(), bullet.getcenterY(), 3, 5, 60, 0.5f,
                            new Color(230, 207, 105)));
                    if (!roocket.updateHp(bullet.getSize())) {
                        score++;
                        roockets.remove(roocket);
                        double x = roocket.getX() + roocket.ROCKET_SIZE / 2;
                        double y = roocket.getY() + roocket.ROCKET_SIZE / 2;

                        boomEffect.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                        boomEffect.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));

                        bullets.remove(bullet);

                    }
                }
            }

        }
    }

    private void checkPlayer(Roocket roocket) {

        if (roocket != null) {
            Area area = new Area(player.getShape());
            area.intersect(roocket.getShape());
            if (!area.isEmpty()) {
                double roocketHp = roocket.getHP();
                if (!roocket.updateHp(player.getHP())) {
                    roockets.remove(roocket);
                    double x = roocket.getX() + roocket.ROCKET_SIZE / 2;
                    double y = roocket.getY() + roocket.ROCKET_SIZE / 2;
                    boomEffect.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffect.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                }
                if (!player.updateHp(roocketHp)) {
                    player.setAlive(false);
                    double x = roocket.getX() + player.PLAYER_SIZE / 2;
                    double y = roocket.getY() + player.PLAYER_SIZE / 2;
                    boomEffect.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffect.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                }

            }
        }
    }

}
