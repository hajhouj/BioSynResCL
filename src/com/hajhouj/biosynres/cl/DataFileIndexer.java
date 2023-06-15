package com.hajhouj.biosynres.cl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class DataFileIndexer {
	private static HashMap<String, long[]> cache = new HashMap<>();
	private String filename;

	public DataFileIndexer(String filename) {
		this.filename = filename;
	}

	public static DataFileIndexer loadIndex(String filename) {
		if (cache.containsKey(filename))
			return new DataFileIndexer(filename);
		else {
			long[] linePositions;
			try {
				linePositions = Files.lines(Paths.get(filename + ".idx")).mapToLong(Long::parseLong).toArray();
				cache.put(filename, linePositions);
				return new DataFileIndexer(filename);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	public String getLine(int lineNumber) {
		long[] linePositions = cache.get(filename);
		try {
			if (lineNumber >= 0 && lineNumber < linePositions.length) {
				RandomAccessFile raf = new RandomAccessFile(filename, "r");
				raf.seek(linePositions[lineNumber]);
				String term = raf.readLine();
				raf.close();
				return term;
			} else {
				System.out.println("Line number out of range");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
