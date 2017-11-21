package se.softcoded.chordmasta.csv;

import se.softcoded.chordmasta.MonoBlockData;

class MonoBlockDataColumnHandler implements ColumnHandler {
    private MonoBlockData monoBlock;

    public MonoBlockDataColumnHandler(MonoBlockData monoBlock) {
        this.monoBlock = monoBlock;
    }

    @Override
    public String getRow(int row) {
        String s = "";
        if (row < monoBlock.size()) {
            s += monoBlock.get(row);
        }
        return s;
    }
}
