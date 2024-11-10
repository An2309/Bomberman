package jsd.project.bomberman.entities.character.enemy;


import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.character.enemy.algorithm.MediumAlgo;
import jsd.project.bomberman.graphic.Sprite;

public class Oneal extends Enemy {

    public Oneal(int x, int y, BoardRender boardRender) {
        super(x, y, boardRender, Sprite.oneal_dead, GameRender.getPlayerSpeed(), 200);
        sprite = Sprite.oneal_left1;
        algorithm = new MediumAlgo(this.boardRender.getPlayer(), this);
        direction = algorithm.getDirection();
    }

    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 60);
                else
                    sprite = Sprite.oneal_left1;
                break;
            case 2:
            case 3:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 60);
                else
                    sprite = Sprite.oneal_left1;
                break;
        }
    }
}
