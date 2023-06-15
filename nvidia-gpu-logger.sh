#!/bin/bash

while true; do
    nvidia-smi >> nv_gpu_log.txt
    sleep 1  # Sleep for 1 second before running the command again
done

