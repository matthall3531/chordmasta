package se.softcoded.chordmasta.signalprocessing;

import org.junit.Test;
import se.softcoded.chordmasta.*;
import se.softcoded.chordmasta.test.TestTools;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FFT implements ProcessUnit {
    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData inData = (MonoBlockData)in;
        FFTResult result = (FFTResult)out;
        List<Complex> data = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            data.add(new Complex((double)inData.get(i), 0.0));
        }
        fftRadix2(data);
        for (int n=0; n<result.size(); n++) {
            result.set(n, data.get(n));
        }
    }

    private void fftRadix2(List<Complex> inData) {
        int N = inData.size();
        if (N >= 2) {
            separate(inData);
            fftRadix2(inData.subList(0, N/2));
            fftRadix2(inData.subList(N/2, inData.size()));

            for (int k = 0; k < N/2; k++) {
                Complex even = inData.get(k);
                Complex odd = inData.get(k + N/2);

                Complex w = new Complex(0, -2.0 * Math.PI * k/N).exp();
                inData.set(k, even.plus(w.times(odd)));
                inData.set(k + N/2, even.minus(w.times(odd)));
            }
        }
    }

    private void separate(List<Complex> a) {
        int N = a.size();
        ArrayList<Complex> b = new ArrayList<>();
        for (int i = 0; i < N/2; i++) {
            b.add(a.get(i*2+1));
        }
        for (int i = 0; i < N/2; i++) {
            a.set(i, a.get(i*2));
        }
        for (int i = 0; i < N/2; i++) {
            a.set(i + N/2, b.get(i));
        }
    }

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
}
