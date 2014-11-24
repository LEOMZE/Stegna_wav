package Util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


public class AudioWaveformCreator {
    AudioInputStream audioInputStream;
    Vector<Line2D.Double> lines = new Vector<Line2D.Double>();
    ArrayList<Double> yAxsis = new ArrayList<Double>();

    /**
     * Render a WaveForm.
     */


    public ArrayList<Double> createWaveForm(String filePath) {

//            lines.removeAllElements();  // clear the old vector
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            byte[] audioBytes = null;
            AudioFormat format = audioInputStream.getFormat();
            try {
                audioBytes = new byte[
                        (int) (audioInputStream.getFrameLength()
                                * format.getFrameSize())];
                audioInputStream.read(audioBytes);
            } catch (Exception ex) {
                ex.printStackTrace();

            }

            int w = 500;
            int h = 200;
            int[] audioData = null;
            if (format.getSampleSizeInBits() == 16) {
                int nlengthInSamples = audioBytes.length / 2;
                audioData = new int[nlengthInSamples];
                if (format.isBigEndian()) {
                    for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is MSB (high order) */
                        int MSB = (int) audioBytes[2*i];
                         /* Second byte is LSB (low order) */
                        int LSB = (int) audioBytes[2*i+1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                } else {
                    for (int i = 0; i < nlengthInSamples; i++) {
                         /* First byte is LSB (low order) */
                        int LSB = (int) audioBytes[2*i];
                         /* Second byte is MSB (high order) */
                        int MSB = (int) audioBytes[2*i+1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                }
            } else if (format.getSampleSizeInBits() == 8) {
                int nlengthInSamples = audioBytes.length;
                audioData = new int[nlengthInSamples];
                if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i];
                    }
                } else {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i] - 128;
                    }
                }
            }

            int frames_per_pixel = audioBytes.length / format.getFrameSize()/w;
            byte my_byte;
//            double y_last = 0;
            int numChannels = format.getChannels();
            for (double x = 0; x < w && audioData != null; x++) {
                int idx = (int) (frames_per_pixel * numChannels * x);
                if (format.getSampleSizeInBits() == 8) {
                    my_byte = (byte) audioData[idx];
                } else {
                    my_byte = (byte) (128 * audioData[idx] / 32768 );
                }
                double y_new = (double) (h * (128 - my_byte) / 256);
//                lines.add(new Line2D.Double(x, y_last, x, y_new));
                yAxsis.add(y_new);

//                y_last = y_new;
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return yAxsis;

    }
}

