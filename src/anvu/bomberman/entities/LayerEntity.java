package anvu.bomberman.entities;

import java.util.LinkedList;

import anvu.bomberman.entities.tile.destroyable.Destroyable;
import anvu.bomberman.graphic.Screen;

public class LayerEntity extends Entity {
    protected LinkedList<Entity> entities = new LinkedList<>();

    public LayerEntity(int x, int y, Entity... entities) {
        this.x = x;
        this.y = y;
        for (int i = 0; i < entities.length; i++) {
            this.entities.add(entities[i]);
            if (i > 1) { //Add to destroyable tiles the bellow sprite for rendering in explosion
                if (entities[i] instanceof Destroyable)
                    ((Destroyable) entities[i]).addBelowSprite(entities[i - 1].getSprite());
            }
        }
    }

    @Override
    public void update() {
        clearRemoved();
        getTopEntity().update();
    }

    @Override
    public void render(Screen screen) {
        getTopEntity().render(screen);
    }

    public Entity getTopEntity() {
        return entities.getLast();
    }

    private void clearRemoved() {
        Entity top = getTopEntity();
        if (top.isRemoved()) {
            matrix[(int) entities.getLast().y][(int) entities.getLast().x] = 1;
            entities.removeLast();
        }
    }

    public void addBeforeTop(Entity e) {
        entities.add(entities.size() - 1, e);
    }

    @Override
    public boolean collide(Entity e) {
        return getTopEntity().collide(e);
    }

}
