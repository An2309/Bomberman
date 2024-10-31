package anvu.bomberman.graphic;

import java.util.Arrays;

public class Sprite {
    public int[] pixels;
    public final int SIZE;
    protected int realWidth;
    protected int realHeight;
    private int x, y;
    private SpriteSheet sheet;

    public Sprite(int size, int x, int y, SpriteSheet sheet, int rw, int rh) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        this.x = x * SIZE;
        this.y = y * SIZE;
        this.sheet = sheet;
        //realWidth = rw;
        //realHeight = rh;
        load();
    }

    public Sprite(int size, int color) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        setColor(color);
    }

    private void load() {
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                pixels[i + j * SIZE] = sheet.pixels[(i + x) + (j + y) * sheet.SIZE];
            }
        }
    }

    private void setColor(int color) {
        Arrays.fill(pixels, color);
    }

    public int getSize() {
        return this.SIZE;
    }

    public int getPixel(int i) {
        return this.pixels[i];
    }
}
