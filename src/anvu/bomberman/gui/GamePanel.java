package anvu.bomberman.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

import anvu.bomberman.GameRender;

public class GamePanel extends JPanel {
    private GameRender gameRender;

    public GamePanel(Frame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE));
        gameRender = new GameRender(frame);
        add(gameRender);
        gameRender.setVisible(true);
        setVisible(true);
        setFocusable(true);
    }

    public GameRender getGame() {
        return gameRender;
    }

}
