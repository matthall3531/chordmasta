package se.softcoded.chordmasta;

import java.util.Vector;

public class FFTResult extends BlockData {
    private QuickIndexSort<Complex> sorter = new QuickIndexSort<>();
    private Vector<Complex> data;

    public FFTResult(int size) {
        data = new Vector<>();
        data.setSize(size);
    }

    private FFTResult(Vector<Complex> data) {
        this.data = data;
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
        sorter.reverseSort(data, sortedIndex);
        return sortedIndex;
    }

    public FFTResult slice(int indexLow, int indexHigh) {
        return new FFTResult(new Vector<>(data.subList(indexLow, indexHigh)));
    }
}
