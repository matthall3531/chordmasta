package se.softcoded.chordmasta;

public class LowPassFilter implements ProcessUnit {
    private double coeff[] = new double[] {
            0.0042028,
            -0.0060293,
            -0.0393217,
            0.0252019,
            0.2863263,
            0.4592401,
            0.2863263,
            0.0252019,
            -0.0393217,
            -0.0060293,
            0.0042028
    };
    private HistoryBuffer history = new HistoryBuffer(coeff.length);

    public LowPassFilter() {
        for (int n = 0; n < history.size(); n++) {
            history.add(0.0);
        }
    }

    @Override
    public void process(BlockData in, BlockData out) {
        MonoBlockData monoIn = (MonoBlockData)in;
        MonoBlockData monoOut = (MonoBlockData)out;
        for (int sample = 0; sample < monoIn.size(); sample++) {
            monoOut.set(sample, calculateFilteredValue((double)monoIn.get(sample)));
        }
    }

    private double calculateFilteredValue(double val) {
        double filteredVal = 0.0;
        history.add(val);
        for (int n = 0; n < coeff.length; n++) {
            filteredVal += coeff[n] * history.get(n);
        }
        return filteredVal;
    }
}
