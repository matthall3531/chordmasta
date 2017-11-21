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
     * Assign the bins to the notes and check which notes get the most hits.
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

        for (int idx = 0; idx < 200; idx++) {
            int bin = binIdx[idx];
            double binMiddleFreq = calculateBinFrequency(bin, fftResult.size());
            PianoNotes.PianoKey[] keys = notes.findNotes(binMiddleFreq, sampleRate/fftResult.size());
            for (PianoNotes.PianoKey key : keys) {
                //System.out.println(idx + ". " + "binMiddleFrequency=" + binMiddleFreq + ", key=" + key + ", mag=" + fftResult.get(bin).abs());
                candidateSet.add(key, fftResult.get(bin).abs());
            }
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
