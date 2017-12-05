package se.softcoded.chordmasta;

import se.softcoded.chordmasta.csv.CsvFile;
import se.softcoded.chordmasta.ext.wav.WavFileGenerator;
import se.softcoded.chordmasta.signalprocessing.*;
import se.softcoded.chordmasta.test.TimeMetrics;
import se.softcoded.chordmasta.util.SineWaveGenerator;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Approach:
 *
 * The audio chain takes a block of 44.1kHz audio and turns it into a mono stream.
 *
 * In order to get a high resolution Fourier Transform with small frequency span
 * between bins, the signal is expanded to a 176.4kHz signal.
 */
public class Main {
    private static final int SAMPLE_RATE = 44100;
    private static final int EXPAND_FACTOR = 4;
    private static final int EXPANDED_SAMPLE_RATE = 44100 * EXPAND_FACTOR;
    private static final int DECIMATION_FACTOR = 1;
    private static final int KEY_RANGE_HZ = 5500;
    private static final int BLOCK_SIZE = 32768;

    public static void main(String[] args) {
        // Create queue for communication between threads
        BlockingQueue<StereoBlockData> micDataQueue = new LinkedBlockingQueue();
        boolean[] done = new boolean[] { false };

        new Thread(() -> {
            SineWaveGenerator audioGenerator = new SineWaveGenerator(SAMPLE_RATE, 440.0, 5.0);
            //WavFileGenerator audioGenerator = new WavFileGenerator("./research/testfiles/ackordtest1.wav");
            //WhiteNoise audioGenerator = new WhiteNoise(0.5, 1.0);
            try {
                while (!done[0]) {
                    StereoBlockData block = new StereoBlockData(BLOCK_SIZE);
                    if (audioGenerator.generateBlock(BLOCK_SIZE, block)) {
                        micDataQueue.add(block);
                    }
                    else {
                        done[0] = true;
                    }
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
            SampleRateExpander sampleRateExpander = new SampleRateExpander(EXPAND_FACTOR);
            LowPassFilter filter = new LowPassFilter();
            Decimator decimationFilter = new Decimator(DECIMATION_FACTOR);
            //HannWindow hannWindow = new HannWindow((BLOCK_SIZE * EXPAND_FACTOR) / DECIMATION_FACTOR);
            HannWindow hannWindow = new HannWindow((BLOCK_SIZE * EXPAND_FACTOR));
            FFT fft = new FFT();
            Normalize normalize = new Normalize();
            PianoNotes notes = new PianoNotes();
            CandidateSelection candidateSelection = new CandidateSelection(notes, EXPANDED_SAMPLE_RATE / DECIMATION_FACTOR);
            while(!done[0]) {
                try {
                    CsvFile csvFile = new CsvFile(new File("chordmasta_output_" + loops + ".csv"));

                    StereoBlockData stereoblock = micDataQueue.take();

                    metrics.start("main-loop");

                    metrics.start("stereo-to-mono");
                    MonoBlockData monoBlock = new MonoBlockData(BLOCK_SIZE);
                    stereoToMono.process(stereoblock, monoBlock);
                    metrics.stop("stereo-to-mono");

                    metrics.start("upsample");
                    MonoBlockData expandedSignal = new MonoBlockData(BLOCK_SIZE * EXPAND_FACTOR);
                    sampleRateExpander.process(monoBlock, expandedSignal);
                    metrics.stop("upsample");

                    //metrics.start("low-pass-filter");
                    //MonoBlockData filteredData = new MonoBlockData(BLOCK_SIZE);
                    //filter.process(monoBlock, filteredData);
                    //metrics.stop("low-pass-filter");

                    //metrics.start("decimation");
                    //MonoBlockData decimatedData = new MonoBlockData((BLOCK_SIZE * EXPAND_FACTOR) / DECIMATION_FACTOR);
                    //decimationFilter.process(expandedSignal, decimatedData);
                    //metrics.stop("decimation");

                    metrics.start("hann-window");
                    MonoBlockData hannWindowBlock = new MonoBlockData(expandedSignal.size());
                    hannWindow.process(expandedSignal, hannWindowBlock);
                    metrics.stop("hann-window");

                    metrics.start("fft");
                    FFTResult fftResult = new FFTResult(hannWindowBlock.size());
                    fft.process(hannWindowBlock, fftResult);
                    metrics.stop("fft");

                    metrics.start("normalize");
                    FFTResult sliced = fftResult.slice(0, fftResult.size()/2);
                    MonoBlockData normalizedMagnitude = new MonoBlockData(sliced.size());
                    normalize.process(sliced, normalizedMagnitude);
                    metrics.stop("normalize");

                    metrics.start("candidate-selection");
                    CandidateSet candidates = new CandidateSet();
                    candidateSelection.process(normalizedMagnitude, candidates);
                    metrics.stop("candidate-selection");

                    List<CandidateSet.Candidate> candidateList = candidates.getCandidates((c1, c2) -> c1.magnitude > c2.magnitude ? -1 : (c1.magnitude < c2.magnitude ? 1 : 0));

                    metrics.stop("main-loop");

                    System.out.println("------------------------------------");
                    for (CandidateSet.Candidate c : candidateList) {
                        System.out.println("key="+c.key+", mag="+c.magnitude+", hits="+c.hits);
                    }

                    csvFile.addColumn("Stereo", stereoblock);
                    csvFile.addColumn("Mono-data", monoBlock);
                    csvFile.addColumn("Upsampled", expandedSignal);
                    //csvFile.addColumn("Low-pass-filter", filteredData);
                    //csvFile.addColumn("Decimation", decimatedData);
                    csvFile.addColumn("Hann-window-factors", hannWindow.factors);
                    csvFile.addColumn("Hann-window", hannWindowBlock);
                    csvFile.addColumn("FFT", fftResult);
                    csvFile.addColumn("FFT-small", fftResult.slice(0, 11050));
                    csvFile.addColumn("Normalized", normalizedMagnitude);
                    csvFile.addColumn("Candidates", candidateList);
                    csvFile.save();

                    if ((loops % 2) == 0) {
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
