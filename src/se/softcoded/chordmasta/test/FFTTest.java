package se.softcoded.chordmasta.test;

import se.softcoded.chordmasta.wavfile.WavFile;

import java.io.File;

public class FFTTest {
    public static void main(String[] args) {
        try {
            WavFile file = WavFile.openWavFile(new File("../../../../../research/testfiles/Grand Piano - Fazioli - minor chords - A#m highest.wav"));
            int numChannels = file.getNumChannels();
            double[] sampleBuffer = new double[1024 * 2 * 20];
            int ret = file.readFrames(sampleBuffer, 1024 * 20);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
