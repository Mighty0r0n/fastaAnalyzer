package org.analyzer;


import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Class, to specify the needed fields for the subclasses.
 */
abstract class FastaEntry implements EntryI {

    /*
     Klassenfelder
     */
    private String seqID;
    private String sequence;
    private int sequenceLength;
    private double gcEnrichment;
    private Map<Character, Double> alphabetCount = new HashMap<>();
    private double molecularWeight;

    /*
    Getter/Setter
     */
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

    public double getGcEnrichment() {
        return gcEnrichment;
    }

    public void setGcEnrichment(double gcEnrichment) {
        this.gcEnrichment = gcEnrichment;
    }

    public double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    /*
        Methoden
        */
    void buildClass(String sequenceHandler) {
        this.setSequence(sequenceHandler);
        this.setSequenceLength(sequenceHandler.length());
        this.calcAlphabet(this);
    }

    void calcGC(SequenceType seqType) {

        switch (seqType) {
            case PEPTIDE, AMBIGOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for GC enrichment analysis");
            case DNA, RNA ->
                    this.setGcEnrichment((this.getAlphabetCount().get('G') + this.getAlphabetCount().get('C')) /
                            (this.getSequenceLength()));
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }

    void calcMolecularWeight(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for GC enrichment analysis");
            case DNA -> this.setMolecularWeight(this.getAlphabetCount().get('A') * 313.21 +
                    this.getAlphabetCount().get('C') * 289.18 +
                    this.getAlphabetCount().get('G') * 329.21 +
                    this.getAlphabetCount().get('T') * 304.20 -
                    61.96);
            case RNA -> this.setMolecularWeight(this.getAlphabetCount().get('A') * 329.21 +
                    this.getAlphabetCount().get('C') * 305.2 +
                    this.getAlphabetCount().get('G') * 345.2 +
                    this.getAlphabetCount().get('U') * 306.2 +
                    159);
            default -> System.out.println("seqType = " + seqType + "not valid");

        }

    }


}
