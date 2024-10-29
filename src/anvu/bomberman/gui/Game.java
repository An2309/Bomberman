package anvu.bomberman.gui;

import anvu.bomberman.GameRender;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class Game extends JPanel {
    private GameRender gameRender;

    public Game(Frame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE));

        gameRender = new GameRender(frame);
        add(gameRender);
        gameRender.setVisible(true);

        setVisible(true);
        setFocusable(true);
    }

    public void changeSize() {
        setPreferredSize(new Dimension(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE));
        revalidate();
        repaint();
    }

    public GameRender getGameRender() {
        return gameRender;
    }
}
