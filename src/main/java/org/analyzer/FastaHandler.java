package org.analyzer;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import java.util.regex.Pattern;

/**
 * Class for handling the FastaEntrys. Methods are hidden inside the Constructor. It's possible to
 * append more Objects to this Object via the wrapper Function addFastaEntry()
 */
public class FastaHandler {
    LinkedList<FastaEntry> entryList = new LinkedList<>();
    LinkedList<String> fastaFileList = new LinkedList<>();
    private static FastaHandler instance;

    /**
     * Invoke for singleton
     * Additional inputfile check, so no duplicate File will be parsed.
     *
     * @param fastaFile Input File for the analysis
     * @param seqType   Sequence Type of the input file
     * @return instance of the class
     * @throws FileNotFoundException If Incorrect File Path is provided
     */
    public static FastaHandler getInstance(String fastaFile, String seqType) throws FileNotFoundException {
        if (instance == null) {

            instance = new FastaHandler(fastaFile, seqType);
            instance.fastaFileList.add(fastaFile.split("/")[fastaFile.split("/").length - 1]);

        } else if (!instance.fastaFileList.contains(fastaFile.split("/")[fastaFile.split("/").length - 1])) {
            instance.addFastaEntrys(fastaFile, seqType);
            instance.fastaFileList.add(fastaFile.split("/")[fastaFile.split("/").length - 1]);
        }
        return instance;
    }

    /**
     * Invoke for singleton, without SeqType param. Will be treated as ambigous here
     * Additional inputfile check, so no duplicate File will be parsed.
     * No input file memory here, so calculations can be redone when provided with sequence type again.
     *
     * @param fastaFile Input File for the analysis
     * @return instance of the class
     * @throws FileNotFoundException If Incorrect File Path is provided
     */
    public static FastaHandler getInstance(String fastaFile) throws FileNotFoundException {
        if (instance == null) {
            instance = new FastaHandler(fastaFile);
        } else {
            instance.addFastaEntrys(fastaFile);
        }
        return instance;
    }

    /**
     * Constructor for the Class. It needs a fasta file and the corresponding Sequence Type to get instantiated
     *
     * @param fastaFile File to start analysis with
     * @param seqType   Sequence type of the input sequence
     * @throws FileNotFoundException if wrong file path is provided
     */
    FastaHandler(String fastaFile, String seqType) throws FileNotFoundException {
        this.parseFasta(fastaFile, seqType);
    }

    /**
     * Constructor for the Class. It needs a fasta file and the corresponding Sequence Type to get instantiated
     *
     * @param fastaFile File to start analysis with
     * @throws FileNotFoundException if wrong file path is provided
     */
    FastaHandler(String fastaFile) throws FileNotFoundException {
        this.parseFasta(fastaFile, null);
    }


    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters.
     *
     * @param fasta File to analyze
     * @param type  Type of the given FastaFile
     * @throws FileNotFoundException If an invalid FilePath is given as Input an exception is thrown.
     */


    public void addFastaEntrys(String fasta, String type) throws FileNotFoundException {
        this.parseFasta(fasta, type);
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters.
     *
     * @param fasta File to analyze
     * @throws FileNotFoundException If an invalid FilePath is given as Input an exception is thrown.
     */
    public void addFastaEntrys(String fasta) throws FileNotFoundException {
        this.parseFasta(fasta, null);
    }

    private void checkSequenceType(SequenceType seqtype, String sequence) {
        switch (seqtype) {
            case DNA -> {
                if (!Pattern.matches("[ATGC]+", sequence)) {
                    throw new WrongSequenceTypeException("Sequence doesn't look like a DNA sequence");
                }
            }
            case RNA -> {
                if (!Pattern.matches("[AUGC]+", sequence)) {
                    throw new WrongSequenceTypeException("Sequence doesn't look like a RNA sequence");
                }
            }
            case PEPTIDE -> {

                // In general peptides doesnt contain less then 4 unique amino acids in a peptide chain
                // so this is for differentiate between DNA/RNA and peptides better
                if (!Pattern.matches("[ACDEFGHIKLMNPQRSTVWY]+", sequence) || sequence.chars().distinct().count() <= 4) {
                    throw new WrongSequenceTypeException("Sequence doesn't look like a Peptide sequence");
                }
            }
            case AMBIGUOUS -> {
                // allows all possible Characters in DNA, RNA and Peptides, but nothing more
                if (!Pattern.matches("[ACDEFGHIKLMNPQRSTVWYU]+", sequence)) {
                    throw new WrongSequenceTypeException("Sequence type doesn't look like any sequence at all");
                }
            }
        }
    }

    /**
     * Method for checking the format of the Input Fasta-File. Checks for alternating Header Sequence pairs
     * and checks the alphabet of the given input sequence. Throws Errors if the File is not formated
     * correctly.
     *
     * @param fasta   input file that needs to be checked
     * @param seqType the corresponding sequence type of the input file, for getting the alphabet of the sequence
     * @throws FileNotFoundException if the input file does not exist
     */
    public void checkFastaFormat(String fasta, SequenceType seqType) throws FileNotFoundException {

        Scanner fastaReader = new Scanner(new File(fasta));
        boolean inHeader = false;
        boolean inSequence = false;
        int headerCount = 0;
        String sequenceID = "No header found";


        while (fastaReader.hasNextLine()) {
            String line = fastaReader.nextLine().trim();

            if (inSequence && line.startsWith(">")) {
                inHeader = false;
                inSequence = false;
            }

            if (line.startsWith(">")) {
                checkHeaderStructure(inHeader, sequenceID);
                inHeader = true;
                headerCount++;
                sequenceID = line;
            } else if (line.startsWith(";") || line.isEmpty()) {

            } else if (inHeader) {
                checkSequenceType(seqType, line.toUpperCase());
                inSequence = true;
            }
        }

        checklastSequence(inSequence, sequenceID);
        checkMissingHeader(headerCount, sequenceID);
    }

    private void checklastSequence(boolean inSequence, String sequenceID) {
        if (!inSequence) {
            throw new MalformatedFastaFileException("Invalid format: Last sequence ID: " + sequenceID + " has no sequence");
        }
    }

    private void checkMissingHeader(int headerCount, String sequenceID) {
        if (headerCount == 0) {
            throw new MalformatedFastaFileException("Invalid format: " + sequenceID);
        }
    }

    private void checkHeaderStructure(boolean inHeader, String sequenceID) {
        if (inHeader) {
            throw new MalformatedFastaFileException("Invalid format: Missing sequence for " + sequenceID);
        }
    }

    private SequenceType getSequenceType(String type) {
        SequenceType seqType = SequenceType.AMBIGUOUS;

        // Creating the sequenceType here enables multifile support.

        if (type != null) {
            seqType = SequenceType.valueOf(type.toUpperCase());
        } else {
            System.err.println("""

                    No Sequence Type provided. No immediate action required.
                    FastaEntry object are still filled with the seqID the Sequence and translated Sequence(If DNA or RNA),
                    but no further metadata analysis is available from here.\s
                    Please consider to rerun the Program and submit the Sequence Type of the Fasta Sequences for further analysis.""");
        }

        return seqType;
    }

    /**
     * This Method reads the input file and fill all necessary Objects with the information's needed, for further
     * calculations.
     *
     * @param fasta The chosen fasta input file
     * @throws FileNotFoundException Path of the input file is incorrect
     */
    private void parseFasta(String fasta, String type) throws FileNotFoundException {

        SequenceType seqType = getSequenceType(type);
        checkFastaFormat(fasta, seqType);

        StringBuilder sequenceHandler = new StringBuilder();

        Scanner fastaReader = new Scanner(new File(fasta));

        int headerCounter = -1;

        // main parsing logic. Object is creating when header line is found and is filled line by line with
        // all the needed information here

        while (fastaReader.hasNext()) {
            String fastaLine2 = fastaReader.nextLine().trim();
            if (fastaLine2.startsWith(">")) {
                FastaEntry tmpEntry = new FastaEntry(fastaLine2);
                this.entryList.add(tmpEntry);

                if (headerCounter != -1) {
                    // For getting the forelast entry in the entryList because this logic appends the sequence of the
                    // forelast when a NEW Object is found.
                    this.entryList.get(this.entryList.size() - 2).settingSequenceProperties(sequenceHandler.toString(), seqType);
                }
                sequenceHandler = new StringBuilder();
                headerCounter++;
            } else if (fastaLine2.startsWith(";")) {
                System.out.println("Fasta contains commentlines, this parser is ignoring them.");
            } else {
                sequenceHandler.append(fastaLine2.toUpperCase());
            }
        }
        this.entryList.get(this.entryList.size() - 1).settingSequenceProperties(sequenceHandler.toString(), seqType);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}