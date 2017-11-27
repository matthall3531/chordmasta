package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;

public interface SignalGenerator {
    boolean generateBlock(int nrOfSamplesInBlock, BlockData blockData);
}
