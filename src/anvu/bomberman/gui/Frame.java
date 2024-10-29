package anvu.bomberman.gui;

import anvu.bomberman.GameRender;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public Game game;
    private JPanel containerPanel;
    private Info info;
    private GameRender gameRender;

    public Frame() {
        containerPanel = new JPanel(new BorderLayout());
        game = new Game(this);
        info = new Info(game.getGameRender());

        containerPanel.add(info, BorderLayout.NORTH);
        containerPanel.add(game, BorderLayout.CENTER);

        gameRender = game.getGameRender();
        info.setVisible(false);

        add(containerPanel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        gameRender.start();
    }
}
