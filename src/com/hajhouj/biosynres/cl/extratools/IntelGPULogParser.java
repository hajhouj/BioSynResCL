package com.hajhouj.biosynres.cl.extratools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntelGPULogParser {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java IntelGPULogParser <input log file> <output csv file>");
            System.exit(1);
        }

        Path logPath = Paths.get(args[0]);
        Path csvPath = Paths.get(args[1]);
        Pattern pattern = Pattern.compile("^render busy:\\s+(\\d+)%");

        try (BufferedReader reader = Files.newBufferedReader(logPath);
             BufferedWriter writer = Files.newBufferedWriter(csvPath)) {

            // Write CSV Header
            writer.write("Timestamp,GPU Usage(%)\n");

            String line;
            String timestamp = null;
            while ((line = reader.readLine()) != null) {
                // Capture timestamp
                if (!line.startsWith("render")) {
                    timestamp = line.trim();
                    continue;
                }

                // Parse GPU usage
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String gpuUsage = matcher.group(1);
                    writer.write(String.format("%s,%s\n", timestamp, gpuUsage));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}