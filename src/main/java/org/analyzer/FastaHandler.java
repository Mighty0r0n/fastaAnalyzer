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
    private static FastaHandler instance;
    List<FastaEntry> entryList = new ArrayList<>();


    public static FastaHandler getInstance(){
        if (instance == null){
            instance = new FastaHandler();
        }return instance;
    }

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
                        this.entryList.get(headerCounter).buildClass(sequenceHandler.toString());
                    }
                    // clear sequenceHandler after every entry discovered and update headerCounter
                    sequenceHandler = new StringBuilder();
                    headerCounter++;
                } else if (fastaLine.startsWith(";")) {
                } else {
                    sequenceHandler.append(fastaLine);

                }
            }catch (NoSuchElementException e) {
                // Needed for adding last entry information to the object with above logic. TO-DO Fix Counter logic, to apply buildClass here!
                this.entryList.get(headerCounter).buildClass(sequenceHandler.toString());
//                this.entryList.get(headerCounter).setSequence(sequenceHandler.toString());
//                this.entryList.get(headerCounter).setSequenceLength(sequenceHandler.length());
//                this.entryList.get(headerCounter).calcAlphabet(this.entryList.get(headerCounter));
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
