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
}
