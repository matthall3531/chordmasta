package se.softcoded.chordmasta;

import java.util.Vector;

public class FFTResult extends BlockData {
    Vector<Complex> data = new Vector<>();

    public FFTResult(int size) {
        data.setSize(size);
    }

    @Override
    public int size() {
        return data.size();
    }

    public void set(int index, Complex val) {
        data.set(index, val);
    }

    public Complex get(int index) {
        return data.get(index);
    }
}
