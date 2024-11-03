package anvu.bomberman.entities.bomb;

import anvu.bomberman.BoardRender;
import anvu.bomberman.GameRender;
import anvu.bomberman.entities.AnimatedEntity;
import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.character.Character;
import anvu.bomberman.entities.character.Player;
import anvu.bomberman.entities.character.enemy.Enemy;
import anvu.bomberman.graphic.Screen;
import anvu.bomberman.graphic.Sprite;
import anvu.bomberman.level.Coordinates;

public class Bomb extends AnimatedEntity {
    protected double timeToExplode = 120;
    public int timeAfter = 20;
    protected BoardRender boardRender;
    protected boolean allowedToPassThrough = true;
    protected DirectionalExplosion[] explosions = null;
    protected boolean isExploded = false;

    public Bomb(int x, int y, BoardRender boardRender) {
        this.x = x;
        this.y = y;
        this.boardRender = boardRender;
        sprite = Sprite.bomb;
    }

    @Override
    public void update() {
        if (timeToExplode > 0)
            timeToExplode--;
        else {
            if (!isExploded)
                explosion();
            else
                updateExplosions();

            if (timeAfter > 0)
                timeAfter--;
            else
                remove();
        }
        animate();
    }

    @Override
    public void render(Screen screen) {
        if (isExploded) {
            sprite = Sprite.explosion_central;
            renderExplosions(screen);
        } else
            sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 60);

        int xt = (int) this.x << 4;
        int yt = (int) this.y << 4;
        screen.renderEntity(xt, yt, this);
    }

    public void renderExplosions(Screen screen) {
        for (DirectionalExplosion explosion : explosions) {
            explosion.render(screen);
        }
    }

    public void updateExplosions() {
        for (DirectionalExplosion explosion : explosions) {
            explosion.update();
        }
    }

    public void explode() {
        timeToExplode = 0;
    }

    protected void explosion() {
        allowedToPassThrough = true;
        isExploded = true;
        Character a = boardRender.getMobAt(this.x, this.y);
        if (a != null) {
            a.kill();
        }
        explosions = new DirectionalExplosion[4];
        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = new DirectionalExplosion((int) this.x, (int) this.y, i, GameRender.getBombRadius(), boardRender);
            Enemy.resetAvoidBomb((int) this.x, (int) this.y);
        }
        explosionBombAudio.playSound(0);
    }

    public Explosion explosionAt(int x, int y) {
        if (!isExploded) return null;
        for (DirectionalExplosion explosion : explosions) {
            if (explosion == null) return null;
            Explosion e = explosion.explosionAt(x, y);
            if (e != null) return e;
        }
        return null;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player) {
            double diffX = e.getX() - Coordinates.tileToPixel(getX());
            double diffY = e.getY() - Coordinates.tileToPixel(getY());
            if (!(diffX >= -13 && diffX < 16 && diffY >= 1 && diffY <= 30)) {
                allowedToPassThrough = false;
            }
            return allowedToPassThrough;
        }
        if (e instanceof DirectionalExplosion) {
            explode();
            return true;
        }
        return false;
    }
}
