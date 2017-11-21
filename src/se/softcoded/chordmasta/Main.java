package se.softcoded.chordmasta;

import se.softcoded.chordmasta.csv.CsvFile;
import se.softcoded.chordmasta.signalprocessing.*;
import se.softcoded.chordmasta.test.TimeMetrics;
import se.softcoded.chordmasta.util.SineWaveGenerator;
import se.softcoded.chordmasta.wavfile.WavFile;
import se.softcoded.chordmasta.wavfile.WavFileGenerator;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private static final int SAMPLE_RATE = 44100;
    private static final int DECIMATION_FACTOR = 4;
    private static final int BLOCK_SIZE = 4096;

    public static void main(String[] args) {
        // Create queue for communication between threads
        BlockingQueue<StereoBlockData> micDataQueue = new LinkedBlockingQueue();
        boolean[] done = new boolean[] { false };

        new Thread(() -> {
            //SineWaveGenerator audioGenerator = new SineWaveGenerator(SAMPLE_RATE, 440.0, 5.0);
            WavFileGenerator audioGenerator = new WavFileGenerator("./research/testfiles/ackordtest1.wav");
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
            LowPassFilter filter = new LowPassFilter();
            Decimator decimationFilter = new Decimator(DECIMATION_FACTOR);
            HannWindow hannWindow = new HannWindow(BLOCK_SIZE / DECIMATION_FACTOR);
            FFT fft = new FFT();
            PianoNotes notes = new PianoNotes();
            CandidateSelection candidateSelection = new CandidateSelection(notes, SAMPLE_RATE / DECIMATION_FACTOR);
            while(!done[0]) {
                try {
                    CsvFile csvFile = new CsvFile(new File("chordmasta_output_" + loops + ".csv"));

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

                    metrics.start("hann-window");
                    MonoBlockData hannWindowBlock = new MonoBlockData(decimatedData.size());
                    hannWindow.process(decimatedData, hannWindowBlock);
                    metrics.stop("hann-window");

                    // Zero pad
                    metrics.start("zero-padding");
                    MonoBlockData hannWindowDataPadded = new MonoBlockData(SAMPLE_RATE / DECIMATION_FACTOR, 0.0);
                    hannWindowDataPadded.copy(hannWindowBlock, 0);
                    metrics.stop("zero-padding");

                    FFTResult fftResult = new FFTResult(hannWindowDataPadded.size());
                    metrics.start("fft");
                    fft.process(hannWindowDataPadded, fftResult);
                    metrics.stop("fft");

                    CandidateSet candidates = new CandidateSet();
                    metrics.start("candidate-selection");
                    candidateSelection.process(fftResult, candidates);
                    metrics.stop("candidate-selection");

                    List<CandidateSet.Candidate> candidateList = candidates.getCandidates((c1, c2) -> c1.magnitude > c2.magnitude ? -1 : (c1.magnitude < c2.magnitude ? 1 : 0));

                    System.out.println("------------------------------------");
                    for (CandidateSet.Candidate c : candidateList) {
                        System.out.println("key="+c.key+", mag="+c.magnitude+", hits="+c.hits);
                    }

                    metrics.stop("main-loop");

                    csvFile.addColumn("Stereo", stereoblock);
                    csvFile.addColumn("Mono-data", monoBlock);
                    csvFile.addColumn("Low-pass-filter", filteredData);
                    csvFile.addColumn("Decimation", decimatedData);
                    csvFile.addColumn("Hann-window-factors", hannWindow.factors);
                    csvFile.addColumn("Hann-window", hannWindowBlock);
                    csvFile.addColumn("Zero-padded", hannWindowDataPadded);
                    csvFile.addColumn("FFT", fftResult);
                    csvFile.addColumn("Candidates", candidateList);
                    csvFile.save();

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
