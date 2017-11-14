package se.softcoded.chordmasta.test;

import org.junit.Test;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.signalprocessing.Biquad;
import se.softcoded.chordmasta.signalprocessing.FFT;
import se.softcoded.chordmasta.signalprocessing.FFTResult;

public class BiquadTest {
    /**
     * Test low pass biquad by generating a signal with 2 sines, one at 200Hz and one
     * at 5000Hz. They should be equal in amplitude. Use the biquad to filter out the 5000Hz signal.
     */
    @Test
    public void testLowPassBiquad() {
        final int BLOCK_SIZE = (int)(0.2 * 11000);
        MonoBlockData<Double> signal = new MonoBlockData<>(BLOCK_SIZE);
        TestTools.generateMultipleSines(
                new double[] { 200.0, 5000.0 },
                new double[] { 1.0, 2.0 },
                11000.0,
                0.2,
                signal);
        FFT fft = new FFT();
        FFTResult before = new FFTResult(BLOCK_SIZE);
        fft.process(signal, before);

        MonoBlockData<Double> filteredSignal = new MonoBlockData<Double>(BLOCK_SIZE);
        try {
            Biquad biquad = new Biquad(11000, 500.0, 0.75, Biquad.BiquadFilterType.LOW_PASS);
            biquad.process(signal, filteredSignal);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        FFTResult after = new FFTResult(BLOCK_SIZE);
        fft.process(filteredSignal, after);

        MonoBlockData magBefore = new MonoBlockData(BLOCK_SIZE);
        MonoBlockData magAfter = new MonoBlockData(BLOCK_SIZE);
        before.calculateMagnitude(magBefore);
        after.calculateMagnitude(magAfter);

        for (int i=0; i<after.size(); i++) {
            System.out.println(i + " " + magBefore.get(i) + " ---- " + magAfter.get(i));
        }
    }
}
