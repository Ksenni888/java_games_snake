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
public class CactusControllerTest {

    @InjectMocks
    private CactusController cc;

    Entity createMockCactus() {
        Entity cactus = mock(Entity.class);
        lenient().when(cactus.getX()).thenReturn(120.0);
        lenient().when(cactus.getY()).thenReturn(160.0);
        return cactus;
    }

    void setMockCactusInController(Entity cactus) throws NoSuchFieldException, IllegalAccessException {
        Field field = CactusController.class.getDeclaredField("cactus");
        field.setAccessible(true);
        field.set(cc, cactus);
    }

    @Test
    void ateCactus() throws NoSuchFieldException, IllegalAccessException {
        Entity cactus = createMockCactus();
        setMockCactusInController(cactus);
        boolean result = cc.ateCactus(3,4);
        assertTrue(result);
        verify(cactus, times(1)).removeFromWorld();
    }

    @Test
    void ateCactus_WrongPosition_returnFalse() throws NoSuchFieldException, IllegalAccessException {
        Entity cactus1 = createMockCactus();
        setMockCactusInController(cactus1);
        boolean result = cc.ateCactus(5,6);
        assertFalse(result);
        verify(cactus1, never()).removeFromWorld();

    }

    @Test
    void ateCactus_NoCactus(){
        boolean result = cc.ateCactus(3,4);
        assertFalse(result);
    }
}
