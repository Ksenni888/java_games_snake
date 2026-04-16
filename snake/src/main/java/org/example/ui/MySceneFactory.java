package org.example.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import org.jetbrains.annotations.NotNull;

public class MySceneFactory extends SceneFactory {

    @Override
    public @NotNull FXGLMenu newGameMenu() {
        System.out.println("newGameMenu() вызван!");
        return new org.example.ui.MyPauseMenu(MenuType.GAME_MENU);
    }
}
