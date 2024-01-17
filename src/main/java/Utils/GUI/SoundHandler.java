package Utils.GUI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundHandler {
    public static final String PICK_SOUND_PATH = "src/main/resources/GUI/Sounds/pick.wav";
    public static final String BACKGROUND_SOUND_PATH = "src/main/resources/GUI/Sounds/background_music.wav";
    public static final String ROLLING_SOUND_PATH = "src/main/resources/GUI/Sounds/rolling.wav";
    public static final float BACKGROUND_MUSIC_VOLUME = 0.1F;
    public static final float ACTIONS_MUSIC_VOLUME = 1F;
    public static void playLoopingSound(String soundFilePath, float volume) {
        try {
            Clip clip = getClip(soundFilePath);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(convertToDecibels(volume));

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playSound(String soundFilePath, float volume) {
        try {
            Clip clip = getClip(soundFilePath);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(convertToDecibels(volume));

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static Clip getClip(String soundFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(soundFilePath);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    private static float convertToDecibels(float volume) {
        return (float) (Math.log10(volume) * 20);
    }

}
