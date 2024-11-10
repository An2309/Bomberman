package jsd.project.bomberman.level;

import jsd.project.bomberman.GameRender;

public class Coordinates {

    public static int pixelToTile(double i) {
        return (int) (i / GameRender.TILES_SIZE);
    }

    public static int tileToPixel(int i) {
        return i * GameRender.TILES_SIZE;
    }

    public static int tileToPixel(double i) {
        return (int) (i * GameRender.TILES_SIZE);
    }
}
