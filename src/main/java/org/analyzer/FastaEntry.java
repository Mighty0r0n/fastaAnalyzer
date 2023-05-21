package org.analyzer;


import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Class, to specify the needed fields for the subclasses.
 */
class FastaEntry implements EntryI {
    /*
     Klassenfelder
     */
    private String seqID;
    private String sequence;
    private int sequenceLength;
    private Map<Character, Double> alphabetCount = new HashMap<>();
    private double molecularWeight;
    private double gcEnrichment;
    private double meltingPoint;
    private double netCharge;
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
    // To-DO Setter usage not needed. Should they be public usble??
    void calcMolecularWeight(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for GC enrichment analysis");
            case DNA -> this.setMolecularWeight(this.getAlphabetCount().get('A') * seqType.getMolecularWeights().get('A') +
                    this.getAlphabetCount().get('C') * seqType.getMolecularWeights().get('C') +
                    this.getAlphabetCount().get('G') * seqType.getMolecularWeights().get('G') +
                    this.getAlphabetCount().get('T') * seqType.getMolecularWeights().get('T') -
                    61.96);
            case RNA -> this.setMolecularWeight(this.getAlphabetCount().get('A') * seqType.getMolecularWeights().get('A') +
                    this.getAlphabetCount().get('C') * seqType.getMolecularWeights().get('C') +
                    this.getAlphabetCount().get('G') * seqType.getMolecularWeights().get('G') +
                    this.getAlphabetCount().get('U') * seqType.getMolecularWeights().get('U') +
                    159);
            default -> System.out.println("seqType = " + seqType + "not valid");

        }
    }
    public void calcMeltingPoint(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for Melting Point analysis");
            case DNA, RNA -> {
                if (this.sequenceLength < 14) {
                    this.meltingPoint = (this.alphabetCount.get('A') + this.alphabetCount.get('T')) * 2
                            + (this.alphabetCount.get('G') + this.alphabetCount.get('C') * 4);
                } else {
                    this.meltingPoint = 64.9 + (41 * (this.alphabetCount.get('G') +
                            this.alphabetCount.get('C') - 16.4) / this.sequenceLength);
                }
            }
            default -> System.out.println("seqType = " + seqType + "not valid");

        }
    }
    public void calcNetCharge(SequenceType seqType) {
        switch (seqType) {
            case DNA, RNA, AMBIGOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for NetCharge analysis");
            case PEPTIDE -> System.out.println("HALLO");
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }
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

    public double getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public double getNetCharge() {
        return netCharge;
    }

    public void setNetCharge(double netCharge) {
        this.netCharge = netCharge;
    }
}
