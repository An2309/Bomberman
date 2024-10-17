package anvu.bomberman.entities;

import anvu.bomberman.graphic.IRender;
import anvu.bomberman.graphic.Screen;

public abstract class Entity implements IRender {
    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);
}
