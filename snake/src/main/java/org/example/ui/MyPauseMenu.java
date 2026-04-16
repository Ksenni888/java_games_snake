package org.example.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.controllers.SoundController;


public class MyPauseMenu extends FXGLMenu {
    private SoundController soundController = SoundController.getInstance();

    public MyPauseMenu(MenuType type) {
        super(type);
        getContentRoot().getChildren().clear();
        StackPane rootPane = new StackPane();

        Rectangle bg = new Rectangle(300,300);
        bg.setFill(Color.rgb(255, 0,0,0.85));
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(100);

        Text title = new Text("ПАУЗА");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Arial", 40));

        Button resumeBtn = new Button("Продолжить");
        resumeBtn.setStyle("-fx-font-size: 18; -fx-min-width: 200;");
        resumeBtn.setOnAction(e -> fireResume());

        Button exit = new Button("Выйти");
        exit.setStyle("-fx-font-size: 18; -fx-min-width: 200;");
        exit.setOnAction(e -> fireExit());

        HBox volumeBox = new HBox(10);
        volumeBox.setAlignment(Pos.CENTER);
        Text volumeLabel = new Text("Громкость");
        volumeLabel.setFill(Color.WHITE);
        volumeLabel.setFont(Font.font("Arial", 14));
        Slider volumeMusicSlider = new Slider(0, 1, soundController.getVolume());
        volumeMusicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            soundController.setVolume(newVal.doubleValue());
        });
        volumeBox.getChildren().addAll(volumeLabel, volumeMusicSlider);

        HBox volumeEatBox = new HBox(10);
        volumeEatBox.setAlignment(Pos.CENTER);
        Text volumeEatLabel = new Text("Громкость поедания");
        volumeEatLabel.setFill(Color.WHITE);
        volumeEatLabel.setFont(Font.font("Arial", 14));
        Slider volumeEatSlider = new Slider(0,1,soundController.getVolumeEat());
        volumeEatSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            soundController.setVolumeEat(newVal.doubleValue());
        });
        volumeEatBox.getChildren().addAll(volumeEatLabel, volumeEatSlider);

        menuBox.getChildren().addAll(title, resumeBtn, volumeBox, volumeEatBox, exit);
        rootPane.getChildren().addAll(bg, menuBox);
        getContentRoot().getChildren().add(rootPane);
    }
}