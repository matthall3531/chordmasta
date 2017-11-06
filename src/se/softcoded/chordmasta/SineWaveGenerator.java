package se.softcoded.chordmasta;

/**
 * Generates a sine wave of a specified frequency and amplitude.
 * The signal produced is a stereo signal where the samples are interleaved.
 */
public class SineWaveGenerator {
    private final int sampleRate;
    private final double frequency;
    private final double amplitude;
    private final double samplesPerPeriod;

    private int currentSample = 0;

    public SineWaveGenerator(int sampleRate, double frequency, double amplitude) {
        this.sampleRate = sampleRate;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.samplesPerPeriod = sampleRate / frequency;
    }

    /** Sample-based mode uses the following formula to compute the output of the Sine Wave block.

            y=Asin(2π(k+o)/p)+b
            where
     sin(2*pi*f/fs*t)
    A is the amplitude of the sine wave.
    p is the number of time samples per sine wave period.
    k is a repeating integer value that ranges from 0 to p–1.
    o is the offset (phase shift) of the signal.
    b is the signal bias.*/
    public void generateBlock(int nrOfSamplesInBlock, StereoBlockData blockData) {
        for (int sample = 0; sample<nrOfSamplesInBlock; sample++) {
            double y = amplitude * Math.sin(2 * Math.PI * frequency/(double)sampleRate * currentSample);
            currentSample++;
            if (currentSample >= sampleRate) {
                currentSample = 0;
            }
            blockData.appendSample(y, y);
        }
    }
}
