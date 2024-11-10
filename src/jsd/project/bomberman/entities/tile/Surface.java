package jsd.project.bomberman.entities.tile;


import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.graphic.Sprite;

public class Surface extends Tile {

    public Surface(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}
