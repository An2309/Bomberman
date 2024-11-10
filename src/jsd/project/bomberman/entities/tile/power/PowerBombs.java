package jsd.project.bomberman.entities.tile.power;

import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.character.Player;
import jsd.project.bomberman.graphic.Sprite;

public class PowerBombs extends Power {
    public PowerBombs(int x, int y, int level, Sprite sprite) {
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
        GameRender.addBombRate(1);
    }


}