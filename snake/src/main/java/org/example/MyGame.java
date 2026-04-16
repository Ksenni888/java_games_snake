package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.example.controllers.AppleController;
import org.example.controllers.CactusController;
import org.example.ui.MySceneFactory;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;
import static org.example.Constants.CELL_SIZE;
import static org.example.Constants.GRID_HEIGHT;
import static org.example.Constants.GRID_WIDTH;

public class MyGame extends GameApplication {
    private final Snake snake = new Snake();
    private final AppleController appleController = new AppleController(snake);
    private final CactusController cactusController = new CactusController(snake);

    private TimerAction timer;
    private TimerAction timerApple;
    private TimerAction timerCactus;

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
        Music music = getAssetLoader().loadMusic("snake.mp3");
        getAudioPlayer().playMusic(music);
        music.getAudio().setVolume(0.05);
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
        onKey(KeyCode.RIGHT, () -> {if (snake != null) snake.setDirection(1,0);
        });
        onKey(KeyCode.LEFT, () -> {if (snake != null) snake.setDirection(-1,0);
        });
        onKey(KeyCode.UP, () -> {if (snake != null) snake.setDirection(0,-1);

        });
        onKey(KeyCode.DOWN, () -> {if (snake != null) snake.setDirection(0,1);
        });
    }

    private void moveSnake() {

        if (snake.getSnake().isEmpty()) {
            System.out.println("snake пуст!");
            return;
        }

        Entity head = snake.getSnake().get(0);

        int headX = (int) (head.getX() / CELL_SIZE);
        int headY = (int) (head.getY() / CELL_SIZE);
        int newX = headX + snake.getDirectionX();
        int newY = headY + snake.getDirectionY();

        for (Entity segment: snake.getSnake()) {
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

        Entity newHead = snake.createSegment(newX, newY);
        snake.getSnake().add(0, newHead);

        if (cactusController.ateCactus(newX, newY)) {
            snake.removeTail(); }

        if (!appleController.ateApple(newX, newY)) {
            snake.removeTail(); }
    }

    private void gameOver() {
        System.out.println("GAME OVER!");
        getDialogService().showMessageBox("GAME OVER", () -> {
            getGameWorld().getEntities().clear();
            snake.getSnake().clear();
            snake.setDirectionX(1);
            snake.setDirectionY(0);
            getGameController().startNewGame();
        });
    }
}