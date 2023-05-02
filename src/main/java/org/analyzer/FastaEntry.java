package org.analyzer;

import java.util.Dictionary;


abstract class FastaEntry {

    private String seqID;
    private StringBuilder sequence;
    private int sequenceLength;
    private Dictionary alphabetCount;

    public String getSeqID() {
        return seqID;
    }

    public void setSeqID(String seqID) {
        this.seqID = seqID;
    }

    public StringBuilder getSequence() {
        return sequence;
    }

    public void setSequence(StringBuilder sequence) {
        this.sequence = sequence;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public Dictionary getAlphabetCount() {
        return alphabetCount;
    }

    public void setAlphabetCount(Dictionary alphabetCount) {
        this.alphabetCount = alphabetCount;
    }


}
