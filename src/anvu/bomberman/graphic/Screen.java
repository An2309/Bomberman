package anvu.bomberman.graphic;

import anvu.bomberman.GameRender;
import anvu.bomberman.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Screen {
    protected int screenWidth, screenHeight;
    public int[] pixels;
    private final int transparentColor = 0xffff00ff;

    private BufferedImage background = null;
    private Image backgroundFixed = null;

    public static int xOffset = 0, yOffset = 0;

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

    public void renderEntity(int xCoordinate, int yCoordinate, Entity entity) { //save entity pixels
        xCoordinate -= xOffset;
        yCoordinate -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yCoordinate; //add offset
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xCoordinate; //add offset
                if (xa < -entity.getSprite().getSize() || xa >= screenWidth || ya < 0 || ya >= screenHeight)
                    break; //fix black margins
                if (xa < 0) xa = 0; //start at 0 from left
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != transparentColor) pixels[xa + ya * screenWidth] = color;
            }
        }
    }


    public void drawMenu(Graphics g) {
        g.drawImage(backgroundFixed, 0, 0, null);
    }

    public static void setOffset(int xO, int yO) {
        xOffset = xO;
        yOffset = yO;
    }


}
