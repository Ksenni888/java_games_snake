package org.example.controllers;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;

import java.util.prefs.Preferences;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getSettings;

public class SoundController {
    private static SoundController instance;
    private double volume;
    private boolean soundEnabled;
    private final Preferences prefs;
    private boolean soundsLoaded = false;

    private Sound eatAppleSound;
    private Sound eatCactusSound;
    public static final String FILENAME_EAT_APPLE = "nam.wav";
    public static final String FILENAME_EAT_CACTUS = "puk.wav";
    public static final String FILENAME_BACKGROUND = "snake.wav";
    private Music backgroundMusic;
    private double volumeEat;

    private SoundController(){
        prefs = Preferences.userNodeForPackage(SoundController.class);
        loadSettings();

    }

    public static SoundController getInstance(){
        if (instance == null){
            instance = new SoundController();
        }
        return instance;
    }

    public void loadSound() {
        if (soundsLoaded) return;
        try{
            eatAppleSound = getAssetLoader().loadSound(FILENAME_EAT_APPLE);
            eatCactusSound = getAssetLoader().loadSound(FILENAME_EAT_CACTUS);
            soundsLoaded = true;
        } catch (Exception e) {
            System.out.println("Не удалось загрузить звуки" + e.getMessage());
        }
    }

    private void loadSettings() {
        volume = prefs.getDouble("volume", 0.5);
        volumeEat = prefs.getDouble("volumeEat", 0.05);
        soundEnabled = prefs.getBoolean("soundEnabled", true);
    }

    private void saveSettings() {
        prefs.putDouble("volume", volume);
        prefs.putDouble("volumeEat", volumeEat);
        prefs.putBoolean("soundEnabled", soundEnabled);
    }

    public void setVolumeEat(double volumeEat) {
        this.volumeEat = Math.max(0.0, Math.min(1.0, volumeEat));
        System.out.println("volumeEat изменен на: " + this.volumeEat);

        saveSettings();
    }

    public double getVolumeEat() {
        return volumeEat;
    }

    public void playEatApple() {
        if (!soundEnabled || eatAppleSound == null) return;
        double currentVolume = getSettings().getGlobalSoundVolume();
        getSettings().setGlobalSoundVolume(volumeEat);
        getAudioPlayer().playSound(eatAppleSound);
        getSettings().setGlobalSoundVolume(currentVolume);
    }

    public void playEatCactus() {
        if (!soundEnabled || eatCactusSound == null) return;
        double currentVolume = getSettings().getGlobalSoundVolume();
        getSettings().setGlobalSoundVolume(volumeEat);
        getAudioPlayer().playSound(eatCactusSound);
        getSettings().setGlobalSoundVolume(currentVolume);
    }

    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusic != null) {
            backgroundMusic.getAudio().setVolume(this.volume);
        }
        saveSettings();
    }

    public double getVolume() {
        return volume;
    }
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
        saveSettings();
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void toggleSound() {
        setSoundEnabled(!soundEnabled);
    }
    public void playBackgroundMusic() {
        if (!soundEnabled) return;
        if (backgroundMusic != null) {
            return;
        }
        backgroundMusic = getAssetLoader().loadMusic(FILENAME_BACKGROUND);
        getAudioPlayer().playMusic(backgroundMusic);
        backgroundMusic.getAudio().setVolume(volume);
        backgroundMusic.getAudio().setLooping(true);
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            getAudioPlayer().stopMusic(backgroundMusic);
            backgroundMusic = null;
        }
    }
}