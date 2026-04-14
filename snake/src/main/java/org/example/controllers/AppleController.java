package org.example.controllers;

import com.almasb.fxgl.entity.Entity;
import org.example.Snake;
import org.example.model.AppleComponent;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.example.Constants.CELL_SIZE;
import static org.example.Constants.GRID_HEIGHT;
import static org.example.Constants.GRID_WIDTH;

public class AppleController {
    private Snake snake;
    private Entity apple;

    public AppleController(Snake snake) {
        this.snake = snake;
    }

    public void spawnApple() {
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

            for (Entity segment : snake.getSnake()) {
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
        System.out.println("Яблоко создано на позиции: " + appleX + ", " + appleY);
    }

    public boolean ateApple(int newX, int newY) {
        boolean res = false;
        if (apple != null &&
                (int) (apple.getX() / CELL_SIZE) == newX &&
                (int) (apple.getY() / CELL_SIZE) == newY) {
            res = true;
            apple.removeFromWorld();
            apple = null;
        }
        return res;
    }
}