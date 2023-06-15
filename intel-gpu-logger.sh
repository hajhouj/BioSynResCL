#!/bin/bash
while true; do
  date >> intel_gpu_log.txt
  intel_gpu_top -s 1 -o - | head -n 1 >> intel_gpu_log.txt
  sleep 1
done

