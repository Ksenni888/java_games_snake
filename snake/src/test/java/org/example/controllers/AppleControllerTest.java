package org.example.controllers;

import com.almasb.fxgl.entity.Entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AppleControllerTest {

    @InjectMocks
    private AppleController ap;

    Entity createMockApple() {
        Entity apple = mock(Entity.class);
        lenient().when(apple.getX()).thenReturn(120.0);
        lenient().when(apple.getY()).thenReturn(160.0);
        return apple;
    }

    void setMockAppleInController(Entity apple) throws NoSuchFieldException, IllegalAccessException {
        Field field = AppleController.class.getDeclaredField("apple");
        field.setAccessible(true);
        field.set(ap, apple);
    }

    @Test
    public void ateApple() throws NoSuchFieldException, IllegalAccessException {
        Entity apple = createMockApple();
        setMockAppleInController(apple);
        boolean result = ap.ateApple(3, 4);
        assertTrue(result);
        verify(apple, times(1)).removeFromWorld();
    }

    @Test
    public void ateApple_WrongPosition_returnFalse() throws NoSuchFieldException, IllegalAccessException {
        Entity apple = createMockApple();
        setMockAppleInController(apple);
        boolean result = ap.ateApple(5, 8);
        assertFalse(result);
        verify(apple, never()).removeFromWorld();
    }

    @Test
    public void ateApple_NoApple(){
        boolean result = ap.ateApple(3,4);
        assertFalse(result);
    }
}