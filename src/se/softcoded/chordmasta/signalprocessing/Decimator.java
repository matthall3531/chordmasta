package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;

public class Decimator implements ProcessUnit {
    private final int frequencyDivider;

    public Decimator(int divide) {
        frequencyDivider = divide;
    }

    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData monoIn = (MonoBlockData)in;
        MonoBlockData monoOut = (MonoBlockData)out;

        for (int sample = 0, outIndex = 0; sample < monoIn.size(); sample+=frequencyDivider, outIndex++) {
            monoOut.set(outIndex, monoIn.get(sample));
        }
    }
}
