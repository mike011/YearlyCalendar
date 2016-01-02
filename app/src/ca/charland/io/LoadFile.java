package ca.charland.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoadFile {

    public static List<String> loadFile(String filename) {
        return loadFile(filename, false);
    }

    public static List<String> loadCSVFile(String filename) {
        return loadFile(filename, true);
    }

    public static List<String> loadFile(String filename, boolean isCSV) {
        List<String> lines = new ArrayList<String>();
        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (isCSV) {
                    while (strLine.split("\",\"").length < 10) {
                        strLine += br.readLine();
                    }
                }
                lines.add(strLine);
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lines;
    }
}
