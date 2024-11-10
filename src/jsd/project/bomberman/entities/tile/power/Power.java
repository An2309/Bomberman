package jsd.project.bomberman.entities.tile.power;

import jsd.project.bomberman.entities.tile.Tile;
import jsd.project.bomberman.graphic.Sprite;

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
