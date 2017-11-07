package se.softcoded.chordmasta;

import java.util.ArrayList;
import java.util.Collections;

public class PianoNotes {
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
            PianoKey key = new PianoKey(freq, freqLower + (freq - freqLower)/2, freqUpper - (freqUpper - freq)/2, noteName);
            pianoKeys.add(key);
            System.out.println(key);
        }
    }

    public PianoKey findNote(double frequency) {
        Collections.binarySearch(pianoKeys,
                new PianoKey(frequency, 0, 0, ""),
                (o1, o2) -> 0);
        return null;
    }

}


