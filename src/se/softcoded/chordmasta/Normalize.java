package se.softcoded.chordmasta;

import se.softcoded.chordmasta.signalprocessing.FFTResult;
import se.softcoded.chordmasta.signalprocessing.ProcessUnit;

public class Normalize implements ProcessUnit {
    @Override
    public void process(BlockData in, BlockData out) {
        FFTResult fftResult = (FFTResult)in;
        MonoBlockData normalized = (MonoBlockData)out;

        double maxMagnitude = fftResult.getMaxValue().abs();
        for(int idx=0; idx<fftResult.size(); idx++) {
            if (maxMagnitude > 0.0) {
                normalized.set(idx, fftResult.get(idx).abs() / maxMagnitude);
            }
            else {
                normalized.set(idx, 0);
            }
        }
    }
}
