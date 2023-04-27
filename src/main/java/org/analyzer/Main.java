package org.analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        FastaHandler test = new FastaHandler();
        File fasta = new File("/home/daniel/IdeaProjects/FastaAnalyzer/src/main/java/org/analyzer/test.fasta");

        test.parseFasta(fasta);

        System.out.println("hallo");

    }
}