package se.softcoded.chordmasta.test;

import se.softcoded.chordmasta.util.NanoTime;

import java.util.*;

public class TimeMetrics {

    private class Metric {
        String name;
        int measurements = 0;
        long avg = 0;
        long max = -Long.MAX_VALUE;
        long min = Long.MAX_VALUE;

        Vector<Metric> childMetrics = new Vector<>();

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

        public void addChild(Metric metric) {
            childMetrics.add(metric);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);
            formatter.format("%-20s %10f %10f %10f\n",
                    name,
                    NanoTime.toMsec(min),
                    NanoTime.toMsec(avg),
                    NanoTime.toMsec(max));
            for (Metric m : childMetrics) {
                sb.append("  ");
                sb.append((char)0x02ea);
                sb.append("  ");
                sb.append(m.toString());
            }
            return sb.toString();
        }
    }

    private class OngoingMeasurement {
        long start;
        long stop;
    }

    private String name;
    private Map<String, Metric> metricMap = new HashMap<>();
    private Map<String, OngoingMeasurement> ongoingMeasurementMap= new HashMap<>();
    private Stack<Metric> metricStack = new Stack<>();
    private Vector<Metric> mainMetrics = new Vector<>();

    public TimeMetrics(String name) {
        this.name = name;
    }

    public void start(String metricName) {
        Metric current;
        if (!metricMap.containsKey(metricName)) {
            current = new Metric(metricName);
            metricMap.put(metricName, current);

            if (metricStack.empty()) {
                mainMetrics.add(current);
            }
            else {
                metricStack.peek().addChild(current);
            }
        }
        else {
            current = metricMap.get(metricName);
        }
        metricStack.push(current);
        OngoingMeasurement meas = new OngoingMeasurement();
        ongoingMeasurementMap.put(metricName, meas);
        meas.start = System.nanoTime();
    }

    public void stop(String metricName) {
        long stopTime = System.nanoTime();
        OngoingMeasurement meas = ongoingMeasurementMap.remove(metricName);
        meas.stop = stopTime;
        metricMap.get(metricName).add(meas);
        metricStack.pop();
    }

    public void print() {
        for (Metric metric : mainMetrics) {
            System.out.println(metric.toString());
        }
    }
}
