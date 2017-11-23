package se.softcoded.chordmasta.csv;

import se.softcoded.chordmasta.signalprocessing.FFTResult;

public class FFTResultColumnHandler implements ColumnHandler {
    private FFTResult fftResult;

    public FFTResultColumnHandler(FFTResult fftResult) {
        this.fftResult = fftResult;
    }

    @Override
    public String getRow(int row) {
        String s = "";
        if (row < fftResult.size()) {
            s +=fftResult.get(row).abs();
        }
        return s;
    }
}
