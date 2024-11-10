package jsd.project.bomberman.entities.bomb;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.character.Character;
import jsd.project.bomberman.graphic.Screen;

public class DirectionalExplosion extends Entity {
    protected BoardRender boardRender;
    protected int direction;
    private final int radius;
    protected int xOrigin, yOrigin;
    protected Explosion[] explosions;

    public DirectionalExplosion(int x, int y, int direction, int radius, BoardRender boardRender) {
        xOrigin = x;
        yOrigin = y;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.radius = radius;
        this.boardRender = boardRender;
        explosions = new Explosion[calculatePermittedDistance()];
        createExplosions();
    }

    private void createExplosions() {
        boolean last = false;
        int x = (int) this.x;
        int y = (int) this.y;
        for (int i = 0; i < explosions.length; i++) {
            last = i == explosions.length - 1;
            switch (direction) {
                case 0:
                    y--;
                    break;
                case 1:
                    x++;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }
            explosions[i] = new Explosion(x, y, direction, last, boardRender);
        }
    }

    private int calculatePermittedDistance() {
        int radius = 0;
        int x = (int) this.x;
        int y = (int) this.y;
        while (radius < this.radius) {
            if (direction == 0) y--;
            if (direction == 1) x++;
            if (direction == 2) y++;
            if (direction == 3) x--;

            Entity a = boardRender.getEntity(x, y, null);

            if (a instanceof Character) ++radius; //explosion has to be below the mob

            if (!a.collide(this)) //cannot pass through
                break;

            ++radius;
        }
        return radius;
    }

    public Explosion explosionAt(int x, int y) {
        for (Explosion explosion : explosions) {
            if (explosion.getX() == x && explosion.getY() == y)
                return explosion;
        }
        return null;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Screen screen) {
        for (Explosion explosion : explosions) {
            explosion.render(screen);
        }
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}
