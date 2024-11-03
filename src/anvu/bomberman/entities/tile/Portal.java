package anvu.bomberman.entities.tile;

import anvu.bomberman.BoardRender;
import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.character.Player;
import anvu.bomberman.graphic.Sprite;

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
