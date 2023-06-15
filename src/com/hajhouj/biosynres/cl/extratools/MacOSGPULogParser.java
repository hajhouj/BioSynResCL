package com.hajhouj.biosynres.cl.extratools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import java.io.*;
import java.util.*;

public class MacOSGPULogParser {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("gpu_log.txt"));
        Map<String, PrintWriter> writerMap = new HashMap<>();

        String line;
        String timestamp = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("| SwiftyGPU")) {
                // Extract timestamp
                timestamp = line.split("\\|")[1].trim().replace("| SwiftyGPU", "").trim();
            } else if (line.startsWith("|  ") && !line.startsWith("| ID")) {
                // Extract GPU data
                String[] parts = line.split("\\|");
                String gpuName = parts[2].trim();
                String vram = parts[3].trim().split(" ")[0];  // just get the used VRAM
                String gpuUtil = parts[4].trim().split(" ")[0]; // just get the GPU Util %

                PrintWriter writer = writerMap.get(gpuName);
                if (writer == null) {
                    writer = new PrintWriter(new FileWriter(gpuName.replace(' ', '_') + ".csv"));
                    writer.println("Timestamp,VRAM Used,GPU Util");
                    writerMap.put(gpuName, writer);
                }
                writer.println(timestamp + "," + vram + "," + gpuUtil);
            }
        }
        reader.close();

        // Close all PrintWriter objects
        for (PrintWriter writer : writerMap.values()) {
            writer.close();
        }
    }
}

