package org.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.lang.StringBuilder;


public class FastaHandler {

    List<FastaEntry> entryList = new ArrayList<>();

    public void parseFasta(File fasta, String sequenceType) throws FileNotFoundException{

        Scanner fastaReader = new Scanner(fasta);

        StringBuilder sequenceHandler = new StringBuilder();

        FastaEntryFactory factory = FastaEntryFactory.getInstance();

        int headerCounter = -1;

        for (;;){
            try {
                String fastaLine = fastaReader.nextLine();

                if (fastaLine.startsWith(">")){

                    FastaEntry tmpEntry = factory.generateEntryObject(sequenceType);

                    tmpEntry.setSeqID(fastaLine);
                    this.entryList.add(tmpEntry);

                    if (headerCounter != -1) {
                        this.entryList.get(headerCounter).setSequence(sequenceHandler);
                        this.entryList.get(headerCounter).setSequenceLength(sequenceHandler.length());
                    }
                    // clear sequenceHandler after every entry discovered
                    sequenceHandler = new StringBuilder();

                    headerCounter++;

                } else {
                    sequenceHandler.append(fastaLine);
                }
            }catch (NoSuchElementException e) {
                this.entryList.get(headerCounter).setSequence(sequenceHandler);
                this.entryList.get(headerCounter).setSequenceLength(sequenceHandler.length());
                return;
            }
        }
    }

    public void generateOutputFiles(String OutputPath){

    }


}
