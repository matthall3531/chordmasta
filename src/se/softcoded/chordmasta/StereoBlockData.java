package se.softcoded.chordmasta;

import java.util.Arrays;
import java.util.Vector;

public class StereoBlockData extends BlockData {
    private final double[] leftSamples;
    private final double[] rightSamples;
    private int currentSample = 0;

    public StereoBlockData(int size) {
        rightSamples = new double[size];
        leftSamples = new double[size];
    }

    public void appendSample(double left, double right) {
        if (currentSample >= leftSamples.length) {
            throw new IndexOutOfBoundsException();
        }
        leftSamples[currentSample] =  left;
        rightSamples[currentSample] = right;
        currentSample++;
    }

    @Override
    public int size() {
        return leftSamples.length;
    }

    public double getLeft(int index) {
        try {
            return leftSamples[index];
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getRight(int index) {
        try {
            return rightSamples[index];
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void split(MonoBlockData left, MonoBlockData right) {
        left.copy(leftSamples);
        right.copy(rightSamples);
    }
}
