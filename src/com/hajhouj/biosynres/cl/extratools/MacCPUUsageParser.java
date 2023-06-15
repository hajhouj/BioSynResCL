package com.hajhouj.biosynres.cl.extratools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MacCPUUsageParser {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("mac_cpu_log.txt"));
        PrintWriter writer = new PrintWriter(new FileWriter("cpu_usage.csv"));

        writer.println("Timestamp,User,System,Idle");

        String line;
        while ((line = reader.readLine()) != null) {
            // Sample line: "CPU usage: 3.28% user, 7.14% sys, 89.57% idle"
            String[] parts = line.split(":")[1].trim().split(",");
            String user = parts[0].split("%")[0].trim();
            String sys = parts[1].split("%")[0].trim();
            String idle = parts[2].split("%")[0].trim();
            String timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());

            writer.println(timestamp + "," + user + "," + sys + "," + idle);
        }
        reader.close();
        writer.close();
    }
}
