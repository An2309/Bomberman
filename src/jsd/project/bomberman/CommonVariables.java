package jsd.project.bomberman;

import jsd.project.bomberman.audio.Audio;
import jsd.project.bomberman.graphic.Sprite;
import jsd.project.bomberman.graphic.SpriteSheet;

import java.awt.*;

public interface CommonVariables {
    String TITLE = "Bomberman";
    int SCALE = 3;

    int BOMB_RATE = 1;
    int BOMB_RADIUS = 1;
    double PLAYER_SPEED = 1.0;

    int TIME = 200;
    int POINTS = 0;
    int LIVES = 3;

    int SCREEN_DELAY = 2;

    int MAP_WIDTH = 31;
    int MAP_HEIGHT = 13;
    int[][] matrix = new int[MAP_HEIGHT][MAP_WIDTH]; // map size

    int transparentColor = 0xffff00ff; //pink with alpha channel

    int MAX_ANIMATE = 7500; //save the animation status and don't let this get too big

    Audio mainAudio = new Audio("res/sound/Stage.wav");
    Audio placeBombAudio = new Audio("res/sound/PutBomb.wav");
    Audio explosionBombAudio = new Audio("res/sound/Explosion.wav");
    Audio deadAudio = new Audio("res/sound/Hit.wav");
    Audio upItemAudio = new Audio("res/sound/UpItem.wav");
    Audio brickBreakAudio = new Audio("res/sound/Crystal.wav");

    SpriteSheet character = new SpriteSheet("/textures/character.png", 64);
    SpriteSheet bomb = new SpriteSheet("/textures/bomb.png", 64);
    SpriteSheet item = new SpriteSheet("/textures/item.png", 64);
    SpriteSheet enemy = new SpriteSheet("/textures/enemy.png", 128);
    SpriteSheet map = new SpriteSheet("/textures/grass.png", 64);

    Sprite portal = new Sprite(16, 0, 0, SpriteSheet.map, 14, 14);
    Sprite bunker = new Sprite(16, 1, 0, SpriteSheet.map, 16, 16);
    Sprite grass = new Sprite(16, 2, 0, SpriteSheet.map, 16, 16);

    Sprite brick = new Sprite(16, 0, 1, SpriteSheet.map, 16, 16);
    Sprite brick_exploded = new Sprite(16, 1, 1, SpriteSheet.map, 16, 16);
    Sprite brick_exploded1 = new Sprite(16, 2, 1, SpriteSheet.map, 16, 16);
    Sprite brick_exploded2 = new Sprite(16, 3, 1, SpriteSheet.map, 16, 16);

    Sprite wall_top = new Sprite(16, 0, 2, SpriteSheet.map, 16, 16);
    Sprite wall_left = new Sprite(16, 1, 2, SpriteSheet.map, 16, 16);
    Sprite wall_right = new Sprite(16, 2, 2, SpriteSheet.map, 16, 16);
    Sprite wall_down = new Sprite(16, 3, 2, SpriteSheet.map, 16, 16);

    Sprite wall_corner0 = new Sprite(16, 0, 3, SpriteSheet.map, 16, 16);
    Sprite wall_corner1 = new Sprite(16, 1, 3, SpriteSheet.map, 16, 16);
    Sprite wall_corner2 = new Sprite(16, 2, 3, SpriteSheet.map, 16, 16);
    Sprite wall_corner3 = new Sprite(16, 3, 3, SpriteSheet.map, 16, 16);

    Color basicColor = new Color(99, 171, 63, 255);
    Color desertColor = new Color(240, 181, 65, 255);
}
