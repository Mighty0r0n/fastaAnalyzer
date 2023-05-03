package org.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.StringBuilder;
/**
 * Public Class which is can be used outside the package, for analyzing the wanted fasta file.
 */
public class FastaHandler {
    List<FastaEntry> entryList = new ArrayList<>();
    /**
     * This Method reads the input file and fill all necessary Objects with the informations needed, for further
     * calculations.
     *
     * @param fasta The choosen fasta input file
     * @param sequenceType [RNA, DNA, PEPTIDE, AMBIGOUS] The type the input sequence is.
     * @throws FileNotFoundException Path of the input file is incorrect
     *
     */
    public void parseFasta(File fasta, String sequenceType) throws FileNotFoundException{

        // Creating necessary objects for parsing
        Scanner fastaReader = new Scanner(fasta);
        StringBuilder sequenceHandler = new StringBuilder();
        FastaEntryFactory factory = FastaEntryFactory.getInstance();
        int headerCounter = -1;

        // main parsing logic. Object is creating when header line is found and is filled line by line with
        // all the needed information here
        for (;;){
            try {
                String fastaLine = fastaReader.nextLine();

                if (fastaLine.startsWith(">")){

                    // tmp object is created here and saved in entryList. The object gets destroyed if logically
                    // new object is found for creating in the file.
                    FastaEntry tmpEntry = factory.generateEntryObject(sequenceType);
                    tmpEntry.setSeqID(fastaLine);
                    this.entryList.add(tmpEntry);

                    if (headerCounter != -1) {
                        this.entryList.get(headerCounter).setSequence(sequenceHandler);
                        this.entryList.get(headerCounter).setSequenceLength(sequenceHandler.length());
                    }
                    // clear sequenceHandler after every entry discovered and update headerCounter
                    sequenceHandler = new StringBuilder();
                    headerCounter++;
                } else {
                    sequenceHandler.append(fastaLine);
                }
            }catch (NoSuchElementException e) {
                // Needed for adding last entry information to the object with above logic.
                this.entryList.get(headerCounter).setSequence(sequenceHandler);
                this.entryList.get(headerCounter).setSequenceLength(sequenceHandler.length());
                return;
            }
        }
    }
    /**
     * Method for generating the output Files. Calculations are done from other classes and are gathered here.
     *
     * @param OutputPath specifies the path where to place the outputfile
     * @param param the param for the calculations. !TBA!
     */
    public void generateOutputFiles(String OutputPath, String param){

    }
}
