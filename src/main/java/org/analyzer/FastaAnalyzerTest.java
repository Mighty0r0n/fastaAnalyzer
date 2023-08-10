package org.analyzer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the FastaHandler.
 * Calculations are tested with small and long sequences
 * Calculations for translated DNA and RNA sequences are tested with small and long sequences
 * Exceptions are tested, with testfiles that forces wrong format behaviors, these are provided in the TestFiles Folder
 * Extrem cases aren't tested, since the User can't influence any calculations from the outside and the File that
 * gets calculated is tested for proper formation and sequence arrangement. If nonsense is provided, the sequences
 * are set to ambigous, so the programm wont stop mit calculations.
 * Threading is tested also, by accessing multiple objects and check for correct calculations. The order in which they
 * calculated is irrelevant, but should correspond to the unique HeaderID.
 *
 */
public class FastaAnalyzerTest {

    /**
     * Unittest, for testing the GC enrichment method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaGC() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/dna.fasta", "dna");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(0.5258064516129032, testhandler.fastaObjectList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.fastaObjectList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaMolecularWeight() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/dna.fasta", "dna");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(191656.03, testhandler.fastaObjectList.get(0).getMolecularWeight());
        assertEquals(1173.84, testhandler.fastaObjectList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the Melting Point method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaMeltingPoint() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/dna.fasta", "dna");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        // For sequence with length over 14
        assertEquals(85.37354838709678, testhandler.fastaObjectList.get(0).getMeltingPoint());
        // For sequence with length under 14
        assertEquals(12.0, testhandler.fastaObjectList.get(1).getMeltingPoint());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void dnaToPeptideNetCharge() throws WrongSequenceTypeException, MalformattedFastaFileException {

        // can translate DNA/RNA to peptide and make peptide calculations from there
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.numberThreads = 1;


        testhandler.generateFastaHandlerObject("TestFiles/dnatopeptide.fasta", "dna");
        testhandler.processFastaEntries();
        assertEquals(0.9968388513224302, testhandler.fastaObjectList.get(0).getNetCharge());

        testhandler.generateFastaHandlerObject("TestFiles/rnatopeptide.fasta", "rna");
        testhandler.processFastaEntries();
        assertEquals(0.9968388513224302, testhandler.fastaObjectList.get(0).getNetCharge());
    }


    /**
     * Unittest, for testing the GC enrichment method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */


    @Test
    void rnaGC() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/rna.fasta", "rna");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(0.3333333333333333, testhandler.fastaObjectList.get(0).getGcEnrichment());
        assertEquals(0.5, testhandler.fastaObjectList.get(1).getGcEnrichment());
    }

    /**
     * Unittest, for testing the molecular weight method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void rnaMolecularWeight() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/rna.fasta", "rna");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(1739.1999999999998, testhandler.fastaObjectList.get(0).getMolecularWeight());
        assertEquals(1126.8, testhandler.fastaObjectList.get(1).getMolecularWeight());
    }

    /**
     * Unittest, for testing the netCharge method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void peptideNetCharge() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/peptide.fasta", "peptide");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(-1.4247671868363128, testhandler.fastaObjectList.get(0).getNetCharge());

    }

    /**
     * Unittest, for testing the isoelectric Point method. Used a sequence with known properties.
     *
     * @throws MalformattedFastaFileException if File is formatted not in Fasta Format
     */
    @Test
    void peptideIsoelectricPoint() throws WrongSequenceTypeException, MalformattedFastaFileException {
        FastaHandler testhandler = FastaHandler.getInstance();
        testhandler.generateFastaHandlerObject("TestFiles/peptide.fasta", "peptide");
        testhandler.numberThreads = 1;
        testhandler.processFastaEntries();

        assertEquals(6.333508902276007, testhandler.fastaObjectList.get(0).getIsoelectricPoint());
    }

    /**
     * Unittest for fasta format test
     */
    @Test
    void malformatedFasta() throws WrongSequenceTypeException {

        // thrown need to be set to true, since the malformated. fasta is malformated and excpetion needs to be catched to change to true
        boolean hasDoubleHeader = false;
        boolean hasMissingHeaderStart = false;

        // dna.fasta is formated in the right way, if it reaches the catch block, and changes this var to false, somethings wrong
        boolean doublecheck = true;

        try {
            FastaHandler testhandler = FastaHandler.getInstance();
            testhandler.generateFastaHandlerObject("TestFiles/malformated.fasta", "dNa");

        } catch (MalformattedFastaFileException mffe) {
            hasDoubleHeader = true;
        }

        try {
            FastaHandler testhandler = FastaHandler.getInstance();
            testhandler.generateFastaHandlerObject("TestFiles/malformated2.fasta", "dNa");

        } catch (MalformattedFastaFileException mffe) {
            hasMissingHeaderStart = true;
        }


        try {
            FastaHandler testhandler = FastaHandler.getInstance();
            testhandler.generateFastaHandlerObject("TestFiles/dna.fasta", "dNa");

        } catch (MalformattedFastaFileException mffe) {
            doublecheck = false;
        }

        assertTrue(hasDoubleHeader);
        assertTrue(hasMissingHeaderStart);
        assertTrue(doublecheck);

    }

    /**
     * Unittest for checking the sequence type of the input file.
     *
     */
    @Test
    void testWrongSequenceType() throws MalformattedFastaFileException {

        boolean hasWrongSequenceType = false;

        try {
            FastaHandler testhandler = FastaHandler.getInstance();
            testhandler.generateFastaHandlerObject("TestFiles/dna.fasta", "peptide");

        } catch (WrongSequenceTypeException wste) {
            hasWrongSequenceType = true;
        }
        assertTrue(hasWrongSequenceType);

    }
}
