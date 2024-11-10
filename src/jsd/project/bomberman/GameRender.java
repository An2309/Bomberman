package jsd.project.bomberman;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.gui.CodePanel;
import jsd.project.bomberman.gui.Frame;
import jsd.project.bomberman.input.Keyboard;

public class GameRender extends Canvas implements MouseListener, MouseMotionListener, CommonVariables {
    public static final int TILES_SIZE = 16,
            WIDTH = TILES_SIZE * (int) (31 / 2), //minus one to ajust the window,
            HEIGHT = 13 * TILES_SIZE;

    public static int highScore = 0;

    protected static int bombRate = BOMB_RATE;
    protected static int bombRadius = BOMB_RADIUS;
    protected static double playerSpeed = PLAYER_SPEED;

    protected int screenDelay = SCREEN_DELAY;

    private final Keyboard input;
    private boolean isRunning = false;
    private boolean isMenu = true;
    private boolean isPaused = true;
    private boolean isSetting = false;
    private boolean isAboutPanel = false;
    public boolean isEndgame = false;
    public boolean isResetGame = false;
    private boolean isClickChangeMap = false;

    private final BoardRender boardRender;
    private final Screen screen;
    private final Frame frame;

    private final CodePanel codePanel;

    //this will be used to render the game, each render is a calculated image saved here
    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public GameRender(Frame frame) {
        this.frame = frame;
        frame.setTitle(TITLE);
        screen = new Screen(WIDTH, HEIGHT);
        input = new Keyboard();
        boardRender = new BoardRender(this, input, screen);
        codePanel = new CodePanel(frame);
        addKeyListener(input);
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    private void renderGame() { //render will run the maximum times it can per second
        BufferStrategy bs = getBufferStrategy(); //create a buffer to store images using canvas
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.clear();
        boardRender.render(screen);
        for (int i = 0; i < pixels.length; i++) { //create the image to be rendered
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        boardRender.renderMessages(g);
        g.dispose(); //release resources
        bs.show(); //make next buffer visible
    }

    public void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.clear();
        Graphics g = bs.getDrawGraphics();
        boardRender.drawScreen(g);
        g.dispose();
        bs.show();
    }

    private void update() {
        input.update();
        boardRender.update();
    }

    public void start() {
        readHighScore();
        mainAudio.playSound(100);

        ExecutorService executorService = Executors.newFixedThreadPool(3); // For menu, game, and UI update threads
        isRunning = true;

        // Menu rendering thread - loops until player starts the game
        executorService.submit(() -> {
            while (isMenu) {
                renderScreen();
                try {
                    Thread.sleep(16); // Approx. 60 FPS for menu screen
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Wait until player starts the game
        waitForStart();

        // Once the game starts, begin the game loop and other tasks
        boardRender.changeLevel(1);
        requestFocus();

        // Game logic and rendering thread
        executorService.submit(() -> {
            long lastTime = System.nanoTime();
            final double ns = 1000000000.0 / 60.0; // 60 updates per second
            double delta = 0;
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                // Update game state based on delta time
                while (delta >= 1) {
                    update();
                    delta--;
                }
                if (!isPaused) {
                    frame.getInfoPanel().setVisible(true);
                } else {
                    frame.getInfoPanel().setVisible(isSetting || isEndgame || isResetGame);
                }
                // Render either pause screen or game based on pause state
                if (isPaused) {
                    if (screenDelay <= 0) {
                        boardRender.setShow(5);
                        isPaused = false;
                    }
                    renderScreen();
                } else {
                    renderGame();
                }
                try {
                    Thread.sleep(16); // Approx. 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // UI update thread - updates info panel and other UI elements every second
        executorService.submit(() -> {
            long timer = System.currentTimeMillis();
            while (isRunning) {
                if (System.currentTimeMillis() - timer > 1000) {
                    frame.setTime(boardRender.subtractTime());
                    frame.setLives(boardRender.getLives());
                    frame.setPoints(boardRender.getPoints());
                    timer += 1000;
                    frame.setTitle(TITLE);
                    if (boardRender.getShow() == 2) {
                        --screenDelay;
                    }
                }
                try {
                    Thread.sleep(1000); // Update UI every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Shutdown the executor service on application close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }));
    }

    private void waitForStart() {
        // Wait in a loop until the player chooses to start the game
        while (isMenu) {
            // You might replace this with a real check (e.g., button press, key event)
            try {
                Thread.sleep(16); // Approx. 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void readHighScore() {
//        ConnectJBDC connectJBDC = new ConnectJBDC();
//        Integer newHighScore = connectJBDC.getLastHighScore();
//        if(newHighScore != null) {
//            highScore = newHighScore;
//        }
    }

    public void saveHighScore() {
//       ConnectJBDC connectJBDC = new ConnectJBDC();
//       connectJBDC.addHighScore(highScore);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle playButton = new Rectangle(45 * SCALE, 95 * SCALE, 105 * SCALE, 20 * SCALE);
        Rectangle optionButton = new Rectangle(45 * SCALE, 130 * SCALE, 105 * SCALE, 20 * SCALE);
        Rectangle aboutButton = new Rectangle(45 * SCALE, 165 * SCALE, 105 * SCALE, 20 * SCALE);
        if (playButton.contains(e.getX(), e.getY()) && isMenu) {
            isMenu = false;
            isRunning = true;
        }
        if (optionButton.contains(e.getX(), e.getY()) && isMenu && !isSetting) {
            isSetting = true;
            getBoard().setShow(5);
        }
        if (aboutButton.contains(e.getX(), e.getY()) && isMenu) {
            isAboutPanel = true;
            getBoard().setShow(6);
        }
        Rectangle exitAboutButton = new Rectangle(GameRender.WIDTH + 370, GameRender.HEIGHT - 100, 60, 60);
        if (exitAboutButton.contains(e.getX(), e.getY()) && isAboutPanel) {
            isAboutPanel = false;
            getBoard().setShow(4);
        }
        Rectangle exitSettingButton = new Rectangle(GameRender.WIDTH + 300, GameRender.HEIGHT - 60, 50, 50);
        if (exitSettingButton.contains(e.getX(), e.getY()) && (isPaused || isSetting)) {
            if (isMenu) {
                isSetting = false;
                getBoard().setShow(4);
            } else {
                isSetting = false;
                getBoard().gameResume();
            }
            if (isClickChangeMap) {
                if(screen.isBasicMap){
                    map.modifySpriteSheet("/textures/sand.png", 64);
                    changeMap();
                    frame.getInfoPanel().changeBackground(desertColor);
                    screen.isBasicMap = false;
                }else{
                    map.modifySpriteSheet("/res/textures/grass.png", 64);
                    changeMap();
                    frame.getInfoPanel().changeBackground(basicColor);
                    screen.isBasicMap = true;
                }
                isClickChangeMap = false;
            }
        }
        Rectangle changeMapButton = new Rectangle(GameRender.WIDTH + 270, GameRender.HEIGHT + 40, 30, 30);
        Rectangle changeMapButton_1 = new Rectangle(GameRender.WIDTH + 70, GameRender.HEIGHT + 40, 30, 30);
        if (changeMapButton.contains(e.getX(), e.getY()) && isSetting) {
            isClickChangeMap = true;
            if (screen.isBasicMap) {
                map.modifySpriteSheet("/textures/sand.png", 64);
                screen.isBasicMap = false;
            } else {
                map.modifySpriteSheet("/textures/grass.png", 64);
                screen.isBasicMap = true;
            }
        }
        if (changeMapButton_1.contains(e.getX(), e.getY()) && isSetting) {
            isClickChangeMap = true;
            if (screen.isBasicMap) {
                map.modifySpriteSheet("/textures/sand.png", 64);
                screen.isBasicMap = false;
            } else {
                map.modifySpriteSheet("/textures/grass.png", 64);
                screen.isBasicMap = true;
            }
        }
        Rectangle codeButton = new Rectangle(GameRender.WIDTH - 60, GameRender.HEIGHT + 140, 120, 50);
        if (codeButton.contains(e.getX(), e.getY()) && isSetting) {
            codePanel.setVisible(true);
        }
        Rectangle okButton = new Rectangle(GameRender.WIDTH + 90, GameRender.HEIGHT + 240, 100, 50);
        if (okButton.contains(e.getX(), e.getY()) && (isSetting)) {
            if (isMenu) {
                isSetting = false;
                getBoard().setShow(4);
            } else {
                isSetting = false;
                getBoard().gameResume();
            }
            if (screen.isBasicMap) {
                frame.getInfoPanel().changeBackground(basicColor);
                changeMap();
            } else {
                changeMap();
                frame.getInfoPanel().changeBackground(desertColor);
            }
        }
        Rectangle confirmNewGame = new Rectangle(GameRender.WIDTH + 150, GameRender.HEIGHT + 100, 100, 40);
        if (confirmNewGame.contains(e.getX(), e.getY()) && isResetGame) {
            getBoard().newGame();
            isResetGame = false;
        }
        Rectangle exitNewGame = new Rectangle(GameRender.WIDTH - 10, GameRender.HEIGHT + 100, 100, 40);
        if(exitNewGame.contains(e.getX(), e.getY()) && isResetGame){
            getBoard().gameResume();
            isResetGame = false;
        }
        Rectangle replayButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 170, 150, 50);
        if (replayButton.contains(e.getX(), e.getY()) && isEndgame) {
            boardRender.newGame();
            isEndgame = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle playButton = new Rectangle(45 * SCALE, 95 * SCALE, 105 * SCALE, 20 * SCALE);
        Rectangle optionButton = new Rectangle(45 * SCALE, 130 * SCALE, 105 * SCALE, 20 * SCALE);
        Rectangle aboutButton = new Rectangle(45 * SCALE, 165 * SCALE, 105 * SCALE, 20 * SCALE);
        if (isMenu && !isSetting && !isAboutPanel) {
            if (playButton.contains(e.getX(), e.getY())
                    || optionButton.contains(e.getX(), e.getY())
                    || aboutButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        Rectangle exitAbout = new Rectangle(GameRender.WIDTH + 370, GameRender.HEIGHT - 100, 60, 60);
        if (isAboutPanel) {
            if (exitAbout.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        Rectangle exitSettingButton = new Rectangle(GameRender.WIDTH + 300, GameRender.HEIGHT - 60, 50, 50);
        Rectangle changeMapButton = new Rectangle(GameRender.WIDTH + 270, GameRender.HEIGHT + 40, 30, 30);
        Rectangle changeMapButton_1 = new Rectangle(GameRender.WIDTH + 70, GameRender.HEIGHT + 40, 30, 30);
        Rectangle okButton = new Rectangle(GameRender.WIDTH + 90, GameRender.HEIGHT + 240, 100, 50);
        Rectangle codeButton = new Rectangle(GameRender.WIDTH - 60, GameRender.HEIGHT + 140, 120, 50);
        if (isSetting) {
            if (exitSettingButton.contains(e.getX(), e.getY())
                    || changeMapButton.contains(e.getX(), e.getY())
                    || changeMapButton_1.contains(e.getX(), e.getY())
                    || codeButton.contains(e.getX(), e.getY())
                    || okButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
            if (isMenu) {
                if (exitSettingButton.contains(e.getX(), e.getY())
                        || changeMapButton.contains(e.getX(), e.getY())
                        || changeMapButton_1.contains(e.getX(), e.getY())
                        || codeButton.contains(e.getX(), e.getY())
                        || okButton.contains(e.getX(), e.getY())) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }
        Rectangle replayButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 170, 150, 50);
        if (isEndgame) {
            if (replayButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        Rectangle confirmNewGame = new Rectangle(GameRender.WIDTH + 150, GameRender.HEIGHT + 100, 100, 40);
        Rectangle exitNewGame = new Rectangle(GameRender.WIDTH - 10, GameRender.HEIGHT + 100, 100, 40);
        if (isResetGame) {
            if (confirmNewGame.contains(e.getX(), e.getY())
                    || exitNewGame.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
        if (!isMenu && !isSetting && !isEndgame && !isResetGame) {
            setCursor(Cursor.getDefaultCursor());
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // Getters & Setters
    public static double getPlayerSpeed() {
        return playerSpeed;
    }

    public static int getBombRate() {
        return bombRate;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static void addPlayerSpeed(double i) {
        playerSpeed += i;
    }

    public static void addBombRadius(int i) {
        bombRadius += i;
    }

    public static void addBombRate(int i) {
        bombRate += i;
    }

    public void resetScreenDelay() {
        screenDelay = SCREEN_DELAY;
    }

    public BoardRender getBoard() {
        return boardRender;
    }

    public void run() {
        isRunning = true;
        isPaused = false;
    }

    public boolean getMenu() {
        return isMenu;
    }

    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        isPaused = true;
    }

    public void setSetting(boolean _isSetting) {
        this.isSetting = _isSetting;
    }

    public boolean isSetting() {
        return isSetting;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        GameRender.highScore = highScore;
    }

    public Screen getScreen() {
        return screen;
    }

    public void changeMap() {
        portal.changeSheet(map);
        bunker.changeSheet(map);
        grass.changeSheet(map);
        brick.changeSheet(map);
        brick_exploded.changeSheet(map);
        brick_exploded1.changeSheet(map);
        brick_exploded2.changeSheet(map);
        wall_top.changeSheet(map);
        wall_left.changeSheet(map);
        wall_right.changeSheet(map);
        wall_down.changeSheet(map);
        wall_corner0.changeSheet(map);
        wall_corner1.changeSheet(map);
        wall_corner2.changeSheet(map);
        wall_corner3.changeSheet(map);
    }
}
