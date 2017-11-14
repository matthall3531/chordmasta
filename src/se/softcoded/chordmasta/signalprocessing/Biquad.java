package se.softcoded.chordmasta.signalprocessing;

import org.junit.Test;

import se.softcoded.chordmasta.BlockData;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.test.TestTools;
import se.softcoded.chordmasta.util.HistoryBuffer;

import javax.naming.OperationNotSupportedException;

public class Biquad implements ProcessUnit {
    public enum BiquadFilterType {
        LOW_PASS
    }

    static private BiquadCoefficientFactory coeffCalc = new BiquadCoefficientFactory();

    private HistoryBuffer xDelayed = new HistoryBuffer(2);
    private HistoryBuffer yHistory = new HistoryBuffer(2);
    private double[] a = new double[3];
    private double[] b = new double[3];
    
    public Biquad(double sampleRate, double fCenter, double Q, BiquadFilterType filterType) throws OperationNotSupportedException {
        if (filterType == BiquadFilterType.LOW_PASS) {
            coeffCalc.createLowPass(sampleRate, fCenter, Q, a, b);
        }
        else {
            throw new OperationNotSupportedException("Filter type not implemented.");
        }
    }

    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData monoIn = (MonoBlockData)in;
        MonoBlockData monoOut = (MonoBlockData)out;

        for (int n=0; n<monoIn.size(); n++) {
            double y = calculateSample((double)monoIn.get(n));
            yHistory.add(y);
            monoOut.set(n, y);
        }
    }

    private double calculateSample(double x) {
        return b[0] * x + b[1] * xDelayed.get(0) + b[2] * xDelayed.get(1) -
                a[1] * yHistory.get(0) - a[2] * yHistory.get(1);
    }
}
