package com.hajhouj.biosynres.cl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class CPUBioSynResolver {
	   private static List<String> readTermsFromFile(String filename) {
	        try {
	            return Files.readAllLines(Paths.get(filename));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ArrayList<>();
	        }
	    }

	    public static List<Result> resolve(String filename, String queryTerm, int topN) {
	        LevenshteinDistance ld = LevenshteinDistance.getDefaultInstance();
	        List<String> terms = readTermsFromFile(filename);

	        Map<String, Integer> termDistances = terms.parallelStream()
	            .collect(Collectors.toMap(
	                term -> term,
	                term -> ld.apply(queryTerm, term),
	                Math::min
	            ));

	        List<Result> sortedEntries = termDistances.entrySet().stream()
	            .sorted(Map.Entry.comparingByValue())
	            .limit(topN)
	            .map(entry -> new Result(terms.indexOf(entry.getKey()), entry.getKey(), entry.getValue()))
	            .collect(Collectors.toList());

	        return sortedEntries;
	    }
}
