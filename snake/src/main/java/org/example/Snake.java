package org.example;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static org.example.Constants.CELL_SIZE;

public class Snake {
    private final List<Entity> snake = new ArrayList<>();
    private int directionX = 1;
    private int directionY = 0;

    public List<Entity> getSnake() {
        return snake;
    }
    public int getDirectionX() {
        return directionX;
    }
    public int getDirectionY() {
        return directionY;
    }

    public void setDirectionX(int directionX) {
        this.directionX = directionX;
    }
    public void setDirectionY(int directionY) {
        this.directionY = directionY;
    }

    public void setDirection(int x, int y) {
        if ((directionX == -1 && x == 1) || (directionX == 1 && x == -1)) return;
        if ((directionY == -1 && y == 1) || (directionY == 1 && y == -1)) return;
        directionX = x;
        directionY = y;
    }

    public void addSnake() {
        Entity head = createSegment(5, 5);
        Entity body = createSegment(4, 5);
        Entity tail = createSegment(3, 5);
        snake.add(head);
        snake.add(body);
        snake.add(tail);
    }

    public Entity createSegment(int gridX, int gridY) {
        return entityBuilder()
                .at(gridX * CELL_SIZE, gridY * CELL_SIZE)
                .view(new Rectangle(CELL_SIZE - 2, CELL_SIZE -2, Color.YELLOW))
                .buildAndAttach();
    }
    public void removeTail() {
        if (snake.size() > 1) {
            Entity tail = snake.remove(snake.size() - 1);
            tail.removeFromWorld();}}

}