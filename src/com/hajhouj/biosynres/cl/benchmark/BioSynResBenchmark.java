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

public class BioSynResBenchmark {

	public static void main(String[] args) throws IOException {

		String command = args[0];
		
		if (command.equalsIgnoreCase("devices")) {
			DevicesList.main(args);
		} else if (command.equalsIgnoreCase("benchmark")) {
	        String queryTerm = args[1];
	        String filename = args[2];
	        int topN = Integer.parseInt(args[3]);
	        String platform = args[4];
	        
	        List<Result> result = null;
	        
	        if (platform.equalsIgnoreCase("CPU")) {
	        	result = CPUBioSynResolver.resolve(filename, queryTerm, topN);
	        } else if (platform.equalsIgnoreCase("OPENCL")) {
	        	result = OpenCLBioSynResolver.resolve(filename, queryTerm, topN);
	        } else {
	        	System.err.println("Unknown Platform : " + platform);
	        	System.exit(-1);
	        }
	         
	        
	        for (Result entry : result) {
	            System.out.println("Term: " + entry.getTerm() + ", Distance: " + entry.getDistance());
	        }
		} else {
			System.out.println("Unknown command : " + command);
			System.exit(-1);
		}


	}

}
