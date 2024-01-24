# BioSynResCL

BioSynResCL is a command-line tool for resolving biomedical terms to their corresponding concepts. It uses a dataset of abbreviations and a medical vocabulary to perform the resolution, and the entire computation can be done on either the CPU or on a GPU using OpenCL.

This tool was designed with the aim to improve speed, scalability and performance of resolving biomedical terms especially in large-scale datasets.

# Background
This work, BioSynResCL, is the product of a research paper to be presented at[ the 6th IEEE International Conference on Computing and Artificial Intelligence: Technologies and Applications (IEEE CloudTech'23)](http://www.macc.ma/cloudtech23/). 

# How to Build

* Clone this repository.
* Navigate to the root directory of the project.
* Run <code>mvn clean package</code>.

# How to Use
## Vocabulary data

BioSynResCL uses vocabulary datasets for term resolution. The datasets can be downloaded from the following links:

* UMLS Terms vocabulary with all languages : Download from [here](http://89.40.6.5:8080/all.zip).
* UMLS Terms vocabulary for English only : Download from [here](http://89.40.6.5:8080/eng.zip).
* MeSH Terms vocabulary : Download from [here](http://89.40.6.5:8080/msh.zip).

After downloading the desired dataset, decompress its contents and use the "terms.txt" file as a parameter when running either the benchmark or the resolution tool.

## Run Benchmark

<code>java -cp "./target/libs/*" com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark benchmark "queryTerm" "filename" "topN" "platform"</code>

Where:

* **queryTerm** is the term to resolve.
* **filename** is the filename of the input file.
* **topN** is the number of top results to retrieve.
* **platform** is the platform to use (CPU or OPENCL).

## Selecting OpenCL Device

If you opt to use OpenCL as the computational platform, you will need to specify the OpenCL device ID for computation. You can determine the available devices and their IDs by running the following command:

<code>java -cp "./target/libs/*" com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark devices</code>

The output should look something like this:

Use the desired device by adding its query as a JVM parameter using -Duse-device, for example if we want to use Intel(R) UHD Graphics 630 from the result above, we should use -Duse-device=0.1 :

<code>DEVICE QUERY | DEVICE NAME
-------------+-------------
0.0          | Intel(R) Core(TM) i7-8750H CPU @ 2.20GHz
0.1          | Intel(R) UHD Graphics 630
0.2          | AMD Radeon Pro 555X Compute Engine</code>


To use a specific device, include its query ID as a JVM parameter using -Duse-device. For example, to use the Intel(R) UHD Graphics 630 from the output above, your command should look like this:

`java -Duse-device=0.1 -cp "./target/libs/*" com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark benchmark "Autism disorder" "vocabulary/all/terms.txt" 10 OPENCL`

## Run Resolution Tool

<code>java -cp "./target/libs/*" com.hajhouj.biosynres.cl.resolve.BioSynResolutionTool resolve "queryTerm" "acronymsFileName" "vocabularyFileName" "topN" "platform"</code>

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



# Contributing

Contributions are very welcome. Feel free to fork this project, make your changes, and submit a pull request. If you have found a bug or have a feature request, please open an issue.

# Citation
If you find the code in this repository useful in your research, we kindly request that you cite our work. Here is the BibTeX entry for our paper:

```
@inproceedings{hajhouj2023enhancing,
  title={Enhancing Syntactic Resolution in Biomedical Data Processing with OpenCL: A Use Case Study},
  author={Hajhouj, Mohammed and Abik, Mounia and Zarnoufi, Randa},
  booktitle={2023 IEEE 6th International Conference on Cloud Computing and Artificial Intelligence: Technologies and Applications (CloudTech)},
  pages={1--8},
  year={2023},
  organization={IEEE}
}
```

# License

BioSynResCL is released under GNU General Public License v3.0 .

# UMLS Data Usage and Compliance Information

This project utilizes concept names derived from the UMLS Metathesaurus, a comprehensive database managed by the National Library of Medicine (NLM). The inclusion of UMLS data is specifically for benchmarking purposes in the context of accelerated syntactic resolution using OpenCL within this open-source project.

**Please Note**: The usage of UMLS data is governed by the terms of the UMLS Metathesaurus License Agreement. Users of this repository are responsible for ensuring their use of the UMLS data complies with these terms. It is important to acknowledge that redistribution of the UMLS data, even in part, may be subject to restrictions as per the license agreement.

For full details on the licensing terms and conditions, please refer to the [UMLS Metathesaurus License Agreement](https://www.nlm.nih.gov/research/umls/knowledge_sources/metathesaurus/release/license_agreement.html).

We encourage all users to review the license terms and adhere to them strictly to ensure compliance with copyright laws and regulations.

