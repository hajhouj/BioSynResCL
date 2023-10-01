package com.hajhouj.biosynres.cl.benchmark;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hajhouj.biosynres.cl.CPUBioSynResolver;
import com.hajhouj.biosynres.cl.Result;
import com.hajhouj.biosynres.cl.OpenCLBioSynResolver;
import com.hajhouj.fastsico.tools.DevicesList;

/**
 * A benchmarking class for BioSynResCL, a biomedical syntactic resolution tool.
 */
public class BioSynResBenchmark {

    /**
     * Entry point of the benchmarking application.
     *
     * @param args Command line arguments.
     *             The first argument should be the command to execute.
     *             If the command is "devices", it lists the available OpenCL devices.
     *             If the command is "benchmark", it performs benchmarking on the specified platform.
     *             The second argument is the query term.
     *             The third argument is the filename of the input file.
     *             The fourth argument is the number of top results to retrieve.
     *             The fifth argument is the platform to use (CPU or OPENCL).
     * @throws IOException If an I/O error occurs while reading the input file.
     */
    public static void main(String[] args) throws IOException {

        String command = args[0];

        if (command.equalsIgnoreCase("devices")) {
            // List available OpenCL devices
            DevicesList.main(args);
        } else if (command.equalsIgnoreCase("benchmark")) {
            // Perform benchmarking

            // Read command line arguments
            String queryTerm = args[1];
            String filename = args[2];
            int topN = Integer.parseInt(args[3]);
            String platform = args[4];

            List<Result> result = null;

            long startTime = System.nanoTime();  // Start the timer
            if (platform.equalsIgnoreCase("CPU")) {
                // Resolve using CPU platform
                result = CPUBioSynResolver.resolve(filename, queryTerm, topN);
            } else if (platform.equalsIgnoreCase("OPENCL")) {
                // Resolve using OpenCL platform
                result = OpenCLBioSynResolver.resolve(filename, queryTerm, topN);
            } else {
                System.err.println("Unknown Platform : " + platform);
                System.exit(-1);
            }

            long endTime = System.nanoTime();  // Stop the timer

            // Calculate and display the elapsed time in milliseconds
            double elapsedTimeMillis = (endTime - startTime) / 1_000_000_000.0;
            System.out.println("Elapsed time: " + elapsedTimeMillis + " seconds");

            // Print the results
            for (Result entry : result) {
                System.out.println("Term: " + entry.getTerm() + ", Distance: " + entry.getDistance());
            }
        } else {
            System.out.println("Unknown command : " + command);
            System.exit(-1);
        }
    }
}
