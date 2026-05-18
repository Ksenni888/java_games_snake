package org.example.controllers;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.example.model.Snake;
import org.example.ui.MySceneFactory;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static org.example.model.Constants.CELL_SIZE;
import static org.example.model.Constants.GRID_HEIGHT;
import static org.example.model.Constants.GRID_WIDTH;

public class MyGame extends GameApplication {
    private final Snake snake = new Snake();
    private final AppleController appleController = new AppleController(snake);
    private final CactusController cactusController = new CactusController(snake);

    private TimerAction timer;
    private TimerAction timerApple;
    private TimerAction timerCactus;

    SoundController soundController = SoundController.getInstance();

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(CELL_SIZE * GRID_WIDTH);
        gameSettings.setHeight(CELL_SIZE * GRID_HEIGHT);
        gameSettings.setTitle("Snake");
        gameSettings.setVersion("1.0");
        gameSettings.setIntroEnabled(false);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setSceneFactory(new MySceneFactory());
    }

    @Override
    protected void initGame() {
        Texture texture = getAssetLoader().loadTexture("grass.png");
        texture.setFitWidth(600);
        texture.setFitHeight(600);
        GameView backgroundView = new GameView(texture, 0);
        getGameScene().addGameView(backgroundView);
        soundController.playBackgroundMusic();
        soundController.loadSound();
        snake.addSnake();
        if (timer != null) {
            timer.expire();
            timer = null;
        }
        timer = getGameTimer().runAtInterval(() -> moveSnake(), Duration.seconds(0.3));
        timerApple = getGameTimer().runAtInterval(() -> appleController.spawnApple(), Duration.seconds(5));
        timerCactus = getGameTimer().runAtInterval(() -> cactusController.spawnCactus(), Duration.seconds(5));
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.RIGHT, () -> snake.setDirection(1,0));
        onKey(KeyCode.LEFT, () -> snake.setDirection(-1, 0));
        onKey(KeyCode.UP, () -> snake.setDirection(0, -1));
        onKey(KeyCode.DOWN, () -> snake.setDirection(0, 1));
    }

   boolean checkEmptySnake(List<Entity> segmentsSnake){
        return segmentsSnake.isEmpty();
    }

   boolean touchWall(int x, int y){
        return x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT;
    }

   boolean checkSelfCollision(List<Entity> segmentsSnake, int newX, int newY){
        int lengthSnake = segmentsSnake.size();
        for (int i = 0; i < lengthSnake-1; i++) {
            Entity segment = segmentsSnake.get(i);
            int segmentX = (int) (segment.getX() / CELL_SIZE);
            int segmentY = (int) (segment.getY() / CELL_SIZE);
            if (segmentX == newX && segmentY == newY) {
                System.out.println("СТОЛКНОВЕНИЕ С СОБОЙ! Сегмент " + i);
                return true;
            }
        }
        return false;
    }

    private void moveSnake() {
        List<Entity> segmentsSnake = snake.getSnake();
        if (checkEmptySnake(segmentsSnake)) return;

        Entity head = segmentsSnake.get(0);
        int headX = (int) (head.getX() / CELL_SIZE);
        int headY = (int) (head.getY() / CELL_SIZE);
        int newX = headX + snake.getDirectionX();
        int newY = headY + snake.getDirectionY();

        if (touchWall(newX, newY)) {gameOver();}

        Entity newHead = snake.createSegment(newX, newY);
        if (checkSelfCollision(segmentsSnake, newX, newY)){gameOver();}
        segmentsSnake.add(0, newHead);
        CactusCollision(newX, newY);
        AppleCollision(newX, newY);
    }

    private void CactusCollision(int newX, int newY){
        if (cactusController.ateCactus(newX, newY)){
            soundController.playEatCactus();
            snake.removeTail();
        }
    }

    private void AppleCollision(int newX, int newY){
        if (!appleController.ateApple(newX, newY)) {
            snake.removeTail(); }
        else {
            soundController.playEatApple();
        }
    }

    private void gameOver() {
        timer = stopAllTimers(timer);
        timerApple = stopAllTimers(timerApple);
        timerCactus = stopAllTimers(timerCactus);
        getDialogService().showMessageBox("GAME OVER", () -> {
            getGameWorld().getEntities().clear();
            snake.getSnake().clear();
            snake.setDirectionX(1);
            snake.setDirectionY(0);
            getGameController().startNewGame();
        });
    }

    private TimerAction stopAllTimers(TimerAction timer) {
        if (timer != null){
            timer.expire();
        }
        return null;
    }
}