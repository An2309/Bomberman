package anvu.bomberman.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

import anvu.bomberman.GameRender;

public class GamePanel extends JPanel {

    private GameRender _gameRender;

    public GamePanel(Frame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE));

        _gameRender = new GameRender(frame);
        add(_gameRender);
        _gameRender.setVisible(true);

        setVisible(true);
        setFocusable(true);
    }

    public void changeSize() {
        setPreferredSize(new Dimension(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE));
        revalidate();
        repaint();
    }

    public GameRender getGame() {
        return _gameRender;
    }

}
