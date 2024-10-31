package anvu.bomberman.level;

import anvu.bomberman.BoardRender;
import anvu.bomberman.CommonVariables;
import anvu.bomberman.graphic.Screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;

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
//                addLevelEntity(lineTiles[y].charAt(x), x, y);
            }
        }
    }

//    public void addLevelEntity(char c, int x, int y) {
//        int pos = x + y * getWidth();
//
//        // Map
//        switch (c) {
//            // Top-left corner
//            case '6':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_corner0));
//                break;
//
//            // Top-right corner
//            case '7':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_corner1));
//                break;
//
//            // Bottom-right corner
//            case '8':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_corner2));
//                break;
//
//            // Bottom-left corner
//            case '9':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_corner3));
//                break;
//
//            // Top border
//            case 'T':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_top));
//                break;
//
//            // Left border
//            case 'L':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_left));
//                break;
//
//            // Right border
//            case 'R':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_right));
//                break;
//
//            // Bottom border
//            case 'D':
//                boardRender.addEntitie(pos, new WallTile(x, y, wall_down));
//                break;
//
//            // Wall
//            case '#':
//                boardRender.addEntitie(pos, new WallTile(x, y, bunker));
//                break;
//
//            // Brick wall
//            case '*':
//                boardRender.addEntitie(pos, new LayeredEntity(x, y,
//                        new SurfaceTile(x, y, grass),
//                        new BrickTile(x, y, brick)));
//                break;
//
//            // Port
//            case 'P':
//                boardRender.addEntitie(pos, new LayeredEntity(x, y,
//                        new SurfaceTile(x, y, grass),
//                        new PortalTile(x, y, board, portal),
//                        new BrickTile(x, y, brick)));
//                break;
//
//            // Character
//            case 'C':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Player(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                Screen.setOffset(0, 0);
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//
//            // Items
//            case 'B':
//                LayeredEntity layer = new LayeredEntity(x, y,
//                        new SurfaceTile(x, y, grass),
//                        new BrickTile(x, y, brick));
//
////                if (!boardRender.isPowerupUsed(x, y, level)) {
////                    layer.addBeforeTop(new PowerupBombs(x, y, level, Sprite.powerup_bombs));
////                }
//
//                boardRender.addEntitie(pos, layer);
//                break;
//            case 'S':
//                layer = new LayeredEntity(x, y,
//                        new SurfaceTile(x, y, grass),
//                        new BrickTile(x, y, brick));
//
//                if (!boardRender.isPowerupUsed(x, y, level)) {
//                    layer.addBeforeTop(new PowerupSpeed(x, y, level, Sprite.powerup_speed));
//                }
//
//                boardRender.addEntitie(pos, layer);
//                break;
//            case 'F':
//                layer = new LayeredEntity(x, y,
//                        new SurfaceTile(x, y, grass),
//                        new BrickTile(x, y, brick));
//
//                if (!boardRender.isPowerupUsed(x, y, level)) {
//                    layer.addBeforeTop(new PowerupFlames(x, y, level, Sprite.powerup_flames));
//                }
//
//                boardRender.addEntitie(pos, layer);
//                break;
//
//            // Enemies
//            case '1':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//            case '2':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//            case '3':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//            case '4':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//            case '5':
//                matrix[y][x] = 1;
//                boardRender.addMob(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, board));
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//
//            default: // Surface
//                matrix[y][x] = 1;
//                boardRender.addEntitie(pos, new SurfaceTile(x, y, grass));
//                break;
//        }
//    }
}
