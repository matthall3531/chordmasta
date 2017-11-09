package se.softcoded.chordmasta.util;

public class HistoryBuffer {
    private double[] buffer;
    private int nextPositionToAdd = 0;

    public HistoryBuffer(int size) {
        buffer = new double[size];
        for (int n = 0; n < size; n++) {
            buffer[n] = 0.0;
        }
    }

    public void add(double val) {
        buffer[nextPositionToAdd++] = val;
        if (nextPositionToAdd >= buffer.length) {
            nextPositionToAdd = 0;
        }
    }

    /**
     * Get a value in the circular buffer.
     * @param offset Means offset from latest sample. So 0 means the latest sample added.
     * @return Sample value
     */
    public double get(int offset) {
        int index = nextPositionToAdd - 1 - offset;
        if (index < 0) {
            index += buffer.length;
        }
        return buffer[index];
    }

    public int size() {
        return buffer.length;
    }
}
