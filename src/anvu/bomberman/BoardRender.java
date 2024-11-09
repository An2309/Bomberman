package anvu.bomberman;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import anvu.bomberman.entities.Entity;
import anvu.bomberman.entities.Message;
import anvu.bomberman.entities.bomb.Bomb;
import anvu.bomberman.entities.bomb.Explosion;
import anvu.bomberman.entities.character.Character;
import anvu.bomberman.entities.character.Player;
import anvu.bomberman.entities.tile.power.Power;
import anvu.bomberman.graphic.IRender;
import anvu.bomberman.graphic.Screen;
import anvu.bomberman.input.Keyboard;
import anvu.bomberman.level.FileLevel;
import anvu.bomberman.level.Level;

public class BoardRender implements IRender {

    protected Level level;
    protected GameRender gameRender;
    protected Keyboard input;
    protected Screen screen;

    public Entity[] entities;
    public List<Character> mobs = new ArrayList<>();
    protected List<Bomb> bombs = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    private int screenToShow = -1;

    private int time = GameRender.TIME;
    private int points = GameRender.POINTS;
    private int lives = GameRender.LIVES;

    public BoardRender(GameRender gameRender, Keyboard input, Screen screen) {
        this.gameRender = gameRender;
        this.input = input;
        this.screen = screen;
        screenToShow = 4;
    }

    // Render & Update
    @Override
    public void update() {
        if (gameRender.isPaused()) return;
        updateEntities();
        updateMobs();
        updateBombs();
        updateMessages();
        detectEndGame();
        for (int i = 0; i < mobs.size(); i++) {
            Character a = mobs.get(i);
            if (a.isRemoved()) mobs.remove(i);
        }
    }

    @Override
    public void render(Screen screen) {
        if (gameRender.isPaused()) return;
        //only render the visible part of screen
        int x0 = Screen.xOffset >> 4; //tile precision, -> left X
        int x1 = (Screen.xOffset + screen.getWidth() + GameRender.TILES_SIZE) / GameRender.TILES_SIZE; // -> right X
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / GameRender.TILES_SIZE; //render one tile plus to fix black margins
        for (int y = y0; y < y1; y++) {
            for (int x = x0; x < x1; x++) {
                entities[x + y * level.getWidth()].render(screen);
            }
        }
        renderBombs(screen);
        renderMobs(screen);
    }

    // ChangeLevel
    public void newGame() {
        resetProperties();
        changeLevel(1);
    }

    @SuppressWarnings("static-access")
    private void resetProperties() {
        points = GameRender.POINTS;
        lives = GameRender.LIVES;
        Player.powers.clear();
        gameRender.playerSpeed = 1.0;
        gameRender.bombRadius = 1;
        gameRender.bombRate = 1;
    }

    public void restartLevel() {
        changeLevel(level.getLevel());
    }

    public void nextLevel() {
        changeLevel(level.getLevel() + 1);
    }

    public void changeLevel(int level) {
        try {
            time = GameRender.TIME;
            screenToShow = 2;
            gameRender.resetScreenDelay();
            gameRender.pause();
            mobs.clear();
            bombs.clear();
            messages.clear();
            this.level = new FileLevel("levels/Level" + level + ".txt", this);
            entities = new Entity[this.level.getHeight() * this.level.getWidth()];
            this.level.createEntities();
        } catch (Exception e) {
            endGame();
        }

    }

    public void changeLevelByCode(String str) {
        int i = level.validCode(str);
        if (i != -1) {
            if (gameRender.isSetting()) {
                gameRender.setSetting(false);
            }
            changeLevel(i + 1);
        }
    }

    public boolean isPowerUsed(int x, int y, int level) {
        Power p;
        for (int i = 0; i < Player.powers.size(); i++) {
            p = Player.powers.get(i);
            if (p.getX() == x && p.getY() == y && level == p.getLevel())
                return false;
        }

        return true;
    }

    // Detections
    protected void detectEndGame() {
        if (time <= 0)
            restartLevel();
    }

    public void endGame() {
        screenToShow = 1;
        gameRender.resetScreenDelay();
        gameRender.pause();
        gameRender.isEndgame = true;
        if(getPoints() >= gameRender.getHighScore()){
            gameRender.setHighScore(getPoints());
            gameRender.saveHighScore();
        }
    }

    public boolean detectNoEnemies() {
        int total = 0;
        for (Character mob : mobs) {
            if (!(mob instanceof Player))
                ++total;
        }
        return total == 0;
    }

    // Pause & Resume
    public void gamePauseOnSetting() {
        gameRender.resetScreenDelay();
        screenToShow = 5;
        gameRender.pause();
        gameRender.setSetting(true);
    }

    public void gamePauseOnReset(){
        gameRender.resetScreenDelay();
        screenToShow = 7;
        gameRender.pause();
        gameRender.isResetGame = true;
    }

    public void gameResume() {
        gameRender.resetScreenDelay();
        screenToShow = -1;
        gameRender.run();
    }

    // Screens
    public void drawScreen(Graphics g) {
        screen.intializeFont();
        switch (screenToShow) {
            case 1:
                screen.drawEndGame(g, points, gameRender.getHighScore(), level.getLevel());
                break;
            case 2:
                screen.drawChangeLevel(g, level.getLevel());
                break;
            case 3:
                screen.drawPaused(g);
                break;
            case 4:
                screen.drawMenu(g);
                break;
            case 5:
                screen.drawSetting(g);
                break;
            case 6:
                screen.drawAbout(g);
                break;
            case 7:
                screen.drawChooseNewGame(g);
                break;
        }
    }

    // Getters And Setters
    public Entity getEntity(double x, double y, Character m) {
        Entity res = null;
        res = getExplosionAt((int) x, (int) y);
        if (res != null) return res;
        res = getBombAt(x, y);
        if (res != null) return res;
        res = getMobAtExcluding((int) x, (int) y, m);
        if (res != null) return res;
        res = getEntityAt((int) x, (int) y);
        return res;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public Bomb getBombAt(double x, double y) {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.getX() == (int) x && b.getY() == (int) y)
                return b;
        }
        return null;
    }

    public Character getMobAt(double x, double y) {
        Iterator<Character> itr = mobs.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();

            if (cur.getXTile() == x && cur.getYTile() == y)
                return cur;
        }
        return null;
    }

    public Player getPlayer() {
        Iterator<Character> itr = mobs.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();

            if (cur instanceof Player)
                return (Player) cur;
        }
        return null;
    }

    public Character getMobAtExcluding(int x, int y, Character a) {
        Iterator<Character> itr = mobs.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur == a) {
                continue;
            }
            if (cur.getXTile() == x && cur.getYTile() == y) {
                return cur;
            }
        }

        return null;
    }

    public Explosion getExplosionAt(int x, int y) {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            Explosion e = b.explosionAt(x, y);
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    public Entity getEntityAt(double x, double y) {
        return entities[(int) x + (int) y * level.getWidth()];
    }

    // Adds and Removes
    public void addEntity(int pos, Entity e) {
        entities[pos] = e;
    }

    public void addMob(Character e) {
        mobs.add(e);
    }

    public void addBomb(Bomb e) {
        bombs.add(e);
    }

    public void addMessage(Message e) {
        messages.add(e);
    }

    // Renders
    protected void renderMobs(Screen screen) {
        for (Character mob : mobs) mob.render(screen);
    }

    protected void renderBombs(Screen screen) {
        for (Bomb bomb : bombs) bomb.render(screen);
    }

    public void renderMessages(Graphics g) {
        Message m;
        for (Message message : messages) {
            m = message;
            g.setFont(new Font("Arial", Font.PLAIN, m.getSize()));
            g.setColor(m.getColor());
            g.drawString(m.getMessage(), (int) m.getX() - Screen.xOffset * GameRender.SCALE, (int) m.getY());
        }
    }

    // Updates
    protected void updateEntities() {
        if (gameRender.isPaused()) return;
        for (Entity entity : entities) {
            entity.update();
        }
    }

    protected void updateMobs() {
        if (gameRender.isPaused()) return;
        Iterator<Character> itr = mobs.iterator();

        while (itr.hasNext() && !gameRender.isPaused())
            itr.next().update();
    }

    protected void updateBombs() {
        if (gameRender.isPaused()) return;
        for (Bomb bomb : bombs) bomb.update();
    }

    protected void updateMessages() {
        if (gameRender.isPaused()) return;
        Message m;
        int left = 0;
        for (int i = 0; i < messages.size(); i++) {
            m = messages.get(i);
            left = m.getDuration();
            if (left > 0)
                m.setDuration(--left);
            else
                messages.remove(i);
        }
    }

    // Getters & Setters
    public Keyboard getInput() {
        return input;
    }

    public Level getLevel() {
        return level;
    }

    public int getShow() {
        return screenToShow;
    }

    public void setShow(int i) {
        screenToShow = i;
    }

    public int getTime() {
        return time;
    }

    public int getLives() {
        return lives;
    }

    public int subtractTime() {
        if (gameRender.isPaused())
            return this.time;
        else
            return this.time--;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addLives(int lives) {
        this.lives += lives;
    }

    public int getWidth() {
        return level.getWidth();
    }
}