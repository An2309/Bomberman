package anvu.bomberman.entities.tile;

import anvu.bomberman.entities.Entity;
import anvu.bomberman.graphic.Screen;
import anvu.bomberman.graphic.Sprite;
import anvu.bomberman.level.Coordinates;

public abstract class Tile extends Entity {

    public Tile(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    @Override
    public boolean collide(Entity e) {
        return false;
    }

    @Override
    public void render(Screen screen) {
        screen.renderEntity(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y), this);
    }

    @Override
    public void update() {
    }
}