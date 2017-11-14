package se.softcoded.chordmasta.signalprocessing;

class BiquadCoefficientFactory {
    /**
     * Creates a low pass normalized biquad.
     * @param sampleRate
     * @param fCenter
     * @param Q
     * @param a
     * @param b
     */
    public void createLowPass(double sampleRate, double fCenter, double Q, double[] a, double[] b) {
        double w0 = 2.0 * Math.PI * fCenter/sampleRate;
        double cosW0 = Math.cos(w0);
        double omega = Math.sin(w0)/(2.0 * Q);
        a[0] = 1 + omega;

        b[0] = ((1 - cosW0)/2.0) / a[0];
        b[1] = (1 - cosW0) / a[0];
        b[2] = b[0] / a[0];
        a[1] = (-2 * cosW0) / a[0];
        a[2] = (1 - omega) / a[0];
    }
}
