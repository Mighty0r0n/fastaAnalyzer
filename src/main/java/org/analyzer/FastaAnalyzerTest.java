package org.analyzer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FastaAnalyzerTest {

    FastaHandler getInfo() throws FileNotFoundException {
        File inputfile = new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/buildtest.fasta");
        SequenceType testType = SequenceType.valueOf("DNA");
        FastaHandler testHandler = new FastaHandler(testType);
        testHandler.parseFasta(inputfile);
        return testHandler;
    }
    @Test
    void buildClassTest() throws FileNotFoundException {
    File inputfile = new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/buildtest.fasta");
//    SequenceType testType = SequenceType.valueOf("DNA");
//    FastaHandler testHandler = FastaHandler.getInstance(testType);
//    testHandler.parseFasta(inputfile);
    FastaHandler testHandler = getInfo();
    Scanner testScanner = new Scanner(inputfile);
    int objectCounter = 0;
    String[] testarray = new String[2];
    String testline = "";
    while (testScanner.hasNext()){
        String line = testScanner.nextLine();

        if (line.startsWith(">")) {
            assertEquals(line, testHandler.entryList.get(objectCounter).getSeqID());

            if (objectCounter != 0){
                testarray[objectCounter-1] = testline;
                testline = "";
            }
            objectCounter++;
        } else if (line.startsWith(";")) {

        } else {
            testline += line;
            if (objectCounter == 2) {
                testarray[objectCounter-1] = testline;
            }
        }
    }
    assertEquals(objectCounter, testHandler.entryList.size());


    for (int i = 0; i< testarray.length;i++) {
        assertEquals(testarray[i], testHandler.entryList.get(i).getSequence());
        assertEquals(testarray[i].length(), testHandler.entryList.get(i).getSequenceLength());
    }



}
    @Test
    void testGC() throws FileNotFoundException {
        FastaHandler testHandler = getInfo();
        testHandler.entryList.get(0).calcGC(SequenceType.DNA);
        assertEquals(0.4, testHandler.entryList.get(0).getGcEnrichment());

    }

    @Test
    void testMolecularWeight() throws FileNotFoundException {
        FastaHandler testHandler = getInfo();
        testHandler.entryList.get(0).calcMolecularWeight(SequenceType.DNA);
        assertEquals(3018.04, testHandler.entryList.get(0).getMolecularWeight());

    }

    @Test // Recheck!
    void testMeltingPoint() throws FileNotFoundException {
        FastaHandler testHandler = getInfo();
        testHandler.entryList.get(0).calcMeltingPoint(SequenceType.DNA);
        assertEquals(22, testHandler.entryList.get(0).getMeltingPoint());
    }

}
