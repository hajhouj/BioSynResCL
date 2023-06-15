#!/bin/bash

# Infinite loop
while true; do

    echo "Enter acronym (default: SARS):"
    read acronym
    if [ -z "$acronym" ]; then
        acronym="SARS"
    fi

    echo "Enter query (default: SARS Covid):"
    read query
    if [ -z "$query" ]; then
        query="SARS Covid"
    fi

    echo "Enter path to acronyms file (default: acronyms.txt):"
    read acronyms_file
    if [ -z "$acronyms_file" ]; then
        acronyms_file="acronyms.txt"
    fi

    echo "Enter path to terms file (default: terms.txt):"
    read terms_file
    if [ -z "$terms_file" ]; then
        terms_file="terms.txt"
    fi

    echo "Select platform (1. CPU (default), 2. OpenCL):"
    read platform_option
    if [ -z "$platform_option" ] || [ "$platform_option" == "1" ]; then
        platform="CPU"
    elif [ "$platform_option" == "2" ]; then
        platform="OPENCL"
        echo "List of available OpenCL Devices"
        java -cp "target/lib/*" com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark devices
        echo "Enter device name:"
        read device_name
    else
        echo "Invalid option, defaulting to CPU"
        platform="CPU"
    fi

    echo "Select logger script (1. amd-gpu-logger, 2. cpu-logger, 3. intel-gpu-logger, 4. nvidia-gpu-logger, 5. mac-gpu-logger, 6. mac-cpu-logger):"
    read logger_option
    case $logger_option in
        1) logger_script="./amd-gpu-logger.sh";;
        2) logger_script="./cpu-logger.sh";;
        3) logger_script="./intel-gpu-logger.sh";;
        4) logger_script="./nvidia-gpu-logger.sh";;
        5) logger_script="./mac-gpu-logger.sh";;
        6) logger_script="./mac-cpu-logger.sh";;
        *) echo "Invalid option, skipping logging"; logger_script="";;
    esac

    # Run logger script in background, if one was chosen
    if [ -n "$logger_script" ]; then
        bash $logger_script &
        logger_pid=$!
    fi

    echo "Find 10 most similar acronyms based on $platform edit distance implementation."
    time java -cp "target/lib/*" -Duse-device=$device_name -Dfastsico.ed.item.size=32 com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark benchmark "$acronym" "$acronyms_file" 10 "$platform"
    
    echo "Find 10 most similar terms based on $platform edit distance implementation."
    time java -cp "target/lib/*" -Duse-device=$device_name -Dfastsico.ed.item.size=64 com.hajhouj.biosynres.cl.benchmark.BioSynResBenchmark benchmark "$query" "$terms_file" 10 "$platform"

    # Kill the logger script after the benchmarks have run
    if [ -n "$logger_script" ]; then
        kill $logger_pid
    fi

    echo "Continue? [y/n]"
    read continue_option
    if [ "$continue_option" != "y" ]; then
        break
    fi
done

