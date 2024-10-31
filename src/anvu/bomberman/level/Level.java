package anvu.bomberman.level;

import anvu.bomberman.BoardRender;

public abstract class Level implements ILevel {
    protected int width, height, level;
    protected String[] lineTiles;
    protected BoardRender boardRender;

    public Level(String path, BoardRender board) {
        loadLevel(path);
        this.boardRender = board;
    }

    @Override
    public abstract void loadLevel(String path);

    public abstract void createEntities();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }
}
