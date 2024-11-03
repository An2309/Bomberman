package anvu.bomberman.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import anvu.bomberman.GameRender;


public class Frame extends JFrame {

    public GamePanel _gamepane;
    private JPanel _containerpane;
    private Info _infopanel;
    private GameRender _gameRender;

    public Frame() {
        _containerpane = new JPanel(new BorderLayout());
        _gamepane = new GamePanel(this);
        _infopanel = new Info(_gamepane.getGame());

        _containerpane.add(_infopanel, BorderLayout.NORTH);
        _containerpane.add(_gamepane, BorderLayout.CENTER);

        _gameRender = _gamepane.getGame();
        _infopanel.setVisible(false);

        add(_containerpane);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        _gameRender.start();
    }

	/*
	|--------------------------------------------------------------------------
	| Game Related
	|--------------------------------------------------------------------------
	 */

    public Info get_infopanel() {
        return _infopanel;
    }

    public void newGame() {
        _gameRender.getBoard().newGame();
    }

    public void changeLevel(int i) {
        _gameRender.getBoard().changeLevel(i);
    }

    public void pauseGame() {
        _gameRender.getBoard().gamePauseOnSetting();
    }

    public void resumeGame() {
        _gameRender.getBoard().gameResume();
    }

    public boolean isRunning() {
        return _gameRender.isRunning();
    }

    public void setTime(int time) {
        _infopanel.setTime(time);
    }

    public void setLives(int lives) {
        _infopanel.setLives(lives);
    }

    public void setPoints(int points) {
        _infopanel.setPoints(points);
    }

    public boolean validCode(String str) {
        if (_gamepane.getGame().getMenu()) {
            return false;
        }
        return _gameRender.getBoard().getLevel().validCode(str) != -1;
    }

    public void changeLevelByCode(String str) {
        _gameRender.getBoard().changeLevelByCode(str);
    }

}