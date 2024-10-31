package anvu.bomberman.entities.tile;

import anvu.bomberman.BoardRender;
import anvu.bomberman.entities.Entity;
import anvu.bomberman.graphic.Sprite;

public class Portal extends  Tile {
    protected BoardRender boardRender;

    public Portal(int x, int y, BoardRender board, Sprite sprite) {
        super(x, y, sprite);
        boardRender = board;
    }

    // TODO:
//    @Override
//    public boolean collide(Entity e) {
//
//        if (e instanceof Player) {
//
//            if (boardRender.detectNoEnemies() == false)
//                return false;
//
//            if (e.getXTile() == getX() && e.getYTile() == getY()) {
//                if (boardRender.detectNoEnemies())
//                    boardRender.nextLevel();
//            }
//
//            return true;
//        }
//
//        return false;
//    }
}
