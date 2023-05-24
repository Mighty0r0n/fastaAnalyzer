//package org.analyzer;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FastaAnalyzerTest {
//
//    public FastaHandler generateObject() throws FileNotFoundException {
//        FastaHandler testHandler = new FastaHandler();
//        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/dna.fasta"), "dna");
//        return testHandler;
//    }
//
//    @Test
//    public void setUpTest() throws FileNotFoundException {
//        FastaHandler testHandler = new FastaHandler();
//        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/dna.fasta"), "dna");
//        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/rna.fasta"), "rna");
//        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/peptide.fasta"), "peptide");
//        testHandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/dna.fasta"), "ambiguous");
//
//        assertEquals(5, testHandler.entryList.size());
//    }
//
//    @Test
//    public void testCalcsDNA() throws FileNotFoundException {
//        FastaHandler testhandler = generateObject();
//        SequenceType testseq = SequenceType.DNA;
//        testhandler.entryList.get(0).setGC(testseq);
//        testhandler.entryList.get(0).setMeltingPoint(testseq);
//        testhandler.entryList.get(0).setMolecularWeight(testseq);
//        double gc = testhandler.entryList.get(0).getGcEnrichment();
//        double mweight = testhandler.entryList.get(0).getMolecularWeight();
//        double melt = testhandler.entryList.get(0).getMeltingPoint();
//        assertEquals(0.5454545454545454, gc);
//        assertEquals(3412.2899999999995, mweight);
//        assertEquals(34, melt);
//    }
//
//    @Test
//    public void testCalcsPeptide() throws FileNotFoundException {
//        FastaHandler testhandler = new FastaHandler();
//        testhandler.parseFasta(new File("/home/daniel/IdeaProjects/fastaAnalyzer/TestFiles/peptide.fasta"), "peptide");
//
//        double net = testhandler.entryList.get(0).getNetCharge();
//        double iso = testhandler.entryList.get(0).getIsoelectricPoint();
//
//        assertEquals(0.9968388513224302, net);
//        assertEquals(9.751287691956659, iso);
//
//    }
//
//
//
//
//}
