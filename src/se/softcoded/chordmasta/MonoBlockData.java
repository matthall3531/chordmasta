package se.softcoded.chordmasta;

import java.util.Vector;

public class MonoBlockData<T> extends BlockData {
    private Vector<T> samples = new Vector<>();

    public MonoBlockData(int blockSize) {
        samples.setSize(blockSize);
    }
    public void set(int index, T data) {
        samples.set(index, data);
    }
    public T get(int index) {
        return samples.get(index);
    }

    @Override
    public int size() {
        return samples.size();
    }

    public void copy(Double[] data) {
        samples.copyInto(data);
    }
}
