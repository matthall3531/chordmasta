package se.softcoded.chordmasta;

import se.softcoded.chordmasta.signalprocessing.*;
import se.softcoded.chordmasta.util.SineWaveGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final int SAMPLE_RATE = 44100;
    private static final int BLOCK_SIZE = 4096;

    public static void main(String[] args) {
        // Create queue for communication between threads
        BlockingQueue<StereoBlockData> micDataQueue = new LinkedBlockingQueue();
        boolean done = false;

        new Thread(() -> {
            SineWaveGenerator sine = new SineWaveGenerator(SAMPLE_RATE, 440.0, 5.0);
            try {
                while (!done) {
                    StereoBlockData block = new StereoBlockData(BLOCK_SIZE);
                    sine.generateBlock(BLOCK_SIZE, block);
                    micDataQueue.add(block);
                    Thread.sleep(BLOCK_SIZE * 1000 / SAMPLE_RATE);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()-> {
            while(!done) {
                try {
                    StereoToMono stereoToMono = new StereoToMono();
                    LowPassFilter filter = new LowPassFilter();
                    Decimator decimationFilter = new Decimator(4);
                    FFT fft = new FFT();
                    PianoNotes notes = new PianoNotes();
                    CandidateSelection candidateSelection = new CandidateSelection(notes);

                    StereoBlockData stereoblock = micDataQueue.take();

                    MonoBlockData monoBlock = new MonoBlockData(BLOCK_SIZE);
                    stereoToMono.process(stereoblock, monoBlock);

                    MonoBlockData filteredData = new MonoBlockData(BLOCK_SIZE);
                    filter.process(monoBlock, filteredData);

                    MonoBlockData decimatedData = new MonoBlockData(BLOCK_SIZE / 4);
                    decimationFilter.process(filteredData, decimatedData);

                    FFTResult fftResult = new FFTResult(decimatedData.size());
                    fft.process(decimatedData, fftResult);

                    double maxValue = 0.0;
                    int maxIndex = 0;
                    for (int n = 1; n < fftResult.size(); n++) {
                       System.out.println(n + "=" + fftResult.get(n).abs());
                       if (maxValue < fftResult.get(n).abs()) {
                           maxIndex = n;
                           maxValue = fftResult.get(n).abs();
                       }
                    }
                    System.out.println("Max index = " + maxIndex + ", maxValue=" + maxValue);

                    CandidateSet candidates = new CandidateSet();
                    candidateSelection.process(fftResult, candidates);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
