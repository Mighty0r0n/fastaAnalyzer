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
        int headerCounter = -1;

        for (;;){
            try {
                String fastaLine = fastaReader.nextLine();

                if (fastaLine.startsWith(">")){

                    FastaEntry tmpEntry = new FastaEntry(sequenceType);
                    tmpEntry.seqID = fastaLine;
                    this.entryList.add(tmpEntry);

                    if (headerCounter != -1) {  // ohne if schachtelung machbar??
                        this.entryList.get(headerCounter).sequence = sequenceHandler;
                    }
                    // clear sequenceHandler after every entry discovered
                    sequenceHandler = new StringBuilder();

                    headerCounter++;

                } else {
                    sequenceHandler.append(fastaLine);
                }
            }catch (NoSuchElementException e) {
                this.entryList.get(headerCounter).sequence = sequenceHandler;
                return;
            }
        }

    }
}
