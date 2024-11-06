/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Component.PanelGame;

public class Main extends JFrame {

    private PanelGame panelGame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public Main() {
        initUI();
    }

    public void initUI() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        panelGame = new PanelGame();

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "game");
                panelGame.Start();
            }
        });

        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setPreferredSize(new Dimension(200, 60));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setOpaque(true);
        mainMenuPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainMenuPanel.add(startButton, gbc);

        gbc.gridy = 3;
        mainMenuPanel.add(exitButton, gbc);

        ImageIcon image = new ImageIcon(getClass().getResource("/game/img/10001.PNG"));
        JLabel label1 = new JLabel(image);
        gbc.gridy = 0;
        mainMenuPanel.add(label1, gbc);

        JLabel label = new JLabel("Space Shooter Game");
        label.setFont(new Font("Arial", Font.PLAIN, 50));
        label.setForeground(Color.WHITE);
        gbc.gridy = 1;
        mainMenuPanel.add(label, gbc);

        JLabel label2 = new JLabel("Space Shooter Game  Version 1.0");
        label2.setFont(new Font("Arial", Font.PLAIN, 20));
        label2.setForeground(Color.WHITE);
        gbc.gridy = 6;
        mainMenuPanel.add(label2, gbc);

        JLabel label3 = new JLabel("Created by Chatnaphat ");
        label3.setFont(new Font("Arial", Font.PLAIN, 15));
        label3.setForeground(Color.WHITE);
        gbc.gridy = 7;
        mainMenuPanel.add(label3, gbc);

        cardPanel.add(mainMenuPanel, "menu");
        cardPanel.add(panelGame, "game");

        setContentPane(cardPanel);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                cardLayout.show(cardPanel, "menu");
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main main = new Main();
                main.setVisible(true);
            }
        });
    }
}
