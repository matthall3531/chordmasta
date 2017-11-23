package se.softcoded.chordmasta.csv;

import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.signalprocessing.FFTResult;

public class ColumnHandlerFactory {
    public static ColumnHandler create(MonoBlockData monoblock) {
        return new MonoBlockDataColumnHandler(monoblock);
    }

    public static ColumnHandler create(FFTResult fftResult) {
        return new FFTResultColumnHandler(fftResult);
    }
}
