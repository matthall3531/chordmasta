package se.softcoded.chordmasta.csv;

import se.softcoded.chordmasta.CandidateSet;
import se.softcoded.chordmasta.MonoBlockData;
import se.softcoded.chordmasta.StereoBlockData;
import se.softcoded.chordmasta.signalprocessing.FFTResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CsvFile {
    private final File file;
    private List<ColumnHandler> columns = new LinkedList<>();
    private List<String> columnNames = new LinkedList<>();
    private int maxColumnSize = 0;

    public CsvFile(File file) {
        this.file = file;
    }

    public void addColumn(String name, StereoBlockData stereoblock) {
        MonoBlockData left = new MonoBlockData(stereoblock.size());
        MonoBlockData right = new MonoBlockData(stereoblock.size());
        stereoblock.split(left, right);
        columnNames.add(name + "_left");
        columns.add(ColumnHandlerFactory.create(left));
        maxColumnSize = Math.max(maxColumnSize, left.size());
        columnNames.add(name + "_right");
        columns.add(ColumnHandlerFactory.create(right));
        maxColumnSize = Math.max(maxColumnSize, right.size());
    }
    public void addColumn(String name, MonoBlockData monoBlockData) {
        columnNames.add(name);
        columns.add(ColumnHandlerFactory.create(monoBlockData));
        maxColumnSize = Math.max(maxColumnSize, monoBlockData.size());
    }

    public void addColumn(String name, FFTResult fftResult) {
        columnNames.add(name);
        columns.add(ColumnHandlerFactory.create(fftResult));
        maxColumnSize = Math.max(maxColumnSize, fftResult.size());
    }

    public void addColumn(String name, List<CandidateSet.Candidate> candidateList) {

    }

    public void save() {
        BufferedWriter out = null;
        try {
            FileWriter fstream = new FileWriter(file); //true tells to append data.
            out = new BufferedWriter(fstream);

            for (String name : columnNames) {
                out.write(name + ",");
            }
            out.newLine();

            for (int row = 0; row < maxColumnSize; row++) {
                for (ColumnHandler ch : columns) {
                    out.write(ch.getRow(row) + ",");
                }
                out.newLine();
            }

        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if(out != null) {
                try {
                    out.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addColumn(String name, double[] data) {
        MonoBlockData dataBlock = new MonoBlockData(data.length);
        dataBlock.copy(data);
        addColumn(name, dataBlock);
    }
}
