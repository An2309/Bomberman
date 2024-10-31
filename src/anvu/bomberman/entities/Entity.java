package anvu.bomberman.entities;

import anvu.bomberman.CommonVariables;
import anvu.bomberman.graphic.IRender;
import anvu.bomberman.graphic.Screen;
import anvu.bomberman.graphic.Sprite;

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

//    public int getXTile() {
//        return Coordinates.pixelToTile(_x + _sprite.SIZE / 2); //plus half block for precision
//    }
//
//    public int getYTile() {
//        return Coordinates.pixelToTile(_y - _sprite.SIZE / 2); //plus half block
//    }
}
