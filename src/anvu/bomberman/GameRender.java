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

        addMouseListener(this);
        addMouseMotionListener(this);
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
        new Thread(() -> {
            while (isMenu) {
                renderScreen();
                try {
                    Thread.sleep(16); // Approximately 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle playButton = new Rectangle(128, 278, 200, 60);
        if (playButton.contains(e.getX(), e.getY()) && isMenu) {
            System.out.println("Play");
        }

        Rectangle optionButton = new Rectangle(128, 390, 200, 60);
        if (optionButton.contains(e.getX(), e.getY()) && isMenu) {
            System.out.println("Option");
        }

        Rectangle aboutButton = new Rectangle(128, 498, 200, 60);
        if (aboutButton.contains(e.getX(), e.getY()) && isMenu) {
            System.out.println("About");
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle playButton = new Rectangle(128, 278, 200, 60);
        Rectangle optionButton = new Rectangle(128, 390, 200, 60);
        Rectangle aboutButton = new Rectangle(128, 498, 200, 60);

        if (isMenu) {
            if (playButton.contains(e.getX(), e.getY())
                    || optionButton.contains(e.getX(), e.getY())
                    || aboutButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

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
