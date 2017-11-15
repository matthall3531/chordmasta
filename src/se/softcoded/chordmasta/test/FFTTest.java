package se.softcoded.chordmasta.test;

import org.junit.Test;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.signalprocessing.Complex;
import se.softcoded.chordmasta.signalprocessing.FFT;
import se.softcoded.chordmasta.signalprocessing.FFTResult;

import static org.junit.Assert.assertEquals;

public class FFTTest {
    /**
     * Run a basic test on a simple signal and compare the output values
     * to a math too's output of the same FFT.
     */
    @Test
    public void basicTest() {
        MonoBlockData dataIn = new MonoBlockData(8);
        dataIn.set(0, 0.0);
        dataIn.set(1, 1.0);
        dataIn.set(2, 2.0);
        dataIn.set(3, 3.0);
        dataIn.set(4, 4.0);
        dataIn.set(5, 5.0);
        dataIn.set(6, 6.0);
        dataIn.set(7, 7.0);
        FFTResult fftResult = new FFTResult(dataIn.size());
        FFT fft = new FFT();
        fft.process(dataIn, fftResult);

        // Check fftResult against result from Octave output
        assertEquals(dataIn.size(), fftResult.size());
        assertEquals(new Complex(28.0, 0.0), fftResult.get(0));
        assertEquals(new Complex(-4.0, 9.6569), fftResult.get(1));
        assertEquals(new Complex(-4.0, 4.0), fftResult.get(2));
        assertEquals(new Complex(-4.0, 1.6569), fftResult.get(3));
        assertEquals(new Complex(-4.0, 0.0), fftResult.get(4));
        assertEquals(new Complex(-4.0, -1.6569), fftResult.get(5));
        assertEquals(new Complex(-4.0, -4.0), fftResult.get(6));
        assertEquals(new Complex(-4.0, -9.6569), fftResult.get(7));
    }

    /**
     * Run a test to see if we get a peak in the frequency spectra where
     * we would expect one. The signal is a 20Hz sine and it is sampled at 50Hz.
     */
    @Test
    public void frequencyTest() {
        final double F = 20.0;
        final double Fs = 50.0;
        final double A = 1.0;
        final int DATA_SIZE = 128;

        MonoBlockData monoBlockData = new MonoBlockData(DATA_SIZE);
        TestTools.generateSine(F, A, Fs, (double)DATA_SIZE/Fs, monoBlockData);
        FFTResult fftResult = new FFTResult(monoBlockData.size());
        FFT fft = new FFT();
        fft.process(monoBlockData, fftResult);
        int index = (int)Math.round(F / Fs * DATA_SIZE);
        assertEquals(index, fftResult.slice(0, DATA_SIZE/2).getSortedIndex()[0]);
    }

    @Test
    public void zeroPadTest() {
        final double F = 2112.0;
        final double Fs = 8198.0;
        final double A = 1.0;
        final int DATA_SIZE = 128;

        MonoBlockData monoBlockData = new MonoBlockData(DATA_SIZE);
        TestTools.generateSine(F, A, Fs, (double)DATA_SIZE/Fs, monoBlockData);

        MonoBlockData monoBlockDataPadded = new MonoBlockData((int)Fs, 0.0);
        monoBlockDataPadded.copy(monoBlockData, 0);

        FFTResult fftResult = new FFTResult((int)Fs);
        FFT fft = new FFT();
        fft.process(monoBlockDataPadded, fftResult);

        int[] sortedIndex = fftResult.slice(0, (int)Fs/2).getSortedIndex();
        assertEquals((int)F, sortedIndex[0]);
    }

    // Disabled until Adaptive Volume Gain is introduced
   /* @Test
    public void frequencyTest2() {
        final double F = 5;
        final double Fs = 50.0;
        final double A = 1.0;
        final int DATA_SIZE = 128;

        MonoBlockData monoBlockData = new MonoBlockData(DATA_SIZE);
        TestTools.generateAmplitudeVaryingSine(F, A-0.5, A+0.5, 1.0, Fs, (double)DATA_SIZE/Fs, monoBlockData);
        FFTResult fftResult = new FFTResult(monoBlockData.size());
        FFT fft = new FFT();
        fft.process(monoBlockData, fftResult);
        int index = (int)Math.round(F / Fs * DATA_SIZE);
        assertEquals(index, fftResult.slice(0, DATA_SIZE/2).getSortedIndex()[0]);
    }*/
}
