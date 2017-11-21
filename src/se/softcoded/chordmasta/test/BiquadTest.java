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
     *
     * Test that the 5000Hz signal is there before and that it is gone after.
     */
    @Test
    public void testLowPassBiquad() {
        final int BLOCK_SIZE = (int)(0.2 * 11000);
        MonoBlockData signal = new MonoBlockData(BLOCK_SIZE);
        TestTools.generateMultipleSines(
                new double[] { 200.0, 5000.0 },
                new double[] { 1.0, 2.0 },
                11000.0,
                0.2,
                signal);
        FFT fft = new FFT();
        FFTResult before = new FFTResult(BLOCK_SIZE);
        fft.process(signal, before);

        MonoBlockData filteredSignal = new MonoBlockData(BLOCK_SIZE);
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

        int[] sortedIndexBefore = before.slice(0, BLOCK_SIZE/2).getSortedIndex();
        int[] sortedIndexAfter = after.slice(0, BLOCK_SIZE/2).getSortedIndex();

        org.junit.Assert.assertEquals((int)(5000.0/11000.0 * BLOCK_SIZE) - 1, sortedIndexBefore[0]);
        org.junit.Assert.assertEquals((int)(200.0/11000.0 * BLOCK_SIZE) - 1, sortedIndexBefore[1]);

        org.junit.Assert.assertEquals((int)(200.0/11000.0 * BLOCK_SIZE) - 1, sortedIndexAfter[0]);
    }
}
