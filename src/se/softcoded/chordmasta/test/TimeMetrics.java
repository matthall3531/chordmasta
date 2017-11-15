package se.softcoded.chordmasta.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TimeMetrics {

    private class Metric {
        String name;
        int measurements = 0;
        long avg = 0;
        long max = -Long.MAX_VALUE;
        long min = Long.MAX_VALUE;

        Metric(String name) {
            this.name = name;
        }

        void add(OngoingMeasurement meas) {
            long nanosecs = meas.stop - meas.start;
            max = Math.max(nanosecs, max);
            min = Math.min(nanosecs, min);
            avg = (avg * measurements + nanosecs) / (measurements + 1);
            measurements += 1;
        }
    }

    private class OngoingMeasurement {
        long start;
        long stop;
    }

    private String name;
    private Map<String, Metric> metricMap = new HashMap<>();
    private Map<String, OngoingMeasurement> ongoingMeasurementMap= new HashMap<>();

    public TimeMetrics(String name) {
        this.name = name;
    }

    public void start(String metricName) {
        if (!metricMap.containsKey(metricName)) {
            metricMap.put(metricName, new Metric(metricName));
        }
        OngoingMeasurement meas = new OngoingMeasurement();
        ongoingMeasurementMap.put(metricName, meas);
        meas.start = System.nanoTime();
    }

    public void stop(String metricName) {
        long stopTime = System.nanoTime();
        OngoingMeasurement meas = ongoingMeasurementMap.remove(metricName);
        meas.stop = stopTime;
        metricMap.get(metricName).add(meas);
    }

    public void print() {
        Iterator it = metricMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String name = (String)pair.getKey();
            Metric metric = (Metric)pair.getValue();
            System.out.println("Metric : " + metric.name);
            System.out.println("   min : " + metric.min);
            System.out.println("   avg : " + metric.avg);
            System.out.println("   max : " + metric.max);
            System.out.println("     n : " + metric.measurements);
        }
    }
}
