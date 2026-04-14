package org.example.controllers;

import com.almasb.fxgl.entity.Entity;
import org.example.Snake;
import org.example.model.CactusComponent;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.example.Constants.CELL_SIZE;
import static org.example.Constants.GRID_HEIGHT;
import static org.example.Constants.GRID_WIDTH;

public class CactusController {
    private Snake snake;
    private Entity cactus;

    public CactusController(Snake snake){
        this.snake = snake;
    }

    public void spawnCactus() {
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

            for (Entity s : snake.getSnake()) {
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

    public boolean ateCactus(int newX, int newY) {
        boolean res = false;
        if (cactus != null &&
                (int)(cactus.getX() / CELL_SIZE) == newX &&
                (int)(cactus.getY() / CELL_SIZE) == newY) {
            res = true;
            cactus.removeFromWorld();
            cactus = null;}
        return res;
    }
}