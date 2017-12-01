package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;

public class SampleRateExpander implements ProcessUnit {
    private final int factor;
    private LowPassFilter filter = new LowPassFilter();

    public SampleRateExpander(int factor) {
        this.factor = factor;
    }

    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData data = (MonoBlockData)in;
        MonoBlockData expanded = new MonoBlockData(out.size());
        for (int idx=0; idx<data.size(); idx++) {
            expanded.set(idx * factor, factor * data.get(idx));
        }
        filter.process(expanded, out);
    }
}
