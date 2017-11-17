package se.softcoded.chordmasta;

import java.util.*;

public class CandidateSet extends BlockData {
    public class Candidate {
        PianoNotes.PianoKey key;
        double magnitude = 0.0;
        int hits = 0;
    }

    private Map<PianoNotes.PianoKey, Candidate> candidateMap = new HashMap<>();
    private ArrayList<Candidate> candidates = new ArrayList<>();

    @Override
    public int size() {
        return 0;
    }

    public void add(PianoNotes.PianoKey key, double magnitude) {
        Candidate candidate;
        if (candidateMap.containsKey(key)) {
            candidate = candidateMap.get(key);
        }
        else {
            candidate = new Candidate();
            candidateMap.put(key, candidate);
            candidates.add(candidate);
        }
        candidate.key = key;
        candidate.magnitude += magnitude;
        candidate.hits++;
    }

    public List<Candidate> getCandidates(Comparator<Candidate> comparator) {
        candidates.sort(comparator);
        return candidates;
    }


}
