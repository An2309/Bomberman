package jsd.project.bomberman.entities.character;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.Message;
import jsd.project.bomberman.entities.bomb.Bomb;
import jsd.project.bomberman.entities.bomb.DirectionalExplosion;
import jsd.project.bomberman.entities.character.enemy.Enemy;
import jsd.project.bomberman.entities.tile.power.Power;
import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.graphic.Sprite;
import jsd.project.bomberman.input.Keyboard;
import jsd.project.bomberman.level.Coordinates;

public class Player extends Character {
    private List<Bomb> bombs;
    protected Keyboard input;

    protected int timeBetweenPutBombs = 0;

    public static List<Power> powers = new ArrayList<>();

    public Player(int x, int y, BoardRender boardRender) {
        super(x, y, boardRender);
        this.bombs = boardRender.getBombs();
        input = boardRender.getInput();
        sprite = Sprite.player_right;
    }

    // Update & Render
    @Override
    public void update() {
        clearBombs();
        if (!isAlive) {
            afterKill();
            return;
        }
        if (timeBetweenPutBombs < -7500) timeBetweenPutBombs = 0;
        else timeBetweenPutBombs--;
        animate();
        calculateMove();
        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();
        if (isAlive)
            chooseSprite();
        else
            sprite = Sprite.player_dead;
        screen.renderEntity((int) this.x, (int) this.y - sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(boardRender, this);
        Screen.setOffset(xScroll, 0);
    }


    // Mob Unique
    private void detectPlaceBomb() {
        if (input.space && GameRender.getBombRate() > 0 && timeBetweenPutBombs < 0) {
            int xt = Coordinates.pixelToTile(this.x + sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((this.y + sprite.getSize() / 2) - sprite.getSize()); //subtract half player height and minus 1 y position
            placeBomb(xt, yt);
            GameRender.addBombRate(-1);
            timeBetweenPutBombs = 30;
        }
    }

    protected void placeBomb(int x, int y) {
        Bomb b = new Bomb(x, y, boardRender);
        Enemy.avoidBomb(x, y);
        boardRender.addBomb(b);
        placeBombAudio.playSound(0);
    }

    private void clearBombs() {
        Iterator<Bomb> bs = bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                GameRender.addBombRate(1);
            }
        }

    }

    // Mob Collide & Kill
    @Override
    public void kill() {
        if (!isAlive) return;

        isAlive = false;

        boardRender.addLives(-1);
        deadAudio.playSound(0);
        Message msg = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
        boardRender.addMessage(msg);
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) --timeAfter;
        else {
            if (bombs.isEmpty()) {

                if (boardRender.getLives() == 0)
                    boardRender.endGame();
                else
                    boardRender.restartLevel();
            }
        }
    }

    // Mob Movement
    @Override
    protected void calculateMove() {
        int xa = 0, ya = 0;
        if (input.up) ya--;
        if (input.down) ya++;
        if (input.left) xa--;
        if (input.right) xa++;
        if (xa != 0 || ya != 0) {
            move(xa * GameRender.getPlayerSpeed(), ya * GameRender.getPlayerSpeed());
            isMoving = true;
        } else {
            isMoving = false;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        for (int c = 0; c < 4; c++) {
            double xt = ((this.x + x) + c % 2 * 13) / GameRender.TILES_SIZE;
            double yt = ((this.y + y) + c / 2 * 12 - 13) / GameRender.TILES_SIZE;

            Entity a = boardRender.getEntity(xt, yt, this);
            if (!a.collide(this)) {
                return false;
            }

        }
        return true;
    }

    @Override
    public void move(double xa, double ya) {
        if (xa > 0) direction = 1;
        if (xa < 0) direction = 3;
        if (ya > 0) direction = 2;
        if (ya < 0) direction = 0;

        if (canMove(0, ya)) { //separate the moves for the player can slide when is colliding
            this.y += ya;
        }

        if (canMove(xa, 0)) {
            this.x += xa;
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof DirectionalExplosion) {
            kill();
            return false;
        }

        if (e instanceof Enemy) {
            kill();
            return true;
        }

        return true;
    }

    // Power
    public void addPower(Power p) {
        if (p.isRemoved()) return;

        powers.add(p);
        upItemAudio.playSound(0);
        p.setValues();
    }

    // Mob Sprite
    private void chooseSprite() {
        switch (direction) {
            case 0:
                sprite = Sprite.player_up;
                if (isMoving) {
                    sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
                }
                break;
            case 1:
                sprite = Sprite.player_right;
                if (isMoving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
            case 2:
                sprite = Sprite.player_down;
                if (isMoving) {
                    sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
                }
                break;
            case 3:
                sprite = Sprite.player_left;
                if (isMoving) {
                    sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
                }
                break;
            default:
                sprite = Sprite.player_right;
                if (isMoving) {
                    sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
                }
                break;
        }
    }
}