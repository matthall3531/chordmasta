package se.softcoded.chordmasta;

import java.util.ArrayList;
import java.util.Collections;

public class PianoNotes {
    private final static double GUARD_BAND_DIVISOR = 1.5;

    public class PianoKey {
        public final double freq;
        public final double freqLower;
        public final double freqUpper;
        public final String noteName;

        public PianoKey(double freq, double freqLower, double freqUpper, String noteName) {
            this.freq = freq;
            this.freqLower = freqLower;
            this.freqUpper = freqUpper;
            this.noteName = noteName;
        }

        @Override
        public String toString() {
            return "PianoNote : " + noteName + " " + freqLower + " < " + freq + " < " + freqUpper;
        }

        public int compareTo(PianoKey o2) {
            if (o2.freq > freqLower && o2.freq < freqUpper) {
                return 0;
            }
            return freq < o2.freq ? -1 : 1;
        }
    }

    private String[] octaveNoteNames = new String[]{
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C"
    };
    private ArrayList<PianoKey> pianoKeys = new ArrayList<>();

    public PianoNotes() {
        for (int n = 1; n <= 102; n++) {
            double freqLower = Math.pow(2, ((double) n - 50.0) / 12.0) * 440.0;
            double freq = Math.pow(2, ((double) n - 49.0) / 12.0) * 440.0;
            double freqUpper = Math.pow(2, ((double) n - 48.0) / 12.0) * 440.0;
            String noteName = octaveNoteNames[(n - 1) % octaveNoteNames.length];
            PianoKey key = new PianoKey(freq, freqLower + (freq - freqLower)/GUARD_BAND_DIVISOR, freqUpper - (freqUpper - freq)/GUARD_BAND_DIVISOR, noteName);
            pianoKeys.add(key);
            System.out.println(key);
        }
    }

    public PianoKey findNote(double frequency) {
        int keyIdx = getKeyIdx(frequency);
        return pianoKeys.get(Math.max(0, Math.min(keyIdx, pianoKeys.size()-1)));
    }

    private int getKeyIdx(double frequency) {
        return (int)Math.round(12.0 * log2(frequency/440.0)) + 48;
    }

    private double log2(double v) {
        return Math.log(v) / Math.log(2);
    }

    public PianoKey[] findNotes(double f, double bandwidth) {
        int lowIdx = getKeyIdx(f - bandwidth);
        int hiIdx = getKeyIdx(f + bandwidth);
        int size = hiIdx - lowIdx + 1;
        PianoKey[] array = new PianoKey[size];
        for (int n=lowIdx; n >= 0 && n<=hiIdx; n++) {
            array[n - lowIdx] = pianoKeys.get(n);
        }
        return array;
    }

    public int getNumnerOfNotes() {
        return pianoKeys.size();
    }

    public PianoKey getNote(int index) {
        return pianoKeys.get(index);
    }
}


