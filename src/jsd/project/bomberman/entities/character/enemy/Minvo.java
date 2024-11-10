package jsd.project.bomberman.entities.character.enemy;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.character.enemy.algorithm.HighAlgo;
import jsd.project.bomberman.graphic.Sprite;

public class Minvo extends Enemy {

    public Minvo(int x, int y, BoardRender boardRender) {
        super(x, y, boardRender, Sprite.minvo_dead, GameRender.getPlayerSpeed() / 2, 800);
        sprite = Sprite.minvo_right1;
        algorithm = new HighAlgo(this.boardRender.getPlayer(), this);
        direction = algorithm.getDirection();
    }

    // Mob Sprite
    @Override
    protected void chooseSprite() {
        switch (direction) {
            case 0:
            case 1:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2, Sprite.minvo_right3, animate, 60);
                else
                    sprite = Sprite.minvo_left1;
                break;
            case 2:
            case 3:
                if (isMoving)
                    sprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2, Sprite.minvo_left3, animate, 60);
                else
                    sprite = Sprite.minvo_left1;
                break;
        }
    }
}
