package org.analyzer;

import java.util.HashMap;
import java.util.Map;


/**
 * Class that represents one fasta entry and saves matadata about it.
 */
class FastaEntry implements EntryI {
    private final String seqID;
    private String sequence;
    private int sequenceLength;
    private final Map<Character, Double> alphabetCount = new HashMap<>();
    private double molecularWeight;
    private double gcEnrichment;
    private double meltingPoint;
    private double netCharge;
    private double isoelectricPoint;

    FastaEntry(String seqID) {
        this.seqID = seqID;
    }

    /**
     * Sets all the Metadata storable in this Class. The SequenceType enum holds all formulas
     * for the calculations.
     *
     * @param sequenceHandler input sequence
     * @param seqType         enum for sequence type
     */
    void settingSequenceProperties(String sequenceHandler, SequenceType seqType) {
        this.sequence = sequenceHandler;
        this.sequenceLength = sequenceHandler.length();
        this.calcAlphabet();
        this.setGC(seqType);
        this.setMolecularWeight(seqType);
        this.setMeltingPoint(seqType);
        this.setNetCharge(seqType);
        this.setIsoelectricPoint(seqType, 7.0);
    }

    /**
     * Calculates the occurance of a character in a given sequence and saves it into the class field alphabetCount.
     */
    @Override
    public void calcAlphabet() {
        this.sequence.chars()
                .mapToObj(c -> (char) c)
                .forEach(c -> this.alphabetCount.merge(c, 1.0, Double::sum));
    }

    /**
     * Sets the GC Enrichment calculated for this Sequence by the SequenceType Enum
     *
     * @param seqType SequenceType Enum for the calculation
     */
    @Override
    public void setGC(SequenceType seqType) {
        this.gcEnrichment = seqType.gcEnrichment(this.sequenceLength, this.alphabetCount);
    }

    /**
     * Sets the Molecular Weight calculated for this Sequence by the SequenceType Enum
     *
     * @param seqType SequenceType Enum for the calculation
     */
    @Override
    public void setMolecularWeight(SequenceType seqType) {
        this.molecularWeight = seqType.molecularWeight(this.alphabetCount);
    }

    /**
     * Sets the Melting Point calculated for this Sequence by the SequenceType Enum
     *
     * @param seqType SequenceType Enum for the calculation
     */
    @Override
    public void setMeltingPoint(SequenceType seqType) {
        this.meltingPoint = seqType.meltingPoint(this.sequenceLength, this.alphabetCount);
    }

    /**
     * Sets the NetCharge calculated for this Sequence by the SequenceType Enum
     *
     * @param seqType SequenceType Enum for the calculation
     */
    @Override
    public void setNetCharge(SequenceType seqType) {
        this.netCharge = seqType.netCharge(this.alphabetCount, 7.0);
    }

    /**
     * Recursive Logic for getting the isoelectric Point of a sequence.
     *
     * @param seqType SequenceType Enum for the calculation
     * @param pH Initial pH to start the recursive Function. (typically 7)
     */
    // Extractable logic?
    @Override
    public void setIsoelectricPoint(SequenceType seqType, Double pH) {

        switch (seqType) {
            case PEPTIDE -> {
                final double tolerance = 0.004;
                double tmpNetCharge = seqType.netCharge(this.alphabetCount, pH);
                if (Math.abs(tmpNetCharge) <= tolerance) {
                    this.isoelectricPoint = pH;
                } else if (tmpNetCharge > 0) {
                    this.setIsoelectricPoint(seqType, pH + (pH / 2));
                } else if (tmpNetCharge < 0) {
                    this.setIsoelectricPoint(seqType, pH - (pH / 2));
                }
            }
            default -> System.out.println();
        }
    }

    @Override
    public Map<Character, Double> getAlphabetCount() {
        return this.alphabetCount;
    }

    public double getGcEnrichment() {
        return gcEnrichment;
    }

    @Override
    public double getMolecularWeight() {
        return this.molecularWeight;
    }

    @Override
    public double getMeltingPoint() {
        return this.meltingPoint;
    }

    @Override
    public double getNetCharge() {
        return this.netCharge;
    }

    @Override
    public String getSeqID() {
        return seqID;
    }

    @Override
    public String getSequence() {
        return sequence;
    }

    @Override
    public int getSequenceLength() {
        return sequenceLength;
    }

    @Override
    public double getIsoelectricPoint() {
        return isoelectricPoint;
    }
}