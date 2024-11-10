package jsd.project.bomberman.entities.character.enemy;

import java.awt.Color;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.Message;
import jsd.project.bomberman.entities.bomb.DirectionalExplosion;
import jsd.project.bomberman.entities.character.Character;
import jsd.project.bomberman.entities.character.Player;
import jsd.project.bomberman.entities.character.enemy.algorithm.Algorithm;
import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.graphic.Sprite;
import jsd.project.bomberman.level.Coordinates;

public abstract class Enemy extends Character {
    protected int points;
    protected double speed;
    protected Algorithm algorithm;
    //necessary to correct move
    protected final double MAX_STEPS;
    protected final double rest;
    protected double steps;
    protected int finalAnimation = 30;
    protected Sprite deadSprite;

    public Enemy(int x, int y, BoardRender boardRender, Sprite dead, double speed, int points) {
        super(x, y, boardRender);
        this.points = points;
        this.speed = speed;
        MAX_STEPS = GameRender.TILES_SIZE / speed;
        rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
        steps = MAX_STEPS;
        timeAfter = 20;
        deadSprite = dead;
    }

    public static void avoidBomb(int x, int y) {
        matrix[y][x] = 0;
        // Up
        if (y - 1 >= 0 && matrix[y - 1][x] == 1) {
            matrix[y - 1][x] = -1;
        }
        // Right
        if (x + 1 < MAP_WIDTH && matrix[y][x + 1] == 1) {
            matrix[y][x + 1] = -1;
        }
        // Down
        if (y + 1 < MAP_HEIGHT && matrix[y + 1][x] == 1) {
            matrix[y + 1][x] = -1;
        }
        // Left
        if (x - 1 >= 0 && matrix[y][x - 1] == 1) {
            matrix[y][x - 1] = -1;
        }
    }

    public static void resetAvoidBomb(int x, int y) {
        matrix[y][x] = 1;
        // Up
        if (y - 1 >= 0 && matrix[y - 1][x] == -1) {
            matrix[y - 1][x] = 1;
        }
        // Right
        if (x + 1 < MAP_WIDTH && matrix[y][x + 1] == -1) {
            matrix[y][x + 1] = 1;
        }
        // Down
        if (y + 1 < MAP_HEIGHT && matrix[y + 1][x] == -1) {
            matrix[y + 1][x] = 1;
        }
        // Left
        if (x - 1 >= 0 && matrix[y][x - 1] == -1) {
            matrix[y][x - 1] = 1;
        }
    }

    @Override
    public void update() {
        animate();

        if (!isAlive) {
            afterKill();
            return;
        }

        calculateMove();
    }

    @Override
    public void render(Screen screen) {
        if (isAlive) {
            chooseSprite();
        } else {
            if (timeAfter > 0) {
                sprite = deadSprite;
                animate = 0;
            } else {
                sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, animate, 60);
            }
        }
        screen.renderEntity((int) this.x, (int) this.y - sprite.SIZE, this);
    }

    @Override
    public void calculateMove() {
        int xa = 0, ya = 0;
        if (steps <= 0) {
            direction = algorithm.getDirection();
            steps = MAX_STEPS;
        }

        if (direction == 0) ya--;
        if (direction == 2) ya++;
        if (direction == 3) xa--;
        if (direction == 1) xa++;

        if (canMove(xa, ya)) {
            steps -= 1 + rest;
            move(xa * speed, ya * speed);
            isMoving = true;
        } else {
            steps = 0;
            isMoving = false;
        }
    }

    @Override
    public void move(double xa, double ya) {
        if (!isAlive) {
            return;
        }
        this.y += ya;
        this.x += xa;
    }

    @Override
    public boolean canMove(double x, double y) {
        double xr = this.x, yr = this.y - 16; //subtract y to get more accurate results
        //the thing is, subract 15 to 16 (sprite size), so if we add 1 tile we get the next pixel tile with this
        //we avoid the shaking inside tiles with the help of steps
        if (direction == 0) {
            yr += sprite.getSize() - 1;
            xr += sprite.getSize() / 2;
        }
        if (direction == 1) {
            yr += sprite.getSize() / 2;
            xr += 1;
        }
        if (direction == 2) {
            xr += sprite.getSize() / 2;
            yr += 1;
        }
        if (direction == 3) {
            xr += sprite.getSize() - 1;
            yr += sprite.getSize() / 2;
        }
        int xx = Coordinates.pixelToTile(xr) + (int) x;
        int yy = Coordinates.pixelToTile(yr) + (int) y;
        Entity a = boardRender.getEntity(xx, yy, this); //entity of the position we want to go
        return a.collide(this);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof DirectionalExplosion) {
            kill();
            return false;
        }
        if (e instanceof Player) {
            ((Player) e).kill();
            return false;
        }
        return true;
    }

    @Override
    public void kill() {
        if (!isAlive) {
            return;
        }
        isAlive = false;
        boardRender.addPoints(points);
        Message msg = new Message("+" + points, getXMessage(), getYMessage(), 2, Color.white, 14);
        boardRender.addMessage(msg);
    }


    @Override
    protected void afterKill() {
        if (timeAfter > 0) {
            timeAfter--;
        } else {
            if (finalAnimation > 0) {
                --finalAnimation;
            } else {
                remove();
            }
        }
    }

    protected abstract void chooseSprite();
}
