package jsd.project.bomberman.entities.bomb;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.character.Character;
import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.graphic.Sprite;

public class Explosion extends Entity {
    protected boolean last = false;
    protected BoardRender boardRender;

    public Explosion(int x, int y, int direction, boolean last, BoardRender boardRender) {
        this.x = x;
        this.y = y;
        this.last = last;
        this.boardRender = boardRender;

        switch (direction) {
            case 0:
                if (!last) {
                    sprite = Sprite.explosion_vertical_mid;
                } else {
                    sprite = Sprite.explosion_vertical_top;
                }
                break;
            case 1:
                if (!last) {
                    sprite = Sprite.explosion_horizontal_mid;
                } else {
                    sprite = Sprite.explosion_horizontal_right;
                }
                break;
            case 2:
                if (!last) {
                    sprite = Sprite.explosion_vertical_mid;
                } else {
                    sprite = Sprite.explosion_vertical_down;
                }
                break;
            case 3:
                if (!last) {
                    sprite = Sprite.explosion_horizontal_mid;
                } else {
                    sprite = Sprite.explosion_horizontal_left;
                }
                break;
        }
    }

    @Override
    public void render(Screen screen) {
        int xt = (int) this.x << 4;
        int yt = (int) this.y << 4;
        screen.renderEntity(xt, yt, this);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Character) {
            ((Character) e).kill();
        }
        return true;
    }


}