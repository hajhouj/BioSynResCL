package com.hajhouj.biosynres.cl.resolve;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hajhouj.biosynres.cl.CPUBioSynResolver;
import com.hajhouj.biosynres.cl.DataFileIndexer;
import com.hajhouj.biosynres.cl.OpenCLBioSynResolver;
import com.hajhouj.biosynres.cl.Result;
import com.hajhouj.fastsico.tools.DevicesList;

public class BioSynResolutionTool {
	private static int SIMILARITY_THRESHOLD = 3;
	private static HashMap<String, String> cache = new HashMap<>();

	public static void main(String[] args) {
		String command = args[0];
		
		if (command.equalsIgnoreCase("devices")) {
			DevicesList.main(args);
		} else if (command.equalsIgnoreCase("resolve")) {
	        int topN = Integer.parseInt(args[4]);
	        String platform = args[5];
	        
	        //Let E denote the entity mention that we aim to disambiguate 
	        String queryTerm = args[1];
	        //and let D represent the abbreviations dataset. 
	        String acronymsFileName = args[2];
	        //We look for E in D. 
	        List<Result> resultAcronyms = resolve(platform, acronymsFileName, queryTerm, topN);
	        Set<String> termCandidates = new HashSet<String>();
	        //If  Eexists in D , we gain the set A = {a1, a2, ..., an} of terms associated with this abbreviation, 
	        for (Result r : resultAcronyms) {
	        	if (r.getDistance() == 0) {
	        		String record = DataFileIndexer.loadIndex(acronymsFileName.replace(".txt", "_meta.txt")).getLine(r.getIndex());
	        		String[] data = record.split(",");
	        		
	        		termCandidates.add(data[1]);
	        	}
	        }
	        //otherwise A={E}. 
	       if (termCandidates.size() ==0) termCandidates.add(queryTerm);

	        //Formally, let V be the medical vocabulary, and d(a, v)  signify the edit distance between the term a  from A  and the term v from V. 
	        String vocabularyFileName  = args[3];
	        
	        //For each a  A , find v  V that minimizes d(a,v). If a1,a2A such that |d(a1,v)-d(a2,v)| is very small, we retain {a1,a2} for subsequent semantic resolution.
	        Set<String> conceptCandidates = new HashSet<String>();
	        for (String a: termCandidates) {
	        	List<Result> resultVocabulary = resolve(platform, vocabularyFileName, a, topN);
	        	for (Result r: resultVocabulary) {
	        		String record = DataFileIndexer.loadIndex(vocabularyFileName.replace(".txt", "_meta.txt")).getLine(r.getIndex());
	        		String[] data = record.split(",");
	        		if (r.getDistance() <= SIMILARITY_THRESHOLD) {
	        			conceptCandidates.add(data[4]); // change index to 0 to get CUI instead of MesH id 
	        			cache.put(data[4], r.getTerm());
	        		}
	        	}
	        }
	        //Hence, we derive the set of candidates C={c1,c2,,cn}  where ci is the term from V which has the minimum edit distance to a term from A. The identifier of each ci is subsequently preserved for upcoming utilization.
	       for (String c: conceptCandidates) {
	    	   System.out.println(c + ":" + cache.get(c));
	       }
		} else {
			System.out.println("Unknown command : " + command);
			System.exit(-1);
		}

	}
	
	private static List<Result> resolve (String platform, String filename, String queryTerm, int topN) {
        List<Result> result = null;
        
        if (platform.equalsIgnoreCase("CPU")) {
        	result = CPUBioSynResolver.resolve(filename, queryTerm, topN);
        } else if (platform.equalsIgnoreCase("OPENCL")) {
        	result = OpenCLBioSynResolver.resolve(filename, queryTerm, topN);
        } else {
        	System.err.println("Unknown Platform : " + platform);
        	System.exit(-1);
        }
        
        return result;
	}

}
