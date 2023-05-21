package org.analyzer;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
/**
 * Public Class which is can be used outside the package, for analyzing the wanted fasta file.
 */
public class FastaHandler {
    private static FastaHandler instance;
    private final SequenceType sequenceType;
    LinkedList<FastaEntry> entryList = new LinkedList<>();

    public FastaHandler(SequenceType type){
        this.sequenceType = type;

    }

    /**
     * Invoke for singleton
     *
     * @param sType enum of sequnce type
     * @return instance of the class
     */
    public static FastaHandler getInstance(SequenceType sType){
        if (instance == null){
            instance = new FastaHandler(sType);
        }return instance;
    }

    /**
     * This Method reads the input file and fill all necessary Objects with the informations needed, for further
     * calculations.
     *
     * @param fasta The choosen fasta input file
     * @throws FileNotFoundException Path of the input file is incorrect
     *
     */
    public void parseFasta(File fasta) throws FileNotFoundException{

        // Creating necessary objects for parsing
        Scanner fastaReader = new Scanner(fasta);
        StringBuilder sequenceHandler = new StringBuilder();

        int headerCounter = -1;

        // main parsing logic. Object is creating when header line is found and is filled line by line with
        // all the needed information here
        for (;;){
            try {
                String fastaLine = fastaReader.nextLine();

                if (fastaLine.startsWith(">")){

                    // tmp object is created here and saved in entryList. The object gets destroyed if logically
                    // new object is found for creating in the file.

                    FastaEntry tmpEntry = new FastaEntry(fastaLine);
                    this.entryList.add(tmpEntry);

                    if (headerCounter != -1) {
                        this.entryList.get(headerCounter).settingSequenceProperties(sequenceHandler.toString(), this.sequenceType);
                    }
                    // clear sequenceHandler after every entry discovered and update headerCounter
                    sequenceHandler = new StringBuilder();
                    headerCounter++;
                } else if (fastaLine.startsWith(";")) {
                    System.out.println("Fasta contains commentlines, this parser is ignoring them.");
                } else {
                    sequenceHandler.append(fastaLine);

                }
            }catch (NoSuchElementException e) {
                // Needed for adding last entry information to the object with above logic. TO-DO Fix Counter logic, to apply buildClass here!
                this.entryList.get(headerCounter).settingSequenceProperties(sequenceHandler.toString(), this.sequenceType);
                fastaReader.close();
                return;
            }

        }
    }
    /**
     * Method for generating the output Files. Calculations are done from other classes and are gathered here.
     * 0 values are sorted out
     *
     * @param outputPath specifies the path where to place the outputfile
     */
    public void generateOutputFiles(String outputPath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (FastaEntry entry : this.entryList) {
                writer.write("Sequence ID: " + entry.getSeqID());
                writer.newLine();
                writer.write("Sequence Length: " + entry.getSequenceLength() + ";");
                if (entry.getMolecularWeight() != 0.0) {
                    writer.write("Molecular Weight: " + String.format("%.2f", entry.getMolecularWeight()) + "g/mole;");
                } if (entry.getMeltingPoint() != 0.0) {
                    writer.write("Melting Point: " + String.format("%.2f", entry.getMeltingPoint()) + "°C;");
                } if (entry.getGcEnrichment() != 0.0) {
                    writer.write("GC Enrichment: " + String.format("%.2f", entry.getGcEnrichment() * 100) + "%;");
                } if (entry.getNetCharge() != 0.0) {
                    writer.write("Net Charge(at ph 7): " + String.format("%.2f", entry.getNetCharge()) + ";");
                } if (entry.getIsoelectricPoint() != 0.0){
                    writer.write("IsoelectricPoint: " + String.format("%.2f", entry.getIsoelectricPoint())+ "pH;");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
