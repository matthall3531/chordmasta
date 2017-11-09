package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;

public interface ProcessUnit {
    void process(BlockData in, BlockData out);
}
