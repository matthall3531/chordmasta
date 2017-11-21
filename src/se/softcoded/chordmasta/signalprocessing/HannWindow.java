package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;

public class HannWindow implements ProcessUnit {
    public double[] factors;
    public HannWindow(int size) {
        factors = new double[size];
        for (int i = 0; i < size; i++) {
            factors[i] = 0.5 * (1 - Math.cos(2*Math.PI*i/size));
        }
    }

    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData monoIn = (MonoBlockData)in;
        MonoBlockData monoOut = (MonoBlockData)out;
        for (int i=0; i<out.size(); i++) {
            monoOut.set(i, monoIn.get(i) * factors[i]);
        }
    }
}
