package se.softcoded.chordmasta.signalprocessing;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.StereoBlockData;

public class StereoToMono implements ProcessUnit {
    @Override
    public void process(BlockData in, BlockData out) {
        StereoBlockData stereo = (StereoBlockData)in;
        MonoBlockData mono = (MonoBlockData)out;

        for (int n = 0; n < stereo.size(); n++) {
            mono.set(n, (stereo.getLeft(n) + stereo.getRight(n)) / 2);
        }
    }
}
