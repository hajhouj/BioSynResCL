#!/bin/bash
while true; do
  date >> amd_gpu_log.txt
  /opt/rocm/bin/rocm-smi >> amd_gpu_log.txt
  sleep 1
done

