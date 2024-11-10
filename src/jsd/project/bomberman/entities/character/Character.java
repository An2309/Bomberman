package jsd.project.bomberman.entities.character;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.AnimatedEntity;
import jsd.project.bomberman.graphic.Screen;

public abstract class Character extends AnimatedEntity {
    protected BoardRender boardRender;
    protected int direction = -1;
    protected boolean isAlive = true;
    protected boolean isMoving = false;
    public int timeAfter = 80;

    public Character(int x, int y, BoardRender boardRender) {
        this.x = x;
        this.y = y;
        this.boardRender = boardRender;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    protected abstract void calculateMove();

    protected abstract void move(double xa, double ya);

    public abstract void kill();

    protected abstract void afterKill();

    protected abstract boolean canMove(double x, double y);

    public int getDirection() {
        return direction;
    }

    protected double getXMessage() {
        return (this.x * GameRender.SCALE) + (sprite.SIZE / 2 * GameRender.SCALE);
    }

    protected double getYMessage() {
        return (this.y * GameRender.SCALE) - (sprite.SIZE / 2 * GameRender.SCALE);
    }

}













































































