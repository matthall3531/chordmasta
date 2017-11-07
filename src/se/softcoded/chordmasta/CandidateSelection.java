package se.softcoded.chordmasta;

public class CandidateSelection implements ProcessUnit {
    private final PianoNotes notes;

    public CandidateSelection(PianoNotes notes) {
        this.notes = notes;
    }

    @Override
    public void process(BlockData in, BlockData out) {
        FFTResult fftResult = (FFTResult)in;
        CandidateSet candidateSet = (CandidateSet)out;


    }
}
