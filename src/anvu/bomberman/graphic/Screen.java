package anvu.bomberman.graphic;

import anvu.bomberman.GameRender;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Screen {
    protected int screenWidth, screenHeight;
    public int[] pixels;

    private BufferedImage background = null;
    private Image backgroundFixed = null;

    public Screen(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        pixels = new int[width * height];

        try {
            background = ImageIO.read(new File("res/textures/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundFixed = background.getScaledInstance(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE, Image.SCALE_DEFAULT);
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void drawMenu(Graphics g) {
        g.drawImage(backgroundFixed, 0, 0, null);
    }

}
