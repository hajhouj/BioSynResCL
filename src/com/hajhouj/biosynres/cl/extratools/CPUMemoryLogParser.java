package com.hajhouj.biosynres.cl.extratools;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class CPUMemoryLogParser {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java CPUMemoryLogParser <input log file> <output csv file>");
            System.exit(1);
        }

        Path logPath = Paths.get(args[0]);
        Path csvPath = Paths.get(args[1]);
        Pattern pattern = Pattern.compile("^CPU usage: (\\d+\\.?\\d*)%\\nMemory usage: (\\d+\\.?\\d*)%");

        try (BufferedReader reader = Files.newBufferedReader(logPath);
             BufferedWriter writer = Files.newBufferedWriter(csvPath)) {

            // Write CSV Header
            writer.write("Timestamp,CPU Usage(%),Memory Usage(%)\n");

            String line;
            String timestamp = null;
            String cpuUsage = null;
            String memoryUsage = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Mon") || line.startsWith("Tue") || line.startsWith("Wed") || 
                    line.startsWith("Thu") || line.startsWith("Fri") || line.startsWith("Sat") ||
                    line.startsWith("Sun")) {
                    timestamp = line.trim();
                } else if (line.startsWith("CPU usage:")) {
                    cpuUsage = line.split(":")[1].trim().replace("%", "");
                } else if (line.startsWith("Memory usage:")) {
                    memoryUsage = line.split(":")[1].trim().replace("%", "");
                    writer.write(String.format("%s,%s,%s\n", timestamp, cpuUsage, memoryUsage));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}