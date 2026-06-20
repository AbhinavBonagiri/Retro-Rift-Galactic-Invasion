package logic;

import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.*;

public class SoundManager {
    private Clip bgMusic;
    private HashMap<String, String> soundEffects = new HashMap<>();

    public void playBackgroundMusic(String path) {
        try {
            URL soundURL = getClass().getResource(path);
            if (soundURL == null) {
                throw new Exception("Background music resource not found: " + path);
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            bgMusic = AudioSystem.getClip();
            bgMusic.open(audioIn);
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
            bgMusic.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (bgMusic != null && bgMusic.isRunning()) {
            bgMusic.stop();
            bgMusic.close();
        }
    }

    public void loadSoundEffect(String name, String path) {
        URL soundURL = getClass().getResource(path);
        if (soundURL == null) {
            System.out.println("Sound effect resource not found: " + path);
            return;
        }
        // Stores name and path of sound effect for later use
        soundEffects.put(name, path);
    }

    public void playSoundEffect(String name) {
        String path = soundEffects.get(name);
        if (path != null) {
            try {
                // Loads and plays sound
                URL soundURL = getClass().getResource(path);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}