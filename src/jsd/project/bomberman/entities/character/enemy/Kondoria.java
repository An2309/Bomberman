package jsd.project.bomberman.entities.character.enemy;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.character.enemy.algorithm.MediumAlgo;
import jsd.project.bomberman.graphic.Sprite;

public class Kondoria extends Enemy {

    public Kondoria(int x, int y, BoardRender boardRender) {
        super(x, y, boardRender, Sprite.kondoria_dead, GameRender.getPlayerSpeed() / 4, 1000);
        sprite = Sprite.kondoria_right1;
        algorithm = new MediumAlgo(boardRender.getPlayer(), this);
        direction = algorithm.getDirection();
    }

    // Mob Sprite
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 60);
                else
                    sprite = Sprite.kondoria_left1;
                break;
            case 2:
            case 3:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 60);
                else
                    sprite = Sprite.kondoria_left1;
                break;
        }
    }
}
