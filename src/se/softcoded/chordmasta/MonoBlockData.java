package se.softcoded.chordmasta;

import java.util.Vector;

public class MonoBlockData extends BlockData {
    private double[] samples;

    public MonoBlockData(int blockSize) {
        samples = new double[blockSize];
    }

    public MonoBlockData(int blockSize, double initValue) {
        this(blockSize);
        for (int i=0; i<blockSize; i++) {
            samples[i] = initValue;
        }
    }

    public void set(int index, double data) {
        samples[index] = data;
    }

    public double get(int index) {
        return samples[index];
    }

    @Override
    public int size() {
        return samples.length;
    }

    public void copy(double[] data) {
        System.arraycopy(data, 0, samples, 0, data.length);
    }

    public void copy(MonoBlockData monoBlockData, int offset) {
        for (int n=0; n<monoBlockData.size() && (n+offset) < samples.length; n++) {
            samples[n + offset] = monoBlockData.get(n);
        }
    }
}
