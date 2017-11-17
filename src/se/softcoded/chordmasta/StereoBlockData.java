package se.softcoded.chordmasta;

import java.util.Vector;

public class StereoBlockData extends BlockData {
    private Vector<Double> leftSamples = new Vector<>();
    private Vector<Double> rightSamples = new Vector<>();
    private int currentSample = 0;

    public StereoBlockData(int size) {
        rightSamples.setSize(size);
        leftSamples.setSize(size);
    }

    public void appendSample(double left, double right) {
        if (currentSample >= leftSamples.capacity()) {
            throw new IndexOutOfBoundsException();
        }
        leftSamples.set(currentSample, left);
        rightSamples.set(currentSample, right);
        currentSample++;
    }

    @Override
    public int size() {
        return leftSamples.capacity();
    }

    public double getLeft(int index) {
        try {
            return leftSamples.get(index);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getRight(int index) {
        try {
            return rightSamples.get(index);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
