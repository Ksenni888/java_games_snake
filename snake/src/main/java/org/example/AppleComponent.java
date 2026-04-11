package org.example;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class AppleComponent extends Component{
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public AppleComponent() {
        animIdle = new AnimationChannel(
                image("apple.png"),
                4,
                32,
                32,
                Duration.seconds(1),
                0,
                3);
        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }
    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
