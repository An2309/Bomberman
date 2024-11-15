package jsd.project.bomberman.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import jsd.project.bomberman.CommonVariables;
import jsd.project.bomberman.GameRender;

public class Info extends JPanel implements CommonVariables {
    private JLabel emptyLabel_1, emptyLabel_2, timeLabel, pointsLabel, livesLabel, soundLabel, settingLabel, resetLabel, homeLabel;
    private JButton settingButton, soundButton, resetButton, homeButton;
    private ImageIcon left_bar = new ImageIcon((new ImageIcon("res/textures/left-bar.png")).getImage().getScaledInstance(80, 30, Image.SCALE_DEFAULT));
    private ImageIcon center_bar = new ImageIcon((new ImageIcon("res/textures/center-bar.png")).getImage().getScaledInstance(80, 30, Image.SCALE_DEFAULT));
    private ImageIcon right_bar = new ImageIcon((new ImageIcon("res/textures/right-bar.png")).getImage().getScaledInstance(80, 30, Image.SCALE_DEFAULT));
    private ImageIcon optionImg = new ImageIcon((new ImageIcon("res/textures/options.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    private ImageIcon soundOnImg = new ImageIcon((new ImageIcon("res/textures/sound.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    private ImageIcon soundOffImg = new ImageIcon((new ImageIcon("res/textures/mute.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    private ImageIcon resetImg = new ImageIcon((new ImageIcon("res/textures/reset.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    private ImageIcon homeImg = new ImageIcon((new ImageIcon("res/textures/home.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));


    public Info(GameRender gameRender) {
        setLayout(new GridLayout(1, 8));

        emptyLabel_1 = new JLabel();
        emptyLabel_2 = new JLabel();

        timeLabel = new JLabel();
        timeLabel.setIcon(left_bar);
        timeLabel.setText("Time: " + gameRender.getBoard().getTime());
        timeLabel.setForeground(Color.white);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        timeLabel.setHorizontalTextPosition(JLabel.CENTER);

        pointsLabel = new JLabel();
        pointsLabel.setIcon(center_bar);
        pointsLabel.setText("Points: " + gameRender.getBoard().getPoints());
        pointsLabel.setForeground(Color.white);
        pointsLabel.setHorizontalAlignment(JLabel.CENTER);
        pointsLabel.setHorizontalTextPosition(JLabel.CENTER);

        livesLabel = new JLabel();
        livesLabel.setIcon(right_bar);
        livesLabel.setText("Lives: " + gameRender.getBoard().getLives());
        livesLabel.setForeground(Color.white);
        livesLabel.setHorizontalAlignment(JLabel.LEFT);
        livesLabel.setHorizontalTextPosition(JLabel.CENTER);

        soundLabel = new JLabel();
        soundLabel.setLayout(new BorderLayout());


        soundButton = new JButton();
        soundButton.setIcon(soundOnImg);
        soundButton.setBackground(basicColor);
        soundButton.setBorder(BorderFactory.createEmptyBorder());
        soundButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        soundButton.setModel(new FixedStateButtonModel());
        soundButton.setFocusPainted(false);
        soundButton.setPreferredSize(new Dimension(30, 30));
        soundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (soundButton.getIcon().equals(soundOnImg)) {
                    disableSound();
                    soundButton.setIcon(soundOffImg);
                } else {
                    enableSound();
                    soundButton.setIcon(soundOnImg);
                }

            }
        });
        soundLabel.add(soundButton, BorderLayout.EAST);

        settingLabel = new JLabel();
        settingLabel.setLayout(new BorderLayout());

        settingButton = new JButton();
        settingButton.setBackground(basicColor);
        settingButton.setBorder(BorderFactory.createEmptyBorder());
        settingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingButton.setIcon(optionImg);
        settingButton.setModel(new FixedStateButtonModel());
        settingButton.setFocusPainted(false);
        settingButton.setPreferredSize(new Dimension(30, 30));
        settingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameRender.isPaused()) {
                    gameRender.getBoard().gamePauseOnSetting();
                }
            }
        });
        settingLabel.add(settingButton, BorderLayout.WEST);

        resetLabel = new JLabel();
        resetLabel.setLayout(new BorderLayout());

        resetButton = new JButton();
        resetButton.setBackground(basicColor);
        resetButton.setBorder(BorderFactory.createEmptyBorder());
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setIcon(resetImg);
        resetButton.setModel(new FixedStateButtonModel());
        resetButton.setFocusPainted(false);
        resetButton.setPreferredSize(new Dimension(30, 30));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameRender.isPaused()) {
                    gameRender.getBoard().gamePauseOnReset();
                }
            }
        });
        resetLabel.add(resetButton, BorderLayout.EAST);

        homeLabel = new JLabel();
        homeLabel.setLayout(new BorderLayout());

        homeButton = new JButton();
        homeButton.setBackground(basicColor);
        homeButton.setBorder(BorderFactory.createEmptyBorder());
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setIcon(homeImg);
        homeButton.setModel(new FixedStateButtonModel());
        homeButton.setFocusPainted(false);
        homeButton.setPreferredSize(new Dimension(30, 30));
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        homeLabel.add(homeButton, BorderLayout.EAST);

//        add(homeLabel);
        add(resetLabel);
        add(emptyLabel_1);
        add(timeLabel);
        add(pointsLabel);
        add(livesLabel);
        add(emptyLabel_2);
        add(soundLabel);
        add(settingLabel);


        setBackground(basicColor);
        setPreferredSize(new Dimension(0, 40));
    }

    private void disableSound() {
        mainAudio.stopSound();
        placeBombAudio.stopSound();
        explosionBombAudio.stopSound();
        deadAudio.stopSound();
        upItemAudio.stopSound();
        brickBreakAudio.stopSound();
    }

    private void enableSound() {
        mainAudio.setDisabled(false);
        placeBombAudio.setDisabled(false);
        explosionBombAudio.setDisabled(false);
        deadAudio.setDisabled(false);
        upItemAudio.setDisabled(false);
        brickBreakAudio.setDisabled(false);
        mainAudio.playSound(100);
    }

    public void setTime(int t) {
        this.timeLabel.setText("Time: " + t);
    }

    public void setLives(int t) {
        this.livesLabel.setText("Lives: " + t);
    }

    public void setPoints(int t) {
        this.pointsLabel.setText("Points: " + t);
    }

    public void changeBackground(Color c) {
        setBackground(c);
        soundButton.setBackground(c);
        settingButton.setBackground(c);
        resetButton.setBackground(c);
        homeButton.setBackground(c);
    }
}

class FixedStateButtonModel extends DefaultButtonModel {
    FixedStateButtonModel() {
    }

    public boolean isPressed() {
        return false;
    }

    public boolean isRollover() {
        return false;
    }

    public void setRollover(boolean b) {
    }
}