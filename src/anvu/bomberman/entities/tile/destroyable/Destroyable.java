package anvu.bomberman.entities.tile.destroyable;

import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.bomb.DirectionalExplosion;
import anvu.bomberman.entities.tile.Tile;
import anvu.bomberman.graphic.Sprite;

public class Destroyable extends Tile {
    private int animate = 0;
    protected boolean isDestroyed = false;
    protected int timeToDisappear = 20; // 2 seconds
    protected Sprite belowSprite = grass; //default

    public Destroyable(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        if (isDestroyed) {
            if (animate < MAX_ANIMATE) animate++;
            else animate = 0; //reset animation
            if (timeToDisappear > 0)
                timeToDisappear--;
            else
                remove();
        }
    }

    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public boolean collide(Entity e) {

        if (e instanceof DirectionalExplosion)
            destroy();

        return false;
    }

    public void addBelowSprite(Sprite sprite) {
        belowSprite = sprite;
    }

    protected Sprite movingSprite(Sprite normal, Sprite x1, Sprite x2) {
        int calc = animate % 30;

        if (calc < 10) {
            return normal;
        }

        if (calc < 20) {
            return x1;
        }

        return x2;
    }

}
