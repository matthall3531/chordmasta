package se.softcoded.chordmasta;

import java.util.Vector;

public class QuickIndexSort<T extends Comparable>  {
    private Vector<T> numbers;
    private int[] index;
    private int number;

    public void sort(Vector<T> values, int[] index) {
        // check for empty or null array
        if (values ==null || values.size()==0){
            return;
        }
        this.numbers = values;
        this.index = index;
        number = values.size();
        quicksort(0, number - 1);
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        T pivot = numbers.get(index[low + (high-low)/2]);

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while (numbers.get(index[i]).compareTo(pivot) < 0) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while (numbers.get(index[j]).compareTo(pivot) > 0) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    private void exchange(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }
}