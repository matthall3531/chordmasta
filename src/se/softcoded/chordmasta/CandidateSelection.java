package se.softcoded.chordmasta;

import se.softcoded.chordmasta.signalprocessing.ProcessUnit;

public class CandidateSelection implements ProcessUnit {
    private final double sampleRate;
    private final PianoNotes notes;
    private final double minFrequency;
    private final double maxFrequency;

    public CandidateSelection(PianoNotes notes, double Fs) {
        this.notes = notes;
        this.sampleRate = Fs;
        this.minFrequency = notes.getNote(0).freqLower;
        this.maxFrequency = notes.getNote(notes.getNumnerOfNotes() - 1).freqUpper;
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
        MonoBlockData data = (MonoBlockData)in;
        CandidateSet candidateSet = (CandidateSet)out;

        int[] binIdx = data.getSortedIndex();
        int size = data.size();

        for (int idx = 0; idx < 20; idx++) {
            int bin = binIdx[idx];
            double binMiddleFreq = calculateBinFrequency(bin, size);
            if (binMiddleFreq > minFrequency && binMiddleFreq < maxFrequency) {
                PianoNotes.PianoKey[] keys = notes.findNotes(binMiddleFreq, sampleRate / (2 * size));
                for (PianoNotes.PianoKey key : keys) {
                    //System.out.println(idx + ". " + "binMiddleFrequency=" + binMiddleFreq + ", key=" + key + ", mag=" + fftResult.get(bin).abs());
                    if (binMiddleFreq > key.freqLower && binMiddleFreq < key.freqUpper) {
                        candidateSet.add(key, data.get(bin));
                    }
                    else {
                        System.out.println("Discarding key : " + key);
                    }
                }
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
        double numberOfBinsOriginal = 2 * numberOfBins;
        double binBandWidth = sampleRate/numberOfBinsOriginal;
        return (double)bin/(double)numberOfBinsOriginal * sampleRate + binBandWidth / 2.0;
    }
}
