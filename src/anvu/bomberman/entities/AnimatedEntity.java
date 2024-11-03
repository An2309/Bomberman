package anvu.bomberman.entities;


public abstract class AnimatedEntity extends Entity {
    protected int animate = 0;

    protected void animate() {
        if (animate < MAX_ANIMATE) animate++;
        else animate = 0; //reset animation
    }
}
