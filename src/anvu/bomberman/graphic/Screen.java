package anvu.bomberman.graphic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import anvu.bomberman.BoardRender;
import anvu.bomberman.CommonVariables;
import anvu.bomberman.GameRender;
import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.character.Player;

import javax.imageio.ImageIO;

public class Screen implements CommonVariables {
    protected int width, height;
    public int[] pixels;
    private Font font;
    public boolean isBasicMap = true;
    private BufferedImage setting = null;
    private Image backgroundFixed = null;
    private BufferedImage aboutImage = null;
    private BufferedImage chooseNewGameImage = null;
    public static int xOffset = 0, yOffset = 0;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        try {
            setting = ImageIO.read(new File("res/textures/options-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("res/textures/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundFixed = background.getScaledInstance(GameRender.WIDTH * GameRender.SCALE, GameRender.HEIGHT * GameRender.SCALE, Image.SCALE_DEFAULT);
        try {
            aboutImage = ImageIO.read(new File("res/textures/about-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chooseNewGameImage = ImageIO.read(new File("res/textures/new-game.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void renderEntity(int xp, int yp, Entity entity) { //save entity pixels
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp; //add offset
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp; //add offset
                if (xa < -entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height)
                    break; //fix black margins
                if (xa < 0) xa = 0; //start at 0 from left
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != transparentColor) pixels[xa + ya * width] = color;
            }
        }
    }

    public void renderEntityWithBelowSprite(int xp, int yp, Entity entity, Sprite below) {
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                if (xa < -entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height)
                    break; //fix black margins
                if (xa < 0) xa = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != transparentColor)
                    pixels[xa + ya * width] = color;
                else
                    pixels[xa + ya * width] = below.getPixel(x + y * below.getSize());
            }
        }
    }

    public static void setOffset(int xO, int yO) {
        xOffset = xO;
        yOffset = yO;
    }

    public static int calculateXOffset(BoardRender boardRender, Player player) {
        if (player == null) return 0;
        int temp = xOffset;

        double playerX = player.getX() / 16;
        double complement = 0.5;
        int firstBreakpoint = boardRender.getWidth() / 4;
        int lastBreakpoint = boardRender.getWidth() - firstBreakpoint;

        if (playerX > firstBreakpoint + complement && playerX < lastBreakpoint - complement) {
            temp = (int) player.getX() - (GameRender.WIDTH / 2);
        }

        return temp;
    }

    // Game Screens
    public void intializeFont() {
        try {
            File fontFile = new File("res/font/VBRUSHTB.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 60);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            //Handle exception
        }
    }

    public void drawEndGame(Graphics g, int points, int highscore, int level) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/textures/score-table.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int targetWidth = image.getWidth() * GameRender.SCALE / 4;
        int targetHeight = image.getHeight() * GameRender.SCALE / 4;
        Image scoreTable = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        g.setFont(font.deriveFont(Font.PLAIN, 12 * GameRender.SCALE));
        g.setColor(Color.white);
        drawCenteredImage(scoreTable, targetWidth, targetHeight, getRealWidth(), getRealHeight(), g);
        drawCenteredString("Level " + level, getRealWidth(), getRealHeight() - targetHeight + 140 / GameRender.SCALE, g);
        drawCenteredString("Your score: " + points, getRealWidth(), getRealHeight() - targetHeight + 700 / GameRender.SCALE, g);
        drawCenteredString("High score: " + highscore, getRealWidth(), getRealHeight() - targetHeight + 1000 / GameRender.SCALE, g);
        drawCenteredString("Retry", getRealWidth() + 10, getRealHeight() - targetHeight + 1524 / GameRender.SCALE, g);
    }

    public void drawChangeLevel(Graphics g, int level) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("LEVEL " + level, getRealWidth(), getRealHeight(), g);

    }

    public void drawPaused(Graphics g) {
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("PAUSED", getRealWidth(), getRealHeight(), g);

    }

    public void drawMenu(Graphics g) {
        g.drawImage(backgroundFixed, 0, 0, null);
    }

    public void drawSetting(Graphics g) {
        g.setFont(font.deriveFont(Font.PLAIN, 12 * GameRender.SCALE));
        g.setColor(Color.white);

        int targetWidth = setting.getWidth() * GameRender.SCALE / 4;
        int targetHeight = setting.getHeight() * GameRender.SCALE / 4;
        Image settingFixed = setting.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        drawCenteredImage(settingFixed, targetWidth, targetHeight, getRealWidth(), getRealHeight(), g);

        if (isBasicMap) {
            g.drawString("Erangel", GameRender.WIDTH + 140, GameRender.HEIGHT + 65);
        } else {
            g.drawString("Miramar", GameRender.WIDTH + 140, GameRender.HEIGHT + 65);
        }
    }

    public void drawAbout(Graphics g) {
        g.drawImage(aboutImage, GameRender.WIDTH - 175, GameRender.HEIGHT - 100, null);
    }

    public void drawChooseNewGame(Graphics g){
        int targetWidth = chooseNewGameImage.getWidth() * GameRender.SCALE / 4;
        int targetHeight = chooseNewGameImage.getHeight() * GameRender.SCALE / 4;
        Image chooseNewGameImageFixed = chooseNewGameImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        drawCenteredImage(chooseNewGameImageFixed, targetWidth, targetHeight, getRealWidth(), getRealHeight(), g);
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(s)) / 2;
        int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    public void drawCenteredImage(Image image, int imageWidth, int imageHeight,
                                  int gameWidth, int gameHeight, Graphics g) {
        int x = (gameWidth - imageWidth) / 2;
        int y = (gameHeight - imageHeight) / 2;
        g.drawImage(image, x, y, null);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRealWidth() {
        return width * GameRender.SCALE;
    }

    public int getRealHeight() {
        return height * GameRender.SCALE;
    }
}