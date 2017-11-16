package se.softcoded.chordmasta.util;

public class NanoTime {
    public static double toUsec(long nano) {
        return (double)nano/1000.0;
    }
    public static double toMsec(long nano) {
        return (double)nano/1000000.0;
    }
}
