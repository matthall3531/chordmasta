package se.softcoded.chordmasta.ext.wav;

import se.softcoded.chordmasta.StereoBlockData;

import java.io.File;

public class WavFileGenerator {
    private WavFile wavFile;

    public WavFileGenerator(String filename) {
        try {
            wavFile = WavFile.openWavFile(new File(filename));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public boolean generateBlock(int blockSize, StereoBlockData block) {
        return false;
    }
}
