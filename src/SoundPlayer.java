import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    public static void sound(String fileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile;
        audioFile = new File(fileName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
}
