package anvu.bomberman.entities.tile.power;

import anvu.bomberman.entities.tile.Tile;
import anvu.bomberman.graphic.Sprite;

public abstract class Power extends Tile {
    protected boolean isActive = false;
    protected int level;

    public Power(int x, int y, int level, Sprite sprite) {
        super(x, y, sprite);
        this.level = level;
    }

    public abstract void setValues();

    public int getLevel() {
        return level;
    }
}
