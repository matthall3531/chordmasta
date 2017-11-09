package se.softcoded.chordmasta.signalprocessing;

public class StereoToMono implements ProcessUnit {
    @Override
    public void process(BlockData in, BlockData out) {
        System.out.println("Stereo2Mono");
        StereoBlockData stereo = (StereoBlockData)in;
        MonoBlockData mono = (MonoBlockData)out;

        for (int n = 0; n < stereo.size(); n++) {
            mono.set(n, (stereo.getLeft(n) + stereo.getRight(n)) / 2);
        }
    }
}
