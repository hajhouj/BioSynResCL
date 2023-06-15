#!/bin/bash
while true; do
    echo $(date) >> cpu_log.txt
    echo "CPU usage:" $(mpstat | grep -A 5 "%idle" | tail -n 1 | awk -F " " '{print 100 -  $ 12}')% >> cpu_log.txt
    echo "Memory usage:" $(free | grep Mem | awk '{print $3/$2 * 100.0}')% >> cpu_log.txt
    sleep 1
done

