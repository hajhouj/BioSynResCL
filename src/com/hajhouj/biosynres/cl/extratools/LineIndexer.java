package com.hajhouj.biosynres.cl.extratools;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class LineIndexer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(args[0], "r");
        PrintWriter pw = new PrintWriter(new FileWriter(args[0] + ".idx"));

        long pos = raf.getFilePointer();
        pw.println(pos); // write the position of the first line

        while (raf.readLine() != null) {
            pos = raf.getFilePointer();
            pw.println(pos);
        }

        pw.close();
        raf.close();
    }
}