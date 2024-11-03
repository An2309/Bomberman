package anvu.bomberman.entities.character.enemy;

import anvu.bomberman.BoardRender;
import anvu.bomberman.GameRender;
import anvu.bomberman.entities.character.enemy.algorithm.SimpleAlgo;
import anvu.bomberman.graphic.Sprite;

public class Doll extends Enemy {
    public Doll(int x, int y, BoardRender boardRender) {
        super(x, y, boardRender, Sprite.doll_dead, GameRender.getPlayerSpeed(), 400);
        sprite = Sprite.doll_right1;
        algorithm = new SimpleAlgo();
        direction = algorithm.getDirection();
    }

    // Mob Sprite
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 60);
                else
                    sprite = Sprite.doll_left1;
                break;
            case 2:
            case 3:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, 60);
                else
                    sprite = Sprite.doll_left1;
                break;
        }
    }
}
