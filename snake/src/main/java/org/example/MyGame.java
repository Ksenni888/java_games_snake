package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static com.almasb.fxgl.dsl.FXGLForKtKt.loopBGM;

public class MyGame extends GameApplication {

    private static int CELL_SIZE = 40;
    private static final int GRID_WIDTH = 15;
    private static final int GRID_HEIGHT = 15;
    private List<Entity> snake = new ArrayList<>();
    private int directionX = 1;
    private int directionY = 0;
    private TimerAction timer;
    private TimerAction timerApple;
    private TimerAction timerCactus;
    private Entity apple;
    private Entity cactus;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(CELL_SIZE * GRID_WIDTH);
        gameSettings.setHeight(CELL_SIZE * GRID_HEIGHT);
        gameSettings.setTitle("Snake");
        gameSettings.setVersion("1.0");
        gameSettings.setIntroEnabled(false);
    }

    @Override
    protected void initGame() {
        Texture texture = getAssetLoader().loadTexture("grass.png");
        texture.setFitWidth(600);
        texture.setFitHeight(600);
        GameView backgroundView = new GameView(texture, 0);
        getGameScene().addGameView(backgroundView);
        loopBGM("snake.mp3");

        if (timer != null) {
            timer.expire();
            timer = null;
        }
        Entity head = createSegment(5, 5);
        Entity body = createSegment(4, 5);
        Entity tail = createSegment(3, 5);
        snake.add(head);
        snake.add(body);
        snake.add(tail);

        timer = getGameTimer().runAtInterval(() -> moveSnake(), Duration.seconds(0.3));
        timerApple = getGameTimer().runAtInterval(() -> spawnApple(), Duration.seconds(5));
        timerCactus = getGameTimer().runAtInterval(() -> spawnCactus(), Duration.seconds(5));

    }

    private void spawnCactus() {
        if (cactus != null) {
            cactus.removeFromWorld();
        }
        boolean freeCellFound;
        int cactusX;
        int cactusY;

        do {
            freeCellFound = true;
            cactusX = (int) (Math.random() * GRID_WIDTH);
            cactusY = (int) (Math.random() * GRID_HEIGHT);

            for (Entity s : snake) {
                if ((int) (s.getX() / CELL_SIZE) == cactusX &&
                        (int) (s.getY() / CELL_SIZE) == cactusY
                ) {
                    freeCellFound = false;
                    break;
                }

            }
        } while (!freeCellFound);

        cactus = entityBuilder()
                .at(cactusX*CELL_SIZE, cactusY*CELL_SIZE)
                .with(new CactusComponent())
                .buildAndAttach();
    }

    private void spawnApple() {
        if (apple != null) {
            apple.removeFromWorld();
        }
        boolean freeCellFound;
        int appleX;
        int appleY;

        do {
            freeCellFound = true;
            appleX = (int) (Math.random() * GRID_WIDTH);
            appleY = (int) (Math.random() * GRID_HEIGHT);

            for (Entity segment : snake) {
                if ((int) (segment.getX() / CELL_SIZE) == appleX &&
                        (int) (segment.getY() / CELL_SIZE) == appleY) {
                    freeCellFound = false;
                    break;
                }
            }
        } while (!freeCellFound);

        apple = entityBuilder()
                .at(appleX * CELL_SIZE, appleY * CELL_SIZE)
                .with(new AppleComponent())
                .buildAndAttach();
        System.out.println("Яблоко создано на позиции: "+appleX+", "+appleY);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.RIGHT, () -> {
            if (directionX != -1) {
                directionX = 1;
                directionY = 0;
            }
        });
        onKey(KeyCode.LEFT, () -> {
            if (directionX != 1) {
                directionX = -1;
                directionY = 0;
            }
        });
        onKey(KeyCode.UP, () -> {
            if (directionY != 1) {
                directionX = 0;
                directionY = -1;
            }
        });
        onKey(KeyCode.DOWN, () -> {
            if (directionY != -1) {
                directionX = 0;
                directionY = 1;
            }
        });
    }

    private Entity createSegment(int gridX, int gridY) {
        Entity segment = entityBuilder()
                .at(gridX * CELL_SIZE, gridY * CELL_SIZE)
                .view(new Rectangle(CELL_SIZE - 2, CELL_SIZE -2, Color.YELLOW))
                .buildAndAttach();
        return  segment;
    }

    private void moveSnake() {

        if (snake.isEmpty()) {
            System.out.println("snake пуст!");
            return;
        }

        Entity head = snake.get(0);

        int headX = (int) (head.getX() / CELL_SIZE);
        int headY = (int) (head.getY() / CELL_SIZE);
        int newX = headX + directionX;
        int newY = headY + directionY;

        for (Entity segment: snake) {
            int segmentX = (int) (segment.getX() / CELL_SIZE);
            int segmentY = (int) (segment.getY() / CELL_SIZE);
            if (segmentX == newX && segmentY == newY) {
                gameOver();
                return;
            }
        }

        if (newX < 0 || newX >= GRID_WIDTH || newY < 0 || newY >= GRID_HEIGHT) {
            System.out.println("СТЕНА! Игра окончена");
            gameOver();
            return;
        }

        boolean ateCactus = (cactus != null &&
                (int)(cactus.getX()/CELL_SIZE) == newX &&
                (int)(cactus.getY()/CELL_SIZE) == newY);

        boolean ateApple = (apple != null &&
                (int)(apple.getX() / CELL_SIZE) == newX &&
                (int)(apple.getY() / CELL_SIZE) == newY);

        Entity newHead = createSegment(newX, newY);
        snake.add(0, newHead);

        if (ateCactus) {
            if (snake.size() > 2) {
                Entity tail = snake.remove(snake.size() - 1);
                tail.removeFromWorld();}
            cactus.removeFromWorld();
            cactus = null;
            spawnCactus();}

        if (!ateApple) {
            Entity tail = snake.remove(snake.size() - 1);
            tail.removeFromWorld();
        } else {
            apple.removeFromWorld();
            apple = null;
            spawnApple();
            System.out.println("Яблоко съедено! Размер змейки " + snake.size());
        }
        System.out.println("Движение выполнено, размер змейки: " + snake.size());
    }

    private void gameOver() {
        System.out.println("GAME OVER!");
        getDialogService().showMessageBox("GAME OVER", () -> {
            getGameWorld().getEntities().clear();
            snake.clear();
            directionX = 1;
            directionY = 0;
            getGameController().startNewGame();
        });
    }
}