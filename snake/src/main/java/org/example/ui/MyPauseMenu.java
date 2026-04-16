package org.example.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.audio.Music;
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

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;


public class MyPauseMenu extends FXGLMenu {
    private Music backgroundMusic;

    public MyPauseMenu(MenuType type) {
        super(type);
        getContentRoot().getChildren().clear();

        if (backgroundMusic == null) {
            backgroundMusic = getAssetLoader().loadMusic("snake.mp3");
            getAudioPlayer().loopMusic(backgroundMusic);
            backgroundMusic.getAudio().setVolume(1);
        }


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

        Slider volumeSlider = new Slider(0,5,0.05);
        volumeSlider.setPrefWidth(150);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(1);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double volume = newVal.doubleValue();
            backgroundMusic.getAudio().setVolume(volume);
        });
        volumeBox.getChildren().addAll(volumeLabel, volumeSlider);
        menuBox.getChildren().addAll(title, resumeBtn, volumeBox, exit);
        rootPane.getChildren().addAll(bg, menuBox);
        getContentRoot().getChildren().add(rootPane);
    }
}