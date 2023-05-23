package org.analyzer;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class FastaAnalyzerTest {

    @Test
    public void setUpTest() throws FileNotFoundException {
        FastaHandler testHandler = new FastaHandler();
        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/dna.fasta"), "dna");
        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/rna.fasta"), "rna");
        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/peptide.fasta"), "peptide");
        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/dna.fasta"), "ambiguous");

        assertEquals(7, testHandler.entryList.size());

    }


}
