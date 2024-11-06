package anvu.bomberman.entities.tile.power;

import anvu.bomberman.GameRender;
import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.character.Player;
import anvu.bomberman.graphic.Sprite;

public class PowerSpeed extends Power {
    public PowerSpeed(int x, int y, int level, Sprite sprite) {
        super(x, y, level, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player) {
            ((Player) e).addPower(this);
            remove();
            return true;
        }
        return false;
    }

    @Override
    public void setValues() {
        isActive = true;
        GameRender.addPlayerSpeed(0.1);
    }


}