package se.softcoded.chordmasta;

import java.util.Vector;

public class FFTResult extends BlockData {
    private QuickIndexSort<Complex> sorter = new QuickIndexSort<Complex>();
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

    public int[] getSortedIndex() {
        int[] sortedIndex = new int[data.size()];
        for (int n=0; n<sortedIndex.length; n++) {
            sortedIndex[n] = n;
        }
        sorter.sort(data, sortedIndex);
        return sortedIndex;
    }
}
