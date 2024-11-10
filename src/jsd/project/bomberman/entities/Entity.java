package jsd.project.bomberman.entities;

import jsd.project.bomberman.CommonVariables;
import jsd.project.bomberman.graphic.IRender;
import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.graphic.Sprite;
import jsd.project.bomberman.level.Coordinates;

public abstract class Entity implements IRender, CommonVariables {
    protected double x, y;
    protected boolean isRemoved = false;
    protected Sprite sprite;

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    public void remove() {
        isRemoved = true;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract boolean collide(Entity e);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getXTile() {
        return Coordinates.pixelToTile(x + sprite.SIZE / 2);
    }

    public int getYTile() {
        return Coordinates.pixelToTile(y - sprite.SIZE / 2);
    }
}
