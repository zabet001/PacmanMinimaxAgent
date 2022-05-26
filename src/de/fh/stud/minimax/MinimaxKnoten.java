package de.fh.stud.minimax;

import de.fh.kiServer.util.Vector2;
import de.fh.pacman.GhostInfo;
import de.fh.pacman.enums.PacmanTileType;

import java.util.List;
import java.util.stream.Collectors;

public class MinimaxKnoten {
    static final int POWERPILL_DURATION = 9;
    static final int HUNTER_PAUSE = 5;

    MinimaxKnoten predecessor;
    int activeAgent;

    Vector2 pacmanPos;

    List<Vector2> ghostPos;
    final List<Vector2> ghostSpawn;
    final List<String> ghostType;
    List<Integer> ghostPillTimer;
    List<Integer> ghostRespawnTimer;

    PacmanTileType[][] view;

    final int costSum;
    final int remainingDots;

    final int nodeDepth;
    float nodeRating;

    private MinimaxKnoten(PacmanTileType[][] world, int pacmanPosX, int pacmanPosY, List<GhostInfo> ghostInfos) {
        this(null, pacmanPosX, pacmanPosY, ghostInfos.stream().map(GhostInfo::getPos).collect(Collectors.toList()),
             ghostInfos.stream().map(GhostInfo::getSpawn).collect(Collectors.toList()),
             ghostInfos.stream().map(GhostInfo::getType).collect(Collectors.toList()),
             ghostInfos.stream().map(GhostInfo::getPillTimer).collect(Collectors.toList()),
             ghostInfos.stream().map(GhostInfo::getRespawnTimer).collect(Collectors.toList()), world);

    }

    private MinimaxKnoten(MinimaxKnoten pred, int pacmanPosX, int pacmanPosY, List<Vector2> ghostPos,
                          List<Vector2> ghostSpawns, List<String> ghostType, List<Integer> ghostPillTimer,
                          List<Integer> ghostRespawnTimer, PacmanTileType[][] view) {
        this.predecessor = pred;

        this.pacmanPos = new Vector2(pacmanPosX, pacmanPosY);

        this.ghostPos = copyPosList(ghostPos);

        this.ghostSpawn = ghostSpawns;

        this.ghostType = ghostType;

        this.ghostPillTimer = List.copyOf(ghostPillTimer);

        this.ghostRespawnTimer = List.copyOf(ghostRespawnTimer);

        if (pred == null) {
            activeAgent = 0;
            this.view = view;
            removeOfAgents(view);
            this.costSum = this.nodeDepth = 0;
            this.remainingDots = countDots(this.view);

        }
        else {
            this.activeAgent = (pred.activeAgent + 1);
            if (activeAgent > ghostPos.size() + 1) {
                activeAgent = 0;
            }
            costSum = activeAgent == 0 ? pred.costSum + 1 : pred.costSum;
            this.view = copyWorld(pred.view);
            if (pred.view[pacmanPosX][pacmanPosY] == PacmanTileType.DOT) {
                this.view[pacmanPosX][pacmanPosY] = PacmanTileType.EMPTY;
                this.remainingDots = pred.remainingDots - 1;
            }
            else {
                this.remainingDots = pred.remainingDots;
                if (pred.view[pacmanPosX][pacmanPosY] == PacmanTileType.POWERPILL) {
                    for (int i = 0; i < ghostPillTimer.size(); i++) {
                        ghostPillTimer.set(i, POWERPILL_DURATION);
                    }
                    this.view[pacmanPosX][pacmanPosY] = PacmanTileType.EMPTY;
                }
            }
            this.nodeDepth = pred.nodeDepth + 1;

        }

    }

    public void removeOfAgents(PacmanTileType[][] view) {
        for (int i = 0; i < view.length; i++) {
            for (int j = 0; j < view[0].length; j++) {
                if (view[i][j] == PacmanTileType.GHOST || view[i][j] == PacmanTileType.PACMAN) {
                    view[i][j] = PacmanTileType.EMPTY;
                }
                else if (view[i][j] == PacmanTileType.GHOST_AND_DOT) {
                    view[i][j] = PacmanTileType.DOT;
                }
                else if (view[i][j] == PacmanTileType.GHOST_AND_POWERPILL) {
                    view[i][j] = PacmanTileType.POWERPILL;
                }
            }
        }
    }

    public short countDots(PacmanTileType[][] view) {
        short cnt = 0;
        for (int row = 0; row < view.length; row++) {
            for (int col = 0; col < view[0].length; col++) {
                if (view[row][col] == PacmanTileType.DOT) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    private static <T extends List<Vector2>> List<Vector2> copyPosList(T orig) {
        List<Vector2> ret;
        try {
            ret = orig.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }

        for (Vector2 pos : orig) {
            ret.add(new Vector2(pos.x, pos.y));
        }
        return ret;
    }

    private PacmanTileType[][] copyWorld(PacmanTileType[][] orig) {
        PacmanTileType[][] ret = new PacmanTileType[orig.length][];
        for (int i = 0; i < orig.length; i++) {
            ret[i] = new PacmanTileType[orig.length];
            for (int j = 0; j < orig[i].length; j++) {
                ret[i][j] = orig[i][j];
            }
        }
        return ret;
    }
}
