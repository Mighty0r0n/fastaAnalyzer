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
     *
     * @return instance of the class
     */
    public static FastaHandler getInstance() {
        if (instance == null) {
            instance = new FastaHandler();
        }
        return instance;
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters. Also, the input file gets memorized, so it won't be parsed twice.
     *
     * @param fasta File to analyze
     * @param type  Type of the given FastaFile
     */
    public void addFastaEntry(String fasta, String type) throws WrongSequenceTypeException, MalformattedFastaFileException {
        String filename = fasta.split("/")[fasta.split("/").length - 1];
        if (!instance.fastaMap.containsKey(filename)) {
            this.parseFasta(fasta, type);
        }
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters. Also, the input file gets memorized, so it won't be parsed twice.
     *
     * @param fasta File to analyze
     */
    public void addFastaEntry(String fasta) throws WrongSequenceTypeException, MalformattedFastaFileException {
        if (!instance.fastaMap.containsKey(fasta.split("/")[fasta.split("/").length - 1].split("\\.")[0] + "-ambiguous.fasta")) {
            this.parseFasta(fasta, null);
        }
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

    /**
     * Parses the input fasta file and checks for format. Object gets filled here and given input file
     *
     * @param fasta input file that needs to be checked
     * @param type  the corresponding sequence type of the input file, for getting the alphabet of the sequence
     */
    private void parseFasta(String fasta, String type) throws MalformattedFastaFileException, WrongSequenceTypeException {

        try {
            Scanner fastaReader = new Scanner(new File(fasta));

            boolean inHeader = false;
            boolean inSequence = false;
            int headerCount = -1;
            String sequenceID = "No header found";
            StringBuilder sequenceHandler = new StringBuilder();
            LinkedList<FastaEntry> entryList = new LinkedList<>();
            String filename = fasta.split("/")[fasta.split("/").length - 1];
            SequenceType seqType = getSequenceType(type, filename);

            while (fastaReader.hasNextLine()) {
                String line = fastaReader.nextLine().trim();

                if (inSequence && line.startsWith(">")) {
                    inHeader = false;
                    inSequence = false;
                    entryList.get(entryList.size() - 1).settingSequenceProperties(sequenceHandler.toString(), seqType);
                }

                if (line.startsWith(">")) {
                    if (inHeader) {
                        throw new MalformattedFastaFileException("Invalid format: Missing sequence for " + sequenceID);
                    }
                    FastaEntry tmpEntry = new FastaEntry(line);
                    entryList.add(tmpEntry);
                    sequenceHandler = new StringBuilder();
                    inHeader = true;
                    headerCount++;
                    sequenceID = line;
                } else if (line.startsWith(";") || line.isEmpty()) {
                    if (line.startsWith(";")) {
                        entryList.get(entryList.size() - 1).setCommentLine(line);
                    }
                } else if (inHeader) {
                    sequenceHandler.append(line.toUpperCase());
                    checkSequenceType(seqType, line.toUpperCase());
                    inSequence = true;
                }
            }

            if (!inSequence) {
                throw new MalformattedFastaFileException("Invalid format: Last sequence ID: " + sequenceID + " has no sequence");
            }
            if (headerCount == -1) {
                throw new MalformattedFastaFileException("Invalid format: " + sequenceID);
            }

            entryList.get(entryList.size() - 1).settingSequenceProperties(sequenceHandler.toString(), seqType);

            setFastaMap(filename, entryList, seqType);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    private void checkSequenceType(SequenceType seqtype, String sequence) throws WrongSequenceTypeException {
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
     * Getter for the fastaMap dictionary, containing all the parsed information
     *
     * @return fastaMap with all parsed information
     */
    public Map<String, LinkedList<FastaEntry>> getFastaMap() {
        return fastaMap;
    }
}