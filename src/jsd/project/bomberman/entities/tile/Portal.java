package jsd.project.bomberman.entities.tile;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.entities.Entity;
import jsd.project.bomberman.entities.character.Player;
import jsd.project.bomberman.graphic.Sprite;

public class Portal extends Tile {
    protected BoardRender boardRender;

    public Portal(int x, int y, BoardRender boardRender, Sprite sprite) {
        super(x, y, sprite);
        this.boardRender = boardRender;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player) {
            if (!boardRender.detectNoEnemies())
                return false;
            if (e.getXTile() == getX() && e.getYTile() == getY()) {
                if (boardRender.detectNoEnemies())
                    boardRender.nextLevel();
            }
            return true;
        }
        return false;
    }

}
