package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.StereoBlockData;

import java.util.Random;

public class WhiteNoise implements SignalGenerator {
    private final double variance;
    private final double mean;
    Random r = new Random();

    public WhiteNoise(double variance, double mean) {
        this.variance = variance;
        this.mean = mean;
    }

    @Override
    public boolean generateBlock(int nrOfSamplesInBlock, BlockData dataIn) {
        StereoBlockData blockData = (StereoBlockData)dataIn;
        for (int n=0; n<nrOfSamplesInBlock; n++) {
            double noise = r.nextGaussian() * Math.sqrt(variance) + mean;
            blockData.appendSample(noise, noise);
        }
        return true;
    }
}
