package se.softcoded.chordmasta;

import se.softcoded.chordmasta.signalprocessing.*;
import se.softcoded.chordmasta.test.TimeMetrics;
import se.softcoded.chordmasta.util.SineWaveGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final int SAMPLE_RATE = 44100;
    private static final int DECIMATION_FACTOR = 4;
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
            TimeMetrics metrics = new TimeMetrics("Audio-chain");
            int loops = 0;

            StereoToMono stereoToMono = new StereoToMono();
            LowPassFilter filter = new LowPassFilter();
            Decimator decimationFilter = new Decimator(DECIMATION_FACTOR);
            FFT fft = new FFT();
            PianoNotes notes = new PianoNotes();
            CandidateSelection candidateSelection = new CandidateSelection(notes, SAMPLE_RATE / DECIMATION_FACTOR);
            while(!done) {
                try {

                    StereoBlockData stereoblock = micDataQueue.take();

                    metrics.start("main-loop");

                    MonoBlockData monoBlock = new MonoBlockData(BLOCK_SIZE);
                    metrics.start("stereo-to-mono");
                    stereoToMono.process(stereoblock, monoBlock);
                    metrics.stop("stereo-to-mono");

                    MonoBlockData filteredData = new MonoBlockData(BLOCK_SIZE);
                    metrics.start("low-pass-filter");
                    filter.process(monoBlock, filteredData);
                    metrics.stop("low-pass-filter");

                    MonoBlockData decimatedData = new MonoBlockData(BLOCK_SIZE / DECIMATION_FACTOR);
                    metrics.start("decimation");
                    decimationFilter.process(filteredData, decimatedData);
                    metrics.stop("decimation");

                    FFTResult fftResult = new FFTResult(decimatedData.size());
                    metrics.start("fft");
                    fft.process(decimatedData, fftResult);
                    metrics.stop("fft");

                    CandidateSet candidates = new CandidateSet();
                    metrics.start("candidate-selection");
                    candidateSelection.process(fftResult, candidates);
                    metrics.stop("candidate-selection");

                    metrics.stop("main-loop");

                    if ((loops % 100) == 0) {
                        metrics.print();
                    }

                    loops++;
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
