package anvu.bomberman;

import anvu.bomberman.gui.Frame;

import java.awt.*;

public class GameRender extends Canvas {
    public static final int TILES_SIZE = 16,
            WIDTH = TILES_SIZE * (int) (31 / 2), //minus one to ajust the window,
            HEIGHT = 13 * TILES_SIZE;
    public static int SCALE = 3;

    public GameRender(Frame frame) {
    }

    public void start() {

    }
}
