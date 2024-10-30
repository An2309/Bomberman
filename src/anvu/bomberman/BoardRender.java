package anvu.bomberman;

import anvu.bomberman.graphic.IRender;
import anvu.bomberman.graphic.Screen;

import java.awt.*;

public class BoardRender implements IRender {

    protected GameRender gameRender;
    protected Screen screen;

    private int screenToShow = -1;

    public BoardRender(GameRender gameRender, Screen screen) {
        this.gameRender = gameRender;
        this.screen = screen;
        screenToShow = 1;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Screen screen) {
    }

    public void drawScreen(Graphics g) {
        switch (screenToShow) {
            case 1:
                screen.drawMenu(g);
                break;
            default:
                break;
        }
    }
}
