package se.softcoded.chordmasta;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
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
        for (int n = 0; n < fftResult.size(); n++) {
            System.out.println(n + " re=" + fftResult.get(n).re() + ", im=" + fftResult.get(n).im());
        }
    }
}
