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
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void dnaGC() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        assertEquals(0.6666666666666666, testhandler.entryList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.entryList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void dnaMolecularWeight() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        assertEquals(5700.759999999999, testhandler.entryList.get(0).getMolecularWeight());
        assertEquals(1173.84, testhandler.entryList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the Melting Point method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void dnaMeltingPoint() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dna.fasta", "dna");

        // For sequence with length over 14
        assertEquals(54.87777777777779, testhandler.entryList.get(0).getMeltingPoint());
        // For sequence with length under 14
        assertEquals(12.0, testhandler.entryList.get(1).getMeltingPoint());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void dnaToPeptideNetCharge() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/dnatopeptide.fasta", "dna");

        assertEquals(0.9968388513224302, testhandler.entryList.get(0).getNetCharge());
    }


    /**
     * Unittest, for testing the GC enrichment method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */


    @Test
    public void rnaGC() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/rna.fasta", "rna");

        assertEquals(0.3333333333333333, testhandler.entryList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.entryList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void rnaMolecularWeight() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/rna.fasta", "rna");

        assertEquals(1739.1999999999998, testhandler.entryList.get(0).getMolecularWeight());
        assertEquals(1126.8, testhandler.entryList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the netCharge method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void peptideNetCharge() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/peptide.fasta", "peptide");

        assertEquals(0.9968388513224302, testhandler.entryList.get(1).getNetCharge());

    }

    /**
     * Unittest, for testing the isoelectric Point method. Used a sequence with known properties.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void peptideIsoelectricPoint() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = new FastaHandler("TestFiles/peptide.fasta", "peptide");

        assertEquals(9.720580332134242, testhandler.entryList.get(1).getIsoelectricPoint());
    }

    /**
     * The Singleton is able to append a new file when called again.
     * The Class FastaHandler should remeber the input Files and should not parse doublicate files.
     *
     * @throws FileNotFoundException     if File does not exist
     * @throws WrongFastaFormatException if File is formatted not in Fasta Format
     */
    @Test
    public void singletonSetUp() throws FileNotFoundException, WrongFastaFormatException {
        FastaHandler testhandler = FastaHandler.getInstance("TestFiles/dna.fasta", "dna");
        FastaHandler.getInstance("TestFiles/rna.fasta", "rna");
        FastaHandler.getInstance("TestFiles/rna.fasta", "rna");

        assertEquals(4, testhandler.entryList.size());
    }

    /**
     * Unittest for fasta format test
     *
     * @throws FileNotFoundException     if File does not exist
     */
    @Test
    public void malformatedFasta() throws FileNotFoundException {

        // thrown need to be set to true, since the malformated. fasta is malformated and excpetion needs to be catched to change to true
        boolean thrown = false;

        // dna.fasta is formated in the right way, if it reaches the catch block, and changes this var to false, somethings wrong
        boolean doublecheck = true;

        try {
            new FastaHandler("TestFiles/malformated.fasta", "dNa");
        } catch (WrongFastaFormatException e) {
            thrown = true;
        }
        try {
            new FastaHandler("TestFiles/dna.fasta", "dNa");
        } catch (WrongFastaFormatException e) {
            doublecheck = false;
        }

        assertTrue(thrown);
        assertTrue(doublecheck);
    }


}
