package anvu.bomberman;

import anvu.bomberman.graphic.Screen;
import anvu.bomberman.gui.Frame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class GameRender extends Canvas implements MouseListener, MouseMotionListener, CommonVariables {
    public static final int TILES_SIZE = 16,
            WIDTH = TILES_SIZE * (int) (31 / 2), //minus one to ajust the window,
            HEIGHT = 13 * TILES_SIZE;

    public static final String TITLE = "Bomberman";

    private BoardRender boardRender;
    private Screen gameScreen;
    private Frame gameFrame;

    public static int SCALE = 3;

    private boolean isMenu = true;

    public GameRender(Frame frame) {
        gameFrame = frame;
        gameFrame.setTitle(TITLE);

        gameScreen = new Screen(WIDTH, HEIGHT);

        boardRender = new BoardRender(this, gameScreen);
    }

    public void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        gameScreen.clear();

        Graphics g = bs.getDrawGraphics();

        boardRender.drawScreen(g);

        g.dispose();
        bs.show();
    }

    public void start() {
        while (isMenu) {
            renderScreen();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
