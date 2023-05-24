package org.analyzer;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;

/**
 * Public Class which can be called public. Methods are hidden inside the Constructor. It's possible to
 * append more Objects to this Object via the wrapper Function addFastaEntry()
 */
public class FastaHandler {
    private static FastaHandler instance;
    LinkedList<FastaEntry> entryList = new LinkedList<>();

    public FastaHandler(File fastaFile, String seqType) throws FileNotFoundException {
        this.parseFasta(fastaFile, seqType);
    }

    /**
     * Invoke for singleton
     *
     * @return instance of the class
     */
    public static FastaHandler getInstance(File fastaFile, String seqType) throws FileNotFoundException {
        if (instance == null) {
            instance = new FastaHandler(fastaFile, seqType);
        }
        return instance;
    }

    private static SequenceType getSequenceType(String type) {
        SequenceType seqType = SequenceType.AMBIGUOUS;

        // Creating the sequenceType here enables multifile support.
        try {
            if (type != null) {
                seqType = SequenceType.valueOf(type.toUpperCase());
            } else {
                System.err.println("""

                        No Sequence Type provided. No immediate action required.
                        FastaEntry object are still filled with the seqID the Sequence and translated Sequence(If DNA or RNA),
                        but no further metadata analysis is available from here.\s
                        Please consider to rerun the Program and submit the Sequence Type of the Fasta Sequences.""");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("""
                    Valid Sequence types:
                    -DNA
                    -RNA
                    -PEPTIDE
                    -AMBIGUOUS
                                        
                    If set ambiguous only the objects with the sequences are created. No metadata analysis available.
                    """);
            e.printStackTrace();
        }
        return seqType;
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters.
     *
     * @param fasta File to analyze
     * @param type  Type of the given FastaFile
     * @throws FileNotFoundException If an invalid FilePath is given as Input an exception is thrown.
     */
    public void addFastaEntrys(File fasta, String type) throws FileNotFoundException {
        this.parseFasta(fasta, type);
    }

    /**
     * This Method reads the input file and fill all necessary Objects with the information's needed, for further
     * calculations.
     *
     * @param fasta The chosen fasta input file
     * @throws FileNotFoundException Path of the input file is incorrect
     */
    private void parseFasta(File fasta, String type) throws FileNotFoundException {

        SequenceType seqType = getSequenceType(type);
        Scanner fastaReader = new Scanner(fasta);
        StringBuilder sequenceHandler = new StringBuilder();

        int headerCounter = -1;

        // main parsing logic. Object is creating when header line is found and is filled line by line with
        // all the needed information here
        for (; ; ) {
            try {
                String fastaLine = fastaReader.nextLine();
                if (fastaLine.startsWith(">")) {
                    // tmp object is created here and saved in entryList.
                    FastaEntry tmpEntry = new FastaEntry(fastaLine);
                    this.entryList.add(tmpEntry);

                    if (headerCounter != -1) {
                        // For getting the forelast entry in the entryList because this logic appends the sequence of the
                        // forelast when a NEW Object is found.
                        this.entryList.get(this.entryList.size() - 2).settingSequenceProperties(sequenceHandler.toString(), seqType);
                    }
                    sequenceHandler = new StringBuilder();
                    headerCounter++;
                } else if (fastaLine.startsWith(";")) {
                    System.out.println("Fasta contains commentlines, this parser is ignoring them.");
                } else {
                    sequenceHandler.append(fastaLine);

                }
            } catch (NoSuchElementException e) {
                // Needed for adding last entry information to the object with above logic. Since every Information
                // is being added when a > is found in lines. The last entry ends with a Sequence, hence no information
                // is added for this entry.
                this.entryList.get(this.entryList.size() - 1).settingSequenceProperties(sequenceHandler.toString(), seqType);
                fastaReader.close();
                return;
            }

        }
    }

    /**
     * Method for generating the output Files. Calculations are done from other classes and are gathered here.
     * 0 values are sorted out
     * TO-DO -> Implement all metadata and generate new FastaFile for DNA/RNA sequence Types
     *
     * @param outputPath specifies the path where to place the outfile
     */
    public void generateOutputFiles(String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (FastaEntry entry : this.entryList) {
                writer.write("Sequence ID: " + entry.getSeqID());
                writer.newLine();
                writer.write("Sequence Length: " + entry.getSequenceLength() + ";");
                if (entry.getMolecularWeight() != 0.0) {
                    writer.write("Molecular Weight: " + String.format("%.2f", entry.getMolecularWeight()) + "g/mole;");
                }
                if (entry.getMeltingPoint() != 0.0) {
                    writer.write("Melting Point: " + String.format("%.2f", entry.getMeltingPoint()) + "°C;");
                }
                if (entry.getGcEnrichment() != 0.0) {
                    writer.write("GC Enrichment: " + String.format("%.2f", entry.getGcEnrichment() * 100) + "%;");
                }
                if (entry.getNetCharge() != 0.0) {
                    writer.write("Net Charge(at ph 7): " + String.format("%.2f", entry.getNetCharge()) + ";");
                }
                if (entry.getIsoelectricPoint() != 0.0) {
                    writer.write("Iso electricPoint: " + String.format("%.2f", entry.getIsoelectricPoint()) + "pH;");
                }
                writer.newLine();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}