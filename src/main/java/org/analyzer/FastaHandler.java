package org.analyzer;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import java.util.regex.Pattern;

/**
 * Class for handling the FastaEntry's. Methods are hidden inside the Constructor. It's possible to
 * append more Objects to this Object via the wrapper Function addFastaEntry()
 */
public class FastaHandler {
    Map<String, LinkedList<FastaEntry>> fastaMap = new HashMap<>();
    private static FastaHandler instance;

    /**
     * Invoke for singleton
     * Additional input file check, so no duplicate File will be parsed.
     *
     * @param fastaFile Input File for the analysis
     * @param seqType   Sequence Type of the input file
     * @return instance of the class
     * @throws FileNotFoundException If Incorrect File Path is provided
     */
    public static FastaHandler getInstance(String fastaFile, String seqType) throws FileNotFoundException {
        String filename = fastaFile.split("/")[fastaFile.split("/").length - 1];
        if (instance == null) {
            instance = new FastaHandler(fastaFile, seqType);

        } else if (!instance.fastaMap.containsKey(filename)) {
            instance.addFastaEntry(fastaFile, seqType);

        }
        return instance;
    }

    /**
     * Invoke for singleton, without SeqType param. Will be treated as ambiguous here
     * Additional input file check, so no duplicate File will be parsed.
     * No input file memory here, so calculations can be redone when provided with sequence type again.
     *
     * @param fastaFile Input File for the analysis
     * @return instance of the class
     * @throws FileNotFoundException If Incorrect File Path is provided
     */
    public static FastaHandler getInstance(String fastaFile) throws FileNotFoundException {
        String filename = fastaFile.split("/")[fastaFile.split("/").length - 1].split("\\.")[0] + "-ambiguous.fasta";
        if (instance == null) {
            instance = new FastaHandler(fastaFile);

        } else if (!instance.fastaMap.containsKey(filename)) {
            instance.addFastaEntry(fastaFile);
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
    public void addFastaEntry(String fasta, String type) throws FileNotFoundException {
        this.parseFasta(fasta, type);
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters.
     *
     * @param fasta File to analyze
     * @throws FileNotFoundException If an invalid FilePath is given as Input an exception is thrown.
     */
    public void addFastaEntry(String fasta) throws FileNotFoundException {
        this.parseFasta(fasta, null);
    }

    private String insertLineBreaks(String input) {
        StringBuilder printableSequence = new StringBuilder();

        int currentIndex = 0;
        while (currentIndex < input.length()) {
            int endIndex = Math.min(currentIndex + 70, input.length());
            printableSequence.append(input, currentIndex, endIndex);

            if (endIndex < input.length()) {
                printableSequence.append("\n");
            }

            currentIndex = endIndex;
        }

        return printableSequence.toString();
    }

    private SequenceType getSequenceType(String type, String filename) {
        SequenceType seqType = SequenceType.AMBIGUOUS;

        // Creating the sequenceType here enables multi file support.

        if (type != null) {
            try {
                seqType = SequenceType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException iae) {
                System.err.println("Invalid sequence type. \nGiven sequence type: " + type + """          
                        \nValid Sequence types:
                                        -DNA
                                        -RNA
                                        -PEPTIDE
                                        -AMBIGUOUS
                                        
                        Sequence type is set to ambiguous, but consider to rerun the program.
                        On ambiguous sequences are no further metadata analysis besides sequence translation
                        possible.
                          """);
            }
        } else {
            System.err.println("""

                    No Sequence Type provided. No immediate action required.
                    FastaEntry object are still filled with the seqID the Sequence and translated Sequence(If DNA or RNA),
                    but no further metadata analysis is available from here.\s
                    Please consider to rerun the Program and submit the Sequence Type of the Fasta Sequences for further analysis.""" + "\nFile is saved as: " + filename.split("\\.")[0] + "-ambiguous.fasta in the FastaHandler");
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


        String filename = fasta.split("/")[fasta.split("/").length - 1];
        LinkedList<FastaEntry> entryList = new LinkedList<>();
        SequenceType seqType = getSequenceType(type, filename);
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
                entryList.add(tmpEntry);

                if (headerCounter != -1) {
                    // For getting the fore last entry in the entryList because this logic appends the sequence of the
                    // fore last when a NEW Object is found.
                    entryList.get(entryList.size() - 2).settingSequenceProperties(sequenceHandler.toString(), seqType);
                }
                sequenceHandler = new StringBuilder();
                headerCounter++;
            } else if (fastaLine2.startsWith(";")) {
                System.out.println("Fasta contains comment lines, this parser is ignoring them.");
            } else {
                sequenceHandler.append(fastaLine2.toUpperCase());
            }
        }
        entryList.get(entryList.size() - 1).settingSequenceProperties(sequenceHandler.toString(), seqType);

        setFastaMap(filename, entryList, seqType);
    }


    private void setFastaMap(String filename, LinkedList<FastaEntry> entryList, SequenceType seqType) {
        if (Objects.requireNonNull(seqType) == SequenceType.AMBIGUOUS) {
            fastaMap.put(filename.split("\\.")[0] + "-ambiguous.fasta", entryList);
        } else {
            fastaMap.put(filename, entryList);
        }
    }

    /**
     * Generates an output file for every parsed file. Includes metadata inside the comment bracket of the fasta
     *
     * @param outputDirectory specifies the Directory where the files are saved to
     */
    public void generateOutputFiles(String outputDirectory) {
        for (String inFile : this.fastaMap.keySet()) {
            String outFile = inFile.split("\\.")[0] + "_analyzed.fasta";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + outFile))) {
                for (FastaEntry entry : this.fastaMap.get(inFile)) {
                    writer.write(entry.getSeqID());
                    writer.newLine();
                    writer.write(";Sequence Length: " + entry.getSequenceLength() + "\t");
                    if (entry.getMolecularWeight() != 0.0) {
                        writer.write("Molecular Weight: " + String.format("%.2f", entry.getMolecularWeight()) + "g/mole\t");
                    }
                    if (entry.getMeltingPoint() != 0.0) {
                        writer.write("Melting Point: " + String.format("%.2f", entry.getMeltingPoint()) + "°C\t");
                    }
                    if (entry.getGcEnrichment() != 0.0) {
                        writer.write("GC Enrichment: " + String.format("%.2f", entry.getGcEnrichment() * 100) + "%\t");
                    }
                    if (entry.getNetCharge() != 0.0) {
                        writer.write("Net Charge(at ph 7): " + String.format("%.2f", entry.getNetCharge()) + "\t");
                    }
                    if (entry.getIsoelectricPoint() != 0.0) {
                        writer.write("Iso electricPoint: " + String.format("%.2f", entry.getIsoelectricPoint()) + "pH\t");
                    }
                    writer.newLine();
                    writer.write((entry.getTranslatedSequence() == null) ? insertLineBreaks(entry.getSequence()) : insertLineBreaks(entry.getTranslatedSequence()));
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

                // In general peptides doesn't contain less than 4 unique amino acids in a peptide chain
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
     * and checks the alphabet of the given input sequence. Throws Errors if the File is not formatted
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
                if (inHeader) {
                    throw new MalformatedFastaFileException("Invalid format: Missing sequence for " + sequenceID);
                }
                inHeader = true;
                headerCount++;
                sequenceID = line;
            } else if (line.startsWith(";") || line.isEmpty()) {
            } else if (inHeader) {
                checkSequenceType(seqType, line.toUpperCase());
                inSequence = true;
            }
        }
        if (!inSequence) {
            throw new MalformatedFastaFileException("Invalid format: Last sequence ID: " + sequenceID + " has no sequence");
        }
        if (headerCount == 0) {
            throw new MalformatedFastaFileException("Invalid format: " + sequenceID);
        }
    }

    public Map<String, LinkedList<FastaEntry>> getFastaMap() {
        return fastaMap;
    }
}