#!/bin/bash

while true
do
    # Extract the line showing overall CPU usage from the top command
    top -l 1 | grep "CPU usage" >> mac_cpu_log.txt
    sleep 1
done
