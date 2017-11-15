package se.softcoded.chordmasta;

import java.util.Vector;

public class MonoBlockData<T> extends BlockData {
    private Vector<T> samples = new Vector<>();

    public MonoBlockData(int blockSize) {
        samples.setSize(blockSize);
    }

    public MonoBlockData(int blockSize, T initValue) {
        this(blockSize);
        for (int i=0; i<blockSize; i++) {
            samples.set(i, initValue);
        }
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

    public void copy(MonoBlockData monoBlockData, int offset) {
        for (int n=0; n<monoBlockData.size() && (n+offset) < samples.size(); n++) {
            samples.set(n + offset, (T)monoBlockData.get(n));
        }
    }
}
