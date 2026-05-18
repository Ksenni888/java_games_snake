package org.example.controllers;

import org.example.model.Snake;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyGameTest {
    @Mock
    private Snake snake;
    @InjectMocks
    private MyGame mg;

    @Test
    public void checkEmptySnake_NoSnake() {
        when(snake.getSnake()).thenReturn(new ArrayList<>());
        boolean result = mg.checkEmptySnake(snake.getSnake());
        assertTrue(result);
    }
}
