package se.softcoded.chordmasta;

import se.softcoded.chordmasta.signalprocessing.FFTResult;
import se.softcoded.chordmasta.signalprocessing.ProcessUnit;

public class CandidateSelection implements ProcessUnit {
    private final double sampleRate;
    private final PianoNotes notes;

    public CandidateSelection(PianoNotes notes, double Fs) {
        this.notes = notes;
        this.sampleRate = Fs;
    }

    /**
     * Check for the highest magnitude frequency components from the FFT.
     *
     *
     *
     * @param in
     * @param out
     */
    @Override
    public void process(BlockData in, BlockData out) {
        FFTResult fftResult = (FFTResult)in;
        CandidateSet candidateSet = (CandidateSet)out;

        int[] binIdx = fftResult.slice(0, fftResult.size()/2).getSortedIndex();

        for (int idx = 0; idx < 10; idx++) {
            int bin = binIdx[idx];
            double binMiddleFreq = calculateBinFrequency(bin, fftResult.size());
            PianoNotes.PianoKey key = notes.findNote(binMiddleFreq);
            System.out.println(idx + ". " + "binMiddleFrequency="+binMiddleFreq+", key="+key+", mag="+fftResult.get(bin).abs());
        }
    }

    /**
     *
     * @param bin
     * @param numberOfBins
     * @return
     */
    private double calculateBinFrequency(int bin, int numberOfBins) {
        double binBandWidth = sampleRate/numberOfBins;
        return (double)bin/(double)numberOfBins * sampleRate + binBandWidth / 2.0;
    }
}
