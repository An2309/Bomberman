package jsd.project.bomberman.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jsd.project.bomberman.GameRender;

public class Frame extends JFrame {
    public GamePanel gamePanel;
    private final Info infoPanel;
    private final GameRender gameRender;

    public Frame() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        gamePanel = new GamePanel(this);
        infoPanel = new Info(gamePanel.getGame());
        containerPanel.add(infoPanel, BorderLayout.NORTH);
        containerPanel.add(gamePanel, BorderLayout.CENTER);
        gameRender = gamePanel.getGame();
        infoPanel.setVisible(false);

        add(containerPanel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        gameRender.start();
    }

    // Game Related
    public Info getInfoPanel() {
        return infoPanel;
    }

    public void setTime(int time) {
        infoPanel.setTime(time);
    }

    public void setLives(int lives) {
        infoPanel.setLives(lives);
    }

    public void setPoints(int points) {
        infoPanel.setPoints(points);
    }

    public boolean validCode(String str) {
        if (gamePanel.getGame().getMenu()) {
            return false;
        }
        return gameRender.getBoard().getLevel().validCode(str) != -1;
    }

    public void changeLevelByCode(String str) {
        gameRender.getBoard().changeLevelByCode(str);
    }

}