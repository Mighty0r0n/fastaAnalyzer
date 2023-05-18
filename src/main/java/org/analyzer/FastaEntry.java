package org.analyzer;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Class, to specify the needed fields for the subclasses.
 */
abstract class FastaEntry  implements EntryI{
    private String seqID;
    private String sequence;
    private int sequenceLength;
    private Map<Character,Double> alphabetCount = new HashMap<>();
    public String getSeqID() {
        return seqID;
    }
    public void setSeqID(String seqID) {
        this.seqID = seqID;
    }
    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public int getSequenceLength() {
        return sequenceLength;
    }
    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public Map<Character, Double> getAlphabetCount() {
        return alphabetCount;
    }

    public void setAlphabetCount(Map<Character, Double> alphabetCount) {
        this.alphabetCount = alphabetCount;
    }



    void buildClass(String sequenceHandler) {
        this.setSequence(sequenceHandler);
        this.setSequenceLength(sequenceHandler.length());
        this.calcAlphabet(this);
    }



}
