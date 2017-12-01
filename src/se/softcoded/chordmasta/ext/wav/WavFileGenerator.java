package se.softcoded.chordmasta.ext.wav;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.StereoBlockData;
import se.softcoded.chordmasta.signalprocessing.SignalGenerator;

import java.io.File;

public class WavFileGenerator implements SignalGenerator {
    private WavFile wavFile;

    public WavFileGenerator(String filename) {
        try {
            wavFile = WavFile.openWavFile(new File(filename));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public boolean generateBlock(int blockSize, BlockData block) {
        StereoBlockData dataBlock = (StereoBlockData)block;
        int nrChannels = wavFile.getNumChannels();
        int dataSize = blockSize * nrChannels;
        int nrFrames = 0;

        try {
            double[] samples = new double[dataSize];
            nrFrames = wavFile.readFrames(samples, blockSize);
            for (int idx=0; idx<nrFrames; idx++) {
                if (nrChannels == 2) {
                    dataBlock.appendSample(samples[2 * idx], samples[2 * idx + 1]);
                }
                else {
                    dataBlock.appendSample(samples[idx], samples[idx]);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return nrFrames > 0;
    }
}
