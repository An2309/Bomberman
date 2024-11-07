package anvu.bomberman.level;

import anvu.bomberman.BoardRender;

public abstract class Level implements ILevel {

    protected int width, height, level;
    protected String[] lineTiles;
    protected BoardRender boardRender;

    protected static String[] codes = {
            "level1",
            "level2",
            "level3",
            "level4",
            "level5",
            "level6",
            "level7",
    };

    public Level(String path, BoardRender boardRender) {
        loadLevel(path);
        this.boardRender = boardRender;
    }

    @Override
    public abstract void loadLevel(String path);

    public abstract void createEntities();

    public int validCode(String str) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

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
