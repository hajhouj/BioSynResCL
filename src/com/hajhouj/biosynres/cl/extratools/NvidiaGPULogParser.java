package com.hajhouj.biosynres.cl.extratools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NvidiaGPULogParser {
    public static void main(String[] args) {
        String filePath = "gpu_log.txt";
        String csvOutputPath = "nvidia_run.csv";

        parseNvidiaSmiLog(filePath, csvOutputPath);
    }

    private static void parseNvidiaSmiLog(String filePath, String csvOutputPath) {
        Path path = Paths.get(filePath);

        // Pattern to match GPU-Util and Memory-Usage in the nvidia-smi output
        Pattern p = Pattern.compile("\\|\\s+(\\d+)%(?:[^|]*\\|){2}\\s+(\\d+)MiB /\\s+(\\d+)MiB.*\\|\\s+(\\d+)%.*\\|");
        // Pattern to match the timestamp in the nvidia-smi output
        Pattern timestampPattern = Pattern.compile("[A-Za-z]{3} [A-Za-z]{3} \\d{1,2} \\d{2}:\\d{2}:\\d{2} \\d{4}");

        SimpleDateFormat timestampFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

        try (BufferedReader reader = Files.newBufferedReader(path);
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(csvOutputPath)))) {

            writer.println("timestamp, gpu_usage(%), gpu_memory_usage(MiB), total_memory(MiB), gpu_util(%)");

            String line;
            String timestamp = ""; // placeholder for timestamp
            while ((line = reader.readLine()) != null) {
                Matcher timestampMatcher = timestampPattern.matcher(line);
                if (timestampMatcher.find()) {
                    try {
                        // parse the timestamp string to Date
                        Date date = timestampFormat.parse(timestampMatcher.group());
                        timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Matcher m = p.matcher(line);

                if (m.find()) {
                    String gpuUsage = m.group(1);
                    String gpuMemoryUsage = m.group(2);
                    String totalMemory = m.group(3);
                    String gpuUtil = m.group(4);
                    writer.printf("%s, %s, %s, %s, %s%n", timestamp, gpuUsage, gpuMemoryUsage, totalMemory, gpuUtil);
                }
            }

            System.out.println("CSV file generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
