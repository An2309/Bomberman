package anvu.bomberman.entities.character.enemy.algorithm;

import anvu.bomberman.entities.character.Player;
import anvu.bomberman.entities.character.enemy.Enemy;

import java.util.List;

import static anvu.bomberman.CommonVariables.matrix;
import static anvu.bomberman.entities.character.enemy.algorithm.BFS.shortestPath;

public class HighAlgo extends Algorithm {
    Player player;
    Enemy enemy;

    public HighAlgo(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    @Override
    public int getDirection() {
        if (player == null) {
            return random.nextInt(4);
        }
        List<Point> path = shortestPath(matrix,
                new Point(player.getYTile(), player.getXTile()),
                new Point(enemy.getYTile(), enemy.getXTile()));
        if (path == null) {
            return random.nextInt(4);
        } else if (path.size() == 1) {
            return -1;
        } else {
            return calculateDirection(path.get(0), path.get(1));
        }
    }

    protected int calculateDirection(Point start, Point end) {
        if (end.row < start.row) return 0;
        if (end.col > start.col) return 1;
        if (end.row > start.row) return 2;
        if (end.col < start.col) return 3;
        return -1;
    }
}
