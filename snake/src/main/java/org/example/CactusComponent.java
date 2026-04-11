package org.example;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class CactusComponent extends Component{
    private AnimatedTexture texture;
    private AnimationChannel animIdle;

    public CactusComponent() {
        animIdle = new AnimationChannel(
                image("cactus.png"),
                2,
                32,
                32,
                Duration.seconds(1),
                0,
                1);
        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }
    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
