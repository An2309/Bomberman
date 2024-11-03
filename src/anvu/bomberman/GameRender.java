package anvu.bomberman;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;


import anvu.bomberman.graphic.Screen;
import anvu.bomberman.gui.CodePanel;
import anvu.bomberman.gui.Frame;
import anvu.bomberman.input.Keyboard;

public class GameRender extends Canvas implements MouseListener, MouseMotionListener, CommonVariables {
    public static final double VERSION = 1.9;
    public static final int TILES_SIZE = 16,
            WIDTH = TILES_SIZE * (int) (31 / 2), //minus one to ajust the window,
            HEIGHT = 13 * TILES_SIZE;

    public static int highScore = 0;

    protected static int bombRate = BOMBRATE;
    protected static int bombRadius = BOMBRADIUS;
    protected static double playerSpeed = PLAYERSPEED;

    protected int _screenDelay = SCREENDELAY;

    private final Keyboard _input;
    private boolean _running = false;
    private boolean _menu = true;
    private boolean _paused = true;
    private boolean isSetting = false;
    private boolean isAboutPane = false;
    public boolean isEndgame = false;
    public boolean isResetGame = false;
    private boolean isClickChangeMap = false;

    private final BoardRender _boardRender;
    private final Screen screen;
    private final Frame _frame;

    private final CodePanel codePanel;

    //this will be used to render the game, each render is a calculated image saved here
    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public GameRender(Frame frame) {
        _frame = frame;
        _frame.setTitle(TITLE);

        screen = new Screen(WIDTH, HEIGHT);
        _input = new Keyboard();

        _boardRender = new BoardRender(this, _input, screen);
        codePanel = new CodePanel(_frame);
        addKeyListener(_input);
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    private void renderGame() { //render will run the maximum times it can per second
        BufferStrategy bs = getBufferStrategy(); //create a buffer to store images using canvas
        if (bs == null) { //if canvas don't have a bufferstrategy, create it
            createBufferStrategy(3); //triple buffer
            return;
        }

        screen.clear();

        _boardRender.render(screen);

        for (int i = 0; i < pixels.length; i++) { //create the image to be rendered
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        _boardRender.renderMessages(g);

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

        _boardRender.drawScreen(g);

        g.dispose();
        bs.show();
    }

    private void update() {
        _input.update();
        _boardRender.update();
    }

    public void start() {
        readHighscore();
        /*
        JWindow window = new JWindow();
        window.getContentPane().add(
                new JLabel("", new ImageIcon("res/textures/intro.gif"), SwingConstants.CENTER));
        window.setBounds(600, 215, 720, 600);
        window.setVisible(true);
        try {
            Thread.sleep(4900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
         */
        mainAudio.playSound(100);
        while (_menu) {
            renderScreen();
        }

        _boardRender.changeLevel(1);


        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0; // nanosecond, 60 frames per second
        double delta = 0;
        int frames = 0;
        int updates = 0;
        requestFocus();

        while (_running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }

            if (!_paused) {
                _frame.get_infopanel().setVisible(true);
            } else {
                _frame.get_infopanel().setVisible(isSetting || isEndgame || isResetGame);
            }

            if (_paused) {
                if (_screenDelay <= 0) {
                    _boardRender.setShow(5);
                    _paused = false;
                }
                renderScreen();
            } else {
                renderGame();
            }


            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                _frame.setTime(_boardRender.subtractTime());
                _frame.setLives(_boardRender.getLives());
                _frame.setPoints(_boardRender.getPoints());
                timer += 1000;
                _frame.setTitle(TITLE);
                updates = 0;
                frames = 0;

                if (_boardRender.getShow() == 2)
                    --_screenDelay;
            }
        }
    }

    public void readHighscore() {
        BufferedReader read;
        try {
            read = new BufferedReader(new FileReader(new File("res/data/BestScore.txt")));
            String score = read.readLine().trim();
            if (score == null)
                highScore = 0;
            else
                highScore = Integer.parseInt(score);
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScore() {
        try {
            File file = new File("res/data/BestScore.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(highScore));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle playButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 130, 150, 50);
        if (playButton.contains(e.getX(), e.getY()) && _menu) {
            _menu = false;
            _running = true;
        }

        Rectangle optionButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 210, 150, 50);
        if (optionButton.contains(e.getX(), e.getY()) && _menu && !isSetting) {
            isSetting = true;
            getBoard().setShow(5);
        }

        Rectangle aboutButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 280, 150, 50);
        if (aboutButton.contains(e.getX(), e.getY()) && _menu) {
            isAboutPane = true;
            getBoard().setShow(6);
        }

        Rectangle exitAboutButton = new Rectangle(GameRender.WIDTH + 370, GameRender.HEIGHT - 100, 60, 60);
        if (exitAboutButton.contains(e.getX(), e.getY()) && isAboutPane) {
            isAboutPane = false;
            getBoard().setShow(4);
        }

        Rectangle exitSettingButton = new Rectangle(GameRender.WIDTH + 300, GameRender.HEIGHT - 60, 50, 50);
        if (exitSettingButton.contains(e.getX(), e.getY()) && (_paused || isSetting)) {
            if (_menu) {
                isSetting = false;
                getBoard().setShow(4);
            } else {
                isSetting = false;
                getBoard().gameResume();
            }
            if (isClickChangeMap) {
                if(screen.isBasicMap){
                    map.modifySpriteSheet("/textures/miramar.png", 64);
                    changeMap();
                    _frame.get_infopanel().changeBackground(desertColor);
                    screen.isBasicMap = false;
                }else{
                    map.modifySpriteSheet("/textures/erangel.png", 64);
                    changeMap();
                    _frame.get_infopanel().changeBackground(basicColor);
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
                map.modifySpriteSheet("/textures/miramar.png", 64);
                screen.isBasicMap = false;
            } else {
                map.modifySpriteSheet("/textures/erangel.png", 64);
                screen.isBasicMap = true;
            }
        }

        if (changeMapButton_1.contains(e.getX(), e.getY()) && isSetting) {
            isClickChangeMap = true;
            if (screen.isBasicMap) {
                map.modifySpriteSheet("/textures/miramar.png", 64);
                screen.isBasicMap = false;
            } else {
                map.modifySpriteSheet("/textures/erangel.png", 64);
                screen.isBasicMap = true;
            }
        }

        Rectangle codeButton = new Rectangle(GameRender.WIDTH - 60, GameRender.HEIGHT + 140, 120, 50);
        if (codeButton.contains(e.getX(), e.getY()) && isSetting) {
            codePanel.setVisible(true);
        }

        Rectangle okButton = new Rectangle(GameRender.WIDTH + 90, GameRender.HEIGHT + 240, 100, 50);
        if (okButton.contains(e.getX(), e.getY()) && (isSetting)) {
            if (_menu) {
                isSetting = false;
                getBoard().setShow(4);
            } else {
                isSetting = false;
                getBoard().gameResume();
            }
            if (screen.isBasicMap) {
                _frame.get_infopanel().changeBackground(basicColor);
                changeMap();
            } else {
                changeMap();
                _frame.get_infopanel().changeBackground(desertColor);
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
            _boardRender.newGame();
            isEndgame = false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle playButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 130, 150, 50);
        Rectangle optionButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 210, 150, 50);
        Rectangle aboutButton = new Rectangle(GameRender.WIDTH + 50, GameRender.HEIGHT + 280, 150, 50);

        if (_menu && !isSetting && !isAboutPane) {
            if (playButton.contains(e.getX(), e.getY())
                    || optionButton.contains(e.getX(), e.getY())
                    || aboutButton.contains(e.getX(), e.getY())) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }

        Rectangle exitAbout = new Rectangle(GameRender.WIDTH + 370, GameRender.HEIGHT - 100, 60, 60);
        if (isAboutPane) {
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
            if (_menu) {
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

        if (!_menu && !isSetting && !isEndgame && !isResetGame) {
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

    /*
    |--------------------------------------------------------------------------
    | Getters & Setters
    |--------------------------------------------------------------------------
     */
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

    //screen delay
    public int getScreenDelay() {
        return _screenDelay;
    }

    public void decreaseScreenDelay() {
        _screenDelay--;
    }

    public void resetScreenDelay() {
        _screenDelay = SCREENDELAY;
    }

    public Keyboard getInput() {
        return _input;
    }

    public BoardRender getBoard() {
        return _boardRender;
    }

    public Frame getFrame() {
        return _frame;
    }

    public void run() {
        _running = true;
        _paused = false;
    }

    public boolean getMenu() {
        return _menu;
    }

    public void stop() {
        _running = false;
    }

    public boolean isRunning() {
        return _running;
    }

    public boolean isPaused() {
        return _paused;
    }

    public void pause() {
        _paused = true;
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
