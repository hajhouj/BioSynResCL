package com.hajhouj.biosynres.cl.extratools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class EGMAggregator {
	private final static String ACRONYMS_CSV = "acronyms_meta.txt";
	private final static String ACRONYMS_LIST="acronyms.txt";

	public static void main(String[] args) {
		String[] files = new String[] { "adam/adam.txt", "berman/12000_pathology_abbreviations.txt", "obgyn/obgyn.txt",
				"umls/LRABR", "uthealth/sense_distribution_448.txt", "wikipedia/wikipedia_abr_database.csv" };

		if (args.length != 1) {
			System.out.println("Usage :");
			System.out.println("com.hajhouj.biosynres.cl.extratools.ElliotGMitchellBioABRsAggregator <SourceFolder>");
			System.out.println("<SourceFolder> : Replace with local sources folder. This folder is present is the github repo elliotgmitchell/clinical-acronym-metathesaurus");
			System.exit(-1);
		}
		String folder = args[0];

		int total = 0;
		for (String f : files) {
			File file = new File(Paths.get(folder, f).toUri());
			if (file.isFile()) {
				System.out.println("Processing " + file.getName() + " ...");
				int lines = processFile(file);
				System.out.println(lines + " line processed.");
				total += lines;
			}
		}
		System.out.println("---------");
		System.out.println(files.length + " files and " + total + " lines processed.");		
		System.out.println(
				">" + ACRONYMS_CSV + ": This file contains the extracted abbreviation and long form pairs from Elliot G. Mitchell source files. It also records the line number from the original file where the abbreviation/long form pair was found and the name of the original source file. The data in this file is structured as CSV (comma-separated values). Each line of the file corresponds to one abbreviation/long form pair, and the format is abbreviation,long form,line number,file name.\n"
						+ ">" + ACRONYMS_LIST + ": This file only contains the long forms of the abbreviations extracted from the wikipedia_abr_database.csv file. Each line in this file corresponds to the long form of one abbreviation. This is a plain text file with one entry per line.");
	}

	private static int processFile(File file) {
		switch (file.getName()) {
		case "adam.txt":
			return processAdamFile(file);
		case "12000_pathology_abbreviations.txt":
			return processPathologyFile(file);
		case "obgyn.txt":
			return processObgynFile(file);
		case "LRABR":
			return processLrabrFile(file);
		case "sense_distribution_448.txt":
			return processSenseFile(file);
		case "wikipedia_abr_database.csv":
			return processWikipediaFile(file);
		default:
			return -1;
		}

	}

	private static int processAdamFile(File file) {
		int lineNumber = 1;

		try (BufferedReader br = new BufferedReader(new FileReader(file));
				PrintWriter csvWriter = new PrintWriter(new FileWriter(ACRONYMS_CSV, true));
				PrintWriter txtWriter = new PrintWriter(new FileWriter(ACRONYMS_LIST, true))) {

			String line;

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")) { // Skip comment lines
					lineNumber++;
					continue;
				}

				String[] lineParts = line.split("\t");

				String abbreviation = lineParts[0];
				String longForm = lineParts[2].split(":")[0];

				csvWriter.println(abbreviation + "," + longForm + "," + lineNumber + "," + file.getName());
				txtWriter.println(abbreviation);
				lineNumber++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return lineNumber;
	}

	private static int processPathologyFile(File file) {
		int lineNumber = 1;

		try (BufferedReader br = new BufferedReader(new FileReader(file));
				PrintWriter csvWriter = new PrintWriter(new FileWriter(ACRONYMS_CSV, true));
				PrintWriter txtWriter = new PrintWriter(new FileWriter(ACRONYMS_LIST, true))) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(" = ");
				if (parts.length == 2) {
					String abbreviation = parts[0].trim();
					String longForm = parts[1].trim();

					csvWriter.println(String.format("%s,%s,%d,%s", abbreviation, longForm, lineNumber, file.getName()));
					txtWriter.println(abbreviation);

					lineNumber++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lineNumber;
	}

	private static int processObgynFile(File file) {
		int lineNumber = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(file));
				FileWriter acronymsSourceWriter = new FileWriter(ACRONYMS_CSV, true);
				FileWriter acronymsWriter = new FileWriter(ACRONYMS_LIST, true)) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split(" ", 2); // Split line into two parts
				if (splitLine.length >= 2) {
					String abbreviation = splitLine[0].trim();
					String longForm = splitLine[1].trim();

					// Write to acronyms_source.csv
					acronymsSourceWriter.append(
							String.join(",", abbreviation, longForm, Integer.toString(lineNumber), file.getName()));
					acronymsSourceWriter.append("\n");

					// Write to acronyms.txt
					acronymsWriter.append(abbreviation);
					acronymsWriter.append("\n");
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

	private static int processLrabrFile(File file) {
		int lineNumber = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(file));
				PrintWriter csvWriter = new PrintWriter(new FileWriter(ACRONYMS_CSV, true));
				PrintWriter txtWriter = new PrintWriter(new FileWriter(ACRONYMS_LIST, true))) {

			String line;

			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split("\\|");
				if (splitLine.length >= 5) {
					String abbreviation = splitLine[1].trim();
					String longForm = splitLine[4].trim();

					csvWriter.println(String.format("%s,%s,%d,LRABR", abbreviation, longForm, lineNumber));
					txtWriter.println(abbreviation);
				}
				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

	private static int processSenseFile(File file) {
		int lineNumber = 1;

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			List<String> csvLines = new ArrayList<>();
			List<String> acronymLines = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split("\t");
				if (splitLine.length >= 2) {
					String abbreviation = splitLine[0].trim();
					String longForm = splitLine[1].trim();
					csvLines.add(file.getName() + "," + lineNumber + "," + abbreviation + "," + longForm);
					acronymLines.add(abbreviation);
				}
				lineNumber++;
			}

			// Write to CSV file
			Path csvPath = Paths.get(ACRONYMS_CSV);
			Files.write(csvPath, csvLines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

			// Write to TXT file
			Path txtPath = Paths.get(ACRONYMS_LIST);
			Files.write(txtPath, acronymLines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

	private static int processWikipediaFile(File file) {
		int lineNumber = 0;
		String outputPath = ACRONYMS_CSV;
		String outputLongFormsPath = ACRONYMS_LIST;

		try (BufferedReader br = new BufferedReader(new FileReader(file));
				FileWriter fw = new FileWriter(outputPath, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				FileWriter fwLongForms = new FileWriter(outputLongFormsPath, true);
				BufferedWriter bwLongForms = new BufferedWriter(fwLongForms);
				PrintWriter outLongForms = new PrintWriter(bwLongForms)) {

			String line;

			while ((line = br.readLine()) != null) {
				lineNumber++;
				String[] values = line.split(",");
				if (values.length >= 2) {
					String abbreviation = values[0];
					String longForm = values[1];
					out.println(String.format("%s,%s,%d,%s", abbreviation, longForm, lineNumber, file.getName()));
					outLongForms.println(abbreviation);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

}
