package org.analyzer;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Class for handling the FastaEntry's. Methods are hidden inside the Constructor. It's possible to
 * append more Objects to this Object via the wrapper Function addFastaEntry()
 */
public class FastaHandler {
    private static FastaHandler instance;
    int numberThreads;
    LinkedList<FastaEntry> fastaObjectList = new LinkedList<>();
    String filename;
    SequenceType seqType;

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

    void verbosePrinting(){
        for (FastaEntry entry : this.fastaObjectList){
            System.out.println(entry.getSeqID());
            System.out.println(entry.getSequenceLength());
            System.out.println(entry.getGcEnrichment());
            System.out.println(entry.getMeltingPoint());
            System.out.println(entry.getMolecularWeight());
            System.out.println(entry.getIsoelectricPoint());
            System.out.println(entry.getNetCharge());
        }
    }


    /**
     * Enables Multithreading
     */
    void processFastaEntries() {
        ExecutorService threadPool = Executors.newFixedThreadPool(this.numberThreads);

        for (FastaEntry entry : this.fastaObjectList) {
            threadPool.submit(entry);
        }
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(100, TimeUnit.MINUTES)) {
                System.err.println("Threads doesn't finish work within given time-limit of 100 Minutes.");
            }
        } catch (InterruptedException e) {
            e.getMessage();
        }
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters. Also, the input file gets memorized, so it won't be parsed twice.
     *
     * @param fasta File to analyze
     * @param type  Type of the given FastaFile
     */
    public void generateFastaHandlerObject(String fasta, String type) throws WrongSequenceTypeException, MalformattedFastaFileException {
        this.filename = fasta.split("/")[fasta.split("/").length - 1];
        setSequenceType(type, this.filename);
        this.parseFasta(fasta);
    }

    /**
     * Wrapper for the parseFasta() method so it can be used outside the package without direct access rights
     * to the parser logic and calculation setters. Also, the input file gets memorized, so it won't be parsed twice.
     *
     * @param fasta File to analyze
     */
    public void generateFastaHandlerObject(String fasta) throws WrongSequenceTypeException, MalformattedFastaFileException {
        this.filename = fasta.split("/")[fasta.split("/").length - 1];
        setSequenceType(null, this.filename);
        this.parseFasta(fasta);
    }

    private void setSequenceType(String type, String filename) {
        if (type != null) {
            try {
                this.seqType = SequenceType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException iae) {
                this.seqType = SequenceType.AMBIGUOUS;
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
            this.seqType = SequenceType.AMBIGUOUS;
            System.err.println("""

                    No Sequence Type provided. No immediate action required.
                    FastaEntry object are still filled with the seqID the Sequence and translated Sequence(If DNA or RNA),
                    but no further metadata analysis is available from here.\s
                    Please consider to rerun the Program and submit the Sequence Type of the Fasta Sequences for further analysis.""" + "\nFile is saved as: " + filename.split("\\.")[0] + "-ambiguous.fasta in the FastaHandler");
        }
    }

    /**
     * Parses the input fasta file and checks for format. Object gets filled here and given input file
     *
     * @param fasta input file that needs to be checked
     */
    private void parseFasta(String fasta) throws MalformattedFastaFileException, WrongSequenceTypeException {

        try {
            Scanner fastaReader = new Scanner(new File(fasta));

            boolean inHeader = false;
            boolean inSequence = false;
            int headerCount = -1;
            String sequenceID = "No header found";
            StringBuilder sequenceHandler = new StringBuilder();
            LinkedList<FastaEntry> entryList = new LinkedList<>();

            while (fastaReader.hasNextLine()) {
                String line = fastaReader.nextLine().trim();

                if (inSequence && line.startsWith(">")) {
                    inHeader = false;
                    inSequence = false;
                    entryList.get(entryList.size() - 1).settingSequenceData(sequenceHandler.toString());
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
                    checkSequenceType(this.seqType, line.toUpperCase());
                    inSequence = true;
                }
            }

            if (!inSequence) {
                throw new MalformattedFastaFileException("Invalid format: Last sequence ID: " + sequenceID + " has no sequence");
            }
            if (headerCount == -1) {
                throw new MalformattedFastaFileException("Invalid format: " + sequenceID);
            }

            entryList.get(entryList.size() - 1).settingSequenceData(sequenceHandler.toString());

            this.fastaObjectList = entryList;

        } catch (FileNotFoundException fnfe) {
            fnfe.getMessage();
        }
    }

    /**
     * Generates an output file for every parsed file. Includes metadata inside the comment bracket of the fasta
     *
     * @param outputDirectory specifies the Directory where the files are saved to
     */
    public void generateOutputFiles(String outputDirectory, boolean translate) {
        String outFile = this.filename.split("\\.")[0] + "_analyzed.fasta";
        if (translate){
            outFile = this.filename.split("\\.")[0] + "_analyzed_translated.fasta";
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + outFile))) {
            for (FastaEntry entry : this.fastaObjectList) {
                writeEntry(writer, entry, translate);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void writeEntry(BufferedWriter writer, FastaEntry entry, boolean translate) throws IOException {
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

        String sequenceToWrite = (translate && entry.getTranslatedSequence() != null) ? entry.getTranslatedSequence() : entry.getSequence();
        writer.write(insertLineBreaks(sequenceToWrite));
        writer.newLine();
    }

//    private void writeEntry(BufferedWriter writer, FastaEntry entry, boolean verbose, boolean translate) throws IOException {
//        writeVerboseOutput(writer, entry.getSeqID(), verbose);
//        writer.newLine();
//
//        writeVerboseOutput(writer, ";Sequence Length: " + entry.getSequenceLength() + "\t", verbose);
//        writeVerboseOutput(writer, "Molecular Weight: " + String.format("%.2f", entry.getMolecularWeight()) + "g/mole\t", entry.getMolecularWeight() != 0.0 && verbose);
//        writeVerboseOutput(writer, "Melting Point: " + String.format("%.2f", entry.getMeltingPoint()) + "°C\t", entry.getMeltingPoint() != 0.0 && verbose);
//        writeVerboseOutput(writer, "GC Enrichment: " + String.format("%.2f", entry.getGcEnrichment() * 100) + "%\t", entry.getGcEnrichment() != 0.0 && verbose);
//        writeVerboseOutput(writer, "Net Charge(at ph 7): " + String.format("%.2f", entry.getNetCharge()) + "\t", entry.getNetCharge() != 0.0 && verbose);
//        writeVerboseOutput(writer, "Iso electricPoint: " + String.format("%.2f", entry.getIsoelectricPoint()) + "pH\t", entry.getIsoelectricPoint() != 0.0 && verbose);
//
//        writer.newLine();
//
//        String sequenceToWrite = (translate && entry.getTranslatedSequence() != null) ? entry.getTranslatedSequence() : entry.getSequence();
//        writer.write(insertLineBreaks(sequenceToWrite));
//        writer.newLine();
//    }
//
//    private void writeVerboseOutput(BufferedWriter writer, String fastaLine, boolean verbose) throws IOException {
//        writer.write(fastaLine);
//        if (verbose) {
//            if (fastaLine.startsWith(">")) {
//                System.out.println("\n" + fastaLine);
//            } else {
//                System.out.print(fastaLine);
//            }
//        }
//    }

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
}