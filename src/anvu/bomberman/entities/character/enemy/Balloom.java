package anvu.bomberman.entities.character.enemy;

import anvu.bomberman.BoardRender;
import anvu.bomberman.GameRender;
import anvu.bomberman.entities.character.enemy.algorithm.SimpleAlgo;
import anvu.bomberman.graphic.Sprite;

public class Balloom extends Enemy {
	public Balloom(int x, int y, BoardRender boardRender) {
		super(x, y, boardRender, Sprite.balloom_dead, GameRender.getPlayerSpeed()/2, 100);
		sprite = Sprite.balloom_left1;
		algorithm = new SimpleAlgo();
		direction = algorithm.getDirection();
	}
	
	// Mob Sprite
	@Override
	protected void chooseSprite() {
		switch(direction) {
			case 0:
			case 1:
				sprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 60);
				break;
			case 2:
			case 3:
				sprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 60);
				break;
		}
	}
}
