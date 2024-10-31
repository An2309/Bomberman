package anvu.bomberman.entities.tile.destroyable;

import anvu.bomberman.graphic.Screen;
import anvu.bomberman.graphic.Sprite;
import anvu.bomberman.level.Coordinates;

public class Brick extends Destroyable {
    public Brick(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Screen screen) {
        int x = Coordinates.tileToPixel(this.x);
        int y = Coordinates.tileToPixel(this.y);

        if (isDestroyed) {
            // TODO:
//            sprite = movingSprite(brick_exploded, brick_exploded1, brick_exploded2);
//            screen.renderEntityWithBelowSprite(x, y, this, _belowSprite);
        } else {
            screen.renderEntity(x, y, this);
        }
    }
}
