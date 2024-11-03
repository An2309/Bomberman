package anvu.bomberman.entities.tile;


import anvu.bomberman.entities.Entity;
import anvu.bomberman.graphic.Sprite;

public class Surface extends Tile {

    public Surface(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}
