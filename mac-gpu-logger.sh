#!/bin/bash

# Create an infinite loop that prints GPU usage
while true
do
    # Call the SwiftyGPU and append to log file
    /usr/local/bin/swifty-gpu >> gpu_log.txt
    # Pause for a second
    sleep 1
done
