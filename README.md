# BioSynResCL

BioSynResCL is a command-line tool for resolving biomedical terms to their corresponding concepts. It uses a dataset of abbreviations and a medical vocabulary to perform the resolution, and the entire computation can be done on either the CPU or on a GPU using OpenCL.

This tool was designed with the aim to improve speed, scalability and performance of resolving biomedical terms especially in large-scale datasets.

# How to Use
## Vocabulary data

Vocabulary Data

BioSynResCL uses vocabulary datasets for term resolution. The datasets can be downloaded from the following links:

* UMLS Terms vocabulary with all languages : Download from [here](http://89.40.6.5/all.zip).
* UMLS Terms vocabulary for English only : Download from [here](http://89.40.6.5/eng.zip).
* MeSH Terms vocabulary : Download from [here](http://89.40.6.5/msh.zip).

After downloading the desired dataset, decompress its contents and use the "terms.txt" file as a parameter when running either the benchmark or the resolution tool.
## Run Benchmark

<code>java -cp target/biosynres-cl-1.0.0.jar com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark benchmark "queryTerm" "filename" "topN" "platform"</code>

Where:

* **queryTerm** is the term to resolve.
* **filename** is the filename of the input file.
* **topN** is the number of top results to retrieve.
* **platform** is the platform to use (CPU or OPENCL).

## Run Resolution Tool

<code>java -cp target/biosynres-cl-1.0.0.jar com.hajhouj.biosynres.cl.resolve.BioSynResolutionTool resolve "queryTerm" "acronymsFileName" "vocabularyFileName" "topN" "platform"</code>

Where:

* **queryTerm** is the term to resolve.
* **acronymsFileName** is the filename of the input file containing abbreviations.
* **vocabularyFileName** is the filename of the input file containing vocabulary.
* **topN** is the number of top results to retrieve.
* **platform** is the platform to use (CPU or OPENCL).

# Dependencies

To run BioSynResCL, you will need:

* Java 8 or later
* An OpenCL compatible GPU (for OpenCL platform)

# Building from Source

* Clone this repository.
* Navigate to the root directory of the project.
* Run <code>mvn clean package</code>.

# Contributing

Contributions are very welcome. Feel free to fork this project, make your changes, and submit a pull request. If you have found a bug or have a feature request, please open an issue.

# License

BioSynResCL is released under GNU General Public License v3.0 .

