package se.softcoded.chordmasta.test;

import se.softcoded.chordmasta.MonoBlockData;

public class TestTools {
    public static void generateSine(double F, double A, double Fs, double t, MonoBlockData monoBlockData) {
        int nrOfSamplesInBlock = (int)(Fs * t);
        for (int sample = 0; sample<nrOfSamplesInBlock; sample++) {
            double y = A * Math.sin(2 * Math.PI * F/Fs * sample);
            monoBlockData.set(sample, y);
        }
    }

    public static void generateAmplitudeVaryingSine(double F, double minA, double maxA, double At, double Fs, double t, MonoBlockData monoBlockData) {
        int nrOfSamplesInBlock = (int)(Fs * t);
        int nrOfASamplesPerPeriod = (int)((At/t) * nrOfSamplesInBlock);
        double changeAPerTime = (maxA - minA)/At;
        for (int sample = 0; sample<nrOfSamplesInBlock; sample++) {
            double y = (minA + (changeAPerTime * sample%nrOfASamplesPerPeriod)) * Math.sin(2 * Math.PI * F/Fs * sample);
            monoBlockData.set(sample, y);
            System.out.println(y);
        }
    }

    public static void generateMultipleSines(double[] frequencies, double[] amplitudes, double Fs, double t, MonoBlockData monoBlockData) {
        if (frequencies.length != amplitudes.length) {
            throw new IllegalArgumentException("frequencies and amplitudes are not of same length!");
        }
        MonoBlockData internalData = new MonoBlockData(monoBlockData.size());
        for (int n=0; n<frequencies.length; n++) {
            if (n > 0) {
                generateSine(frequencies[n], amplitudes[n], Fs, t, internalData);
                for (int i=0; i<internalData.size(); i++) {
                    monoBlockData.set(i, (internalData.get(i) + monoBlockData.get(i)) / 2.0);
                }
            }
            else {
                generateSine(frequencies[n], amplitudes[n], Fs, t, monoBlockData);
            }
        }
    }
}
