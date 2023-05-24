package org.analyzer;

import java.io.File;
import java.io.FileNotFoundException;

public class TestMain {
    public static void main(String[] args) throws FileNotFoundException {
        FastaHandler testhandler = FastaHandler.getInstance(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/test2.fasta"), "Peptide");

        int testname = testhandler.entryList.get(1).hashCode();

        for (int i = 0; i<testhandler.entryList.size(); i++){
            if (testhandler.entryList.get(i).hashCode() == testname){
                System.out.println(testhandler.entryList.get(i).getSeqID());
            }
        }
        for (FastaEntry entry: testhandler.entryList){
            if (entry.hashCode() == testname){

            }
        }

        System.out.println(testhandler.entryList.get(0).hashCode());
        System.out.println(testhandler.entryList.get(1).hashCode());
    }
}
