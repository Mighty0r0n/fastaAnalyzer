package org.analyzer;

import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * First JUnit test, still a bit overwhelming because there are a lot of Methods to use.
 * Here Objects get created and calculations get compared to hand calculated metadata with help of
 * the provided Calculators from the Script.
 * This Test structure don't test all logics, since I already found Errors not get caught here.
 * But usable for test the calculations.
 */
public class FastaAnalyzerTest {

    /**
     * Unittest, for testing the GC enrichment method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaGC() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        assertEquals(0.5258064516129032, testhandler.entryList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.entryList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaMolecularWeight() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        assertEquals(191656.03, testhandler.entryList.get(0).getMolecularWeight());
        assertEquals(1173.84, testhandler.entryList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the Melting Point method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaMeltingPoint() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        // For sequence with length over 14
        assertEquals(85.37354838709678, testhandler.entryList.get(0).getMeltingPoint());
        // For sequence with length under 14
        assertEquals(12.0, testhandler.entryList.get(1).getMeltingPoint());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaToPeptideNetCharge() throws FileNotFoundException {

        // can translate DNA/RNA to peptide and make peptide calculations from there
        FastaHandler testhandler = new FastaHandler("TestFiles/dnatopeptide.fasta", "dna");
        FastaHandler testhandler2 = new FastaHandler("TestFiles/rnatopeptide.fasta", "rna");

        assertEquals(0.9968388513224302, testhandler.entryList.get(0).getNetCharge());
        assertEquals(0.9968388513224302, testhandler2.entryList.get(0).getNetCharge());
    }


    /**
     * Unittest, for testing the GC enrichment method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */


    @Test
    void rnaGC() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/rna.fasta", "rna");

        assertEquals(0.3333333333333333, testhandler.entryList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.entryList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void rnaMolecularWeight() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/rna.fasta", "rna");

        assertEquals(1739.1999999999998, testhandler.entryList.get(0).getMolecularWeight());
        assertEquals(1126.8, testhandler.entryList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the netCharge method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void peptideNetCharge() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/peptide.fasta", "peptide");

        assertEquals(-1.4247671868363128, testhandler.entryList.get(0).getNetCharge());

    }

    /**
     * Unittest, for testing the isoelectric Point method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void peptideIsoelectricPoint() throws FileNotFoundException {
        FastaHandler testhandler = new FastaHandler("TestFiles/peptide.fasta", "peptide");

        assertEquals(6.333508902276007, testhandler.entryList.get(0).getIsoelectricPoint());
    }

    /**
     * The Singleton is able to append a new file when called again.
     * The Class FastaHandler should remeber the input Files and should not parse douplicate files.
     *
     * @throws FileNotFoundException         if File does not exist
     * @throws MalformatedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void singletonSetUp() throws FileNotFoundException {
        FastaHandler testhandler = FastaHandler.getInstance("TestFiles/dna.fasta", "dna");
        FastaHandler.getInstance("TestFiles/dna.fasta", "dna");
        FastaHandler.getInstance("TestFiles/dna.fasta");
        FastaHandler.getInstance("TestFiles/dna.fasta", "dna");

        /*
         * For the 4 times the input file is loaded, only 2 times it should calculate objects from the input.
         * Since the programm remembers the input given with a proper sequence type, but dont remebers the input when given none
         * sequence type. So 4 Objects are created for 2 entrys in the input file
         */
        assertEquals(4, testhandler.entryList.size());


        /*
         * The first time the input gets read, the programm calculates metadata because the sequence type is provided
         * The second time objects are getting created with no specified sequence type, so no calculations are done
         */
        assertEquals(0.5, testhandler.entryList.get(1).getGcEnrichment());
        assertEquals(">seqID 1337", testhandler.entryList.get(1).getSeqID());

        assertEquals(0.0, testhandler.entryList.get(3).getGcEnrichment());
        assertEquals(">seqID 1337", testhandler.entryList.get(3).getSeqID());
    }

    /**
     * Unittest for fasta format test
     *
     * @throws FileNotFoundException if File does not exist
     */
    @Test
    void malformatedFasta() throws FileNotFoundException {

        // thrown need to be set to true, since the malformated. fasta is malformated and excpetion needs to be catched to change to true
        boolean hasDoubleHeader = false;
        boolean hasMissingHeaderStart = false;

        // dna.fasta is formated in the right way, if it reaches the catch block, and changes this var to false, somethings wrong
        boolean doublecheck = true;

        try {
            new FastaHandler("TestFiles/malformated.fasta", "dNa");
        } catch (MalformatedFastaFileException mffe) {
            hasDoubleHeader = true;
        }

        try {
            new FastaHandler("TestFiles/malformated2.fasta", "dNa");
        } catch (MalformatedFastaFileException mffe) {
            hasMissingHeaderStart = true;
        }


        try {
            new FastaHandler("TestFiles/dna.fasta", "dNa");
        } catch (MalformatedFastaFileException mffe) {
            doublecheck = false;
        }

        assertTrue(hasDoubleHeader);
        assertTrue(hasMissingHeaderStart);
        assertTrue(doublecheck);

    }

    /**
     * Unittest for checking the sequence type of the input file.
     *
     * @throws FileNotFoundException if input file is not found
     */
    @Test
    void testWrongSequenceType() throws FileNotFoundException {

        boolean hasWrongSequenceType = false;

        try {
            new FastaHandler("TestFiles/dna.fasta", "peptide");
        } catch (WrongSequenceTypeException wste) {
            hasWrongSequenceType = true;
        }
        assertTrue(hasWrongSequenceType);

    }


}
