package se.softcoded.chordmasta.csv;

import se.softcoded.chordmasta.MonoBlockData;

public class ColumnHandlerFactory {
    public static ColumnHandler create(MonoBlockData monoblock) {
        return new MonoBlockDataColumnHandler(monoblock);
    }
}
