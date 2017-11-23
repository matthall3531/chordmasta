package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.Normalize;
import se.softcoded.chordmasta.util.QuickIndexSort;
import se.softcoded.chordmasta.util.QuickIndexSortT;

import java.util.Vector;

public class FFTResult extends BlockData {
    QuickIndexSortT<Complex> sorter = new QuickIndexSortT<>();
    private Vector<Complex> data;
    public Complex maxValue = new Complex(0, 0);

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
        if (val.abs() > maxValue.abs()) {
            maxValue = val;
        }
    }

    public Complex get(int index) {
        return data.get(index);
    }

    public FFTResult slice(int indexLow, int indexHigh) {
        FFTResult newResult = new FFTResult(new Vector<>(data.subList(indexLow, indexHigh)));
        int[] sortedIdx = newResult.getSortedIndex();
        newResult.maxValue = newResult.get(sortedIdx[0]);
        return newResult;
    }

    public Complex getMaxValue() {
        return maxValue;
    }

    public void calculateMagnitude(MonoBlockData monoBlockData) {
        for (int i=0; i<data.size(); i++) {
            monoBlockData.set(i, data.get(i).abs());
        }
    }

    public int[] getSortedIndex() {
        int[] sortedIndex = new int[data.size()];
        for (int n=0; n<sortedIndex.length; n++) {
            sortedIndex[n] = n;
        }
        sorter.reverseSort(data, sortedIndex);
        return sortedIndex;
    }
}
