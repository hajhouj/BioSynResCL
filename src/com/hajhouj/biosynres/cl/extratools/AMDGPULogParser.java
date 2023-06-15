package com.hajhouj.biosynres.cl.extratools;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class AMDGPULogParser {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java AMDGPULogParser <input log file> <output csv file>");
            System.exit(1);
        }

        Path logPath = Paths.get(args[0]);
        Path csvPath = Paths.get(args[1]);
        Pattern pattern = Pattern.compile("^(\\d+)[\\s\\S]+(\\d+)%[\\s\\S]+(\\d+)%");

        try (BufferedReader reader = Files.newBufferedReader(logPath);
             BufferedWriter writer = Files.newBufferedWriter(csvPath)) {

            // Write CSV Header
            writer.write("Timestamp,GPU Usage(%),GPU Memory(%)\n");

            String line;
            String timestamp = null;
            while ((line = reader.readLine()) != null) {
                // Capture timestamp
                if (!line.startsWith("=")) {
                    timestamp = line.trim();
                    continue;
                }

                // Parse GPU statistics
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String gpuUsage = matcher.group(2);
                    String gpuMemory = matcher.group(3);
                    writer.write(String.format("%s,%s,%s\n", timestamp, gpuUsage, gpuMemory));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
