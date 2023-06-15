package com.hajhouj.biosynres.cl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hajhouj.fastsico.IConstants;
import com.hajhouj.fastsico.exception.OpenCLDeviceNotFoundException;
import com.hajhouj.fastsico.factory.AlgorithmFactory;
import com.hajhouj.fastsico.factory.StringSimilarityAlgorithm;

public class OpenCLBioSynResolver {
	public static List<Result> resolve(String filename, String queryTerm, int topN) {
		StringSimilarityAlgorithm editDistance = AlgorithmFactory.getAlgorithm(IConstants.EDIT_DISTANCE);
		List<Result> candidateList = new ArrayList<>();
		try {
			List<com.hajhouj.fastsico.Result> result = editDistance.calculateSimilarityInDataSet(queryTerm, filename);

			for (int i = 0; i < topN; i++) {
				int lineNumber = result.get(i).getIndex();
				int distance = new Double(result.get(i).getSimilarityScore()).intValue();
				candidateList
						.add(new Result(lineNumber, DataFileIndexer.loadIndex(filename).getLine(lineNumber), distance));
			}
			return candidateList;
		} catch (OpenCLDeviceNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Result>();
	}
}
