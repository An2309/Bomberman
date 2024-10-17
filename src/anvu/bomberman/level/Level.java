package anvu.bomberman.level;

public abstract class Level implements ILevel {
    @Override
    public abstract void loadLevel(String path);
}
