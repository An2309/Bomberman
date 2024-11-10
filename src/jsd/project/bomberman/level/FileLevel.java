package jsd.project.bomberman.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

import jsd.project.bomberman.BoardRender;
import jsd.project.bomberman.CommonVariables;
import jsd.project.bomberman.GameRender;
import jsd.project.bomberman.entities.LayerEntity;
import jsd.project.bomberman.entities.character.Player;
import jsd.project.bomberman.entities.character.enemy.Balloom;
import jsd.project.bomberman.entities.character.enemy.Doll;
import jsd.project.bomberman.entities.character.enemy.Kondoria;
import jsd.project.bomberman.entities.character.enemy.Minvo;
import jsd.project.bomberman.entities.character.enemy.Oneal;
import jsd.project.bomberman.entities.tile.Surface;
import jsd.project.bomberman.entities.tile.Portal;
import jsd.project.bomberman.entities.tile.Wall;
import jsd.project.bomberman.entities.tile.destroyable.Brick;
import jsd.project.bomberman.entities.tile.power.PowerBombs;
import jsd.project.bomberman.entities.tile.power.PowerFlames;
import jsd.project.bomberman.entities.tile.power.PowerSpeed;

import jsd.project.bomberman.graphic.Screen;
import jsd.project.bomberman.graphic.Sprite;

public class FileLevel extends Level implements CommonVariables {

    public FileLevel(String path, BoardRender boardRender) {
        super(path, boardRender);
    }

    @Override
    public void loadLevel(String path) {
        try {
            URL absPath = FileLevel.class.getResource("/" + path);
            assert absPath != null;
            BufferedReader in = new BufferedReader(new InputStreamReader(absPath.openStream()));
            String data = in.readLine();
            StringTokenizer tokens = new StringTokenizer(data);
            level = Integer.parseInt(tokens.nextToken());
            height = Integer.parseInt(tokens.nextToken());
            width = Integer.parseInt(tokens.nextToken());
            lineTiles = new String[height];
            for (int i = 0; i < height; i++) {
                lineTiles[i] = in.readLine().substring(0, width);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Error loading level " + path);
        }
    }

    @Override
    public void createEntities() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                addLevelEntity(lineTiles[y].charAt(x), x, y);
            }
        }
    }

    public void addLevelEntity(char c, int x, int y) {
        int pos = x + y * getWidth();

        switch (c) {
            // Map
            case '6': // Top-left corner
                boardRender.addEntity(pos, new Wall(x, y, wall_corner0));
                break;
            case '7': // Top-right corner
                boardRender.addEntity(pos, new Wall(x, y, wall_corner1));
                break;
            case '8': // Bottom-right corner
                boardRender.addEntity(pos, new Wall(x, y, wall_corner2));
                break;
            case '9': // Bottom-left corner
                boardRender.addEntity(pos, new Wall(x, y, wall_corner3));
                break;
            case 'T': // Top border
                boardRender.addEntity(pos, new Wall(x, y, wall_top));
                break;
            case 'L': // Left border
                boardRender.addEntity(pos, new Wall(x, y, wall_left));
                break;
            case 'R': // Right border
                boardRender.addEntity(pos, new Wall(x, y, wall_right));
                break;
            case 'D': // Bottom border
                boardRender.addEntity(pos, new Wall(x, y, wall_down));
                break;
            case '#': // Wall
                boardRender.addEntity(pos, new Wall(x, y, bunker));
                break;
            case '*': // Brick wall
                boardRender.addEntity(pos, new LayerEntity(x, y,
                        new Surface(x, y, grass),
                        new Brick(x, y, brick)));
                break;
            case 'P': // Port
                boardRender.addEntity(pos, new LayerEntity(x, y,
                        new Surface(x, y, grass),
                        new Portal(x, y, boardRender, portal),
                        new Brick(x, y, brick)));
                break;
            case 'C': // Character
                matrix[y][x] = 1;
                boardRender.addMob(new Player(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                Screen.setOffset(0, 0);
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;

            // Items
            case 'B':
                LayerEntity layer = new LayerEntity(x, y,
                        new Surface(x, y, grass),
                        new Brick(x, y, brick));
                if (boardRender.isPowerUsed(x, y, level)) {
                    layer.addBeforeTop(new PowerBombs(x, y, level, Sprite.power_bombs));
                }
                boardRender.addEntity(pos, layer);
                break;
            case 'S':
                layer = new LayerEntity(x, y,
                        new Surface(x, y, grass),
                        new Brick(x, y, brick));
                if (boardRender.isPowerUsed(x, y, level)) {
                    layer.addBeforeTop(new PowerSpeed(x, y, level, Sprite.power_speed));
                }
                boardRender.addEntity(pos, layer);
                break;
            case 'F':
                layer = new LayerEntity(x, y,
                        new Surface(x, y, grass),
                        new Brick(x, y, brick));
                if (boardRender.isPowerUsed(x, y, level)) {
                    layer.addBeforeTop(new PowerFlames(x, y, level, Sprite.power_flames));
                }
                boardRender.addEntity(pos, layer);
                break;

            // Enemies
            case '1':
                matrix[y][x] = 1;
                boardRender.addMob(new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;
            case '2':
                matrix[y][x] = 1;
                boardRender.addMob(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;
            case '3':
                matrix[y][x] = 1;
                boardRender.addMob(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;
            case '4':
                matrix[y][x] = 1;
                boardRender.addMob(new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;
            case '5':
                matrix[y][x] = 1;
                boardRender.addMob(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + GameRender.TILES_SIZE, boardRender));
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;

            default: // Surface
                matrix[y][x] = 1;
                boardRender.addEntity(pos, new Surface(x, y, grass));
                break;
        }
    }

}
