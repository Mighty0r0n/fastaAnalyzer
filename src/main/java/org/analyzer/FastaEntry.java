package org.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents one fasta entry and is able to generate metadata depending on the given input sequence.
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
     * Calculates the Metadata for the provided Sequence Input and saves them into class fields
     *
     * @param sequenceHandler input sequence
     * @param seqType         enum for sequence type
     */
    void settingSequenceProperties(String sequenceHandler, SequenceType seqType) {
        this.sequence = sequenceHandler;
        this.sequenceLength = sequenceHandler.length();
        this.calcAlphabet();
        this.calcGC(seqType);
        this.calcMolecularWeight(seqType);
        this.calcMeltingPoint(seqType);
        this.calcNetCharge(seqType);
        this.calcIsoelectricPoint(seqType, 7.0);
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
     * Calculates the GC Enrichment of the Input Sequence, if given a nucleoacid type sequence.
     *
     * @param seqType enum for sequence type
     */
    @Override
    public void calcGC(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGUOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for GC enrichment analysis");
            case DNA, RNA -> this.gcEnrichment = seqType.gcEnrichment(
                    this.sequenceLength,
                    this.alphabetCount.get('G'),
                    this.alphabetCount.get('C')
            );
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }
    // To-DO Setter usage not needed. Should they be public usble??

    /**
     * Calculates the molecular weight of the given input siquence, if given a nucleoacid sequence type.
     *
     * @param seqType enum for sequence type
     */
    @Override
    public void calcMolecularWeight(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGUOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for Molecular Weight analysis");
            case DNA -> this.molecularWeight = seqType.molecularWeight(
                    this.alphabetCount.get('A'),
                    this.alphabetCount.get('C'),
                    this.alphabetCount.get('G'),
                    this.alphabetCount.get('T'),
                    61.96
            );
            case RNA -> this.molecularWeight = seqType.molecularWeight(
                    this.alphabetCount.get('A'),
                    this.alphabetCount.get('C'),
                    this.alphabetCount.get('G'),
                    this.alphabetCount.get('U'),
                    159.00
            );
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }

    /**
     * Calculate the melting point of the input sequence, if given a nucleoacid sequence type.
     *
     * @param seqType enum for sequence type
     */
    @Override
    public void calcMeltingPoint(SequenceType seqType) {
        switch (seqType) {
            case PEPTIDE, AMBIGUOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for Melting Point analysis");
            case DNA -> this.meltingPoint = seqType.meltingPoint(
                    this.sequenceLength,
                    this.alphabetCount.get('A'),
                    this.alphabetCount.get('C'),
                    this.alphabetCount.get('G'),
                    this.alphabetCount.get('T')
            );
            case RNA -> this.meltingPoint = seqType.meltingPoint(
                    this.sequenceLength,
                    this.alphabetCount.get('A'),
                    this.alphabetCount.get('C'),
                    this.alphabetCount.get('G'),
                    this.alphabetCount.get('U')
            );
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }

    /**
     * Calculates the net charge of the input sequence, if given a peptide type sequence
     *
     * @param seqType enum for sequence type
     */
    @Override
    public void calcNetCharge(SequenceType seqType) {
        switch (seqType) {
            case DNA, RNA, AMBIGUOUS ->
                    System.out.println("seqType = " + seqType + "; Not Compatible for NetCharge analysis");
            case PEPTIDE -> this.netCharge = seqType.netCharge(this.alphabetCount, 7.0);
            default -> System.out.println("seqType = " + seqType + "not valid");
        }
    }
    @Override
    public void calcIsoelectricPoint(SequenceType seqType, Double pH){
        final double tolerance = 0.004;
        switch (seqType) {
            case DNA, RNA, AMBIGUOUS -> System.out.println("HALLO");
            case PEPTIDE -> {
                double tmpNetCharge = seqType.netCharge(this.alphabetCount, pH);
                if (Math.abs(tmpNetCharge) <= tolerance) {
                    this.isoelectricPoint = pH;
                } else if (tmpNetCharge > 0) {
                    this.calcIsoelectricPoint(seqType, pH + (pH / 2));
                } else if (tmpNetCharge < 0) {
                    this.calcIsoelectricPoint(seqType, pH - (pH / 2));
                }
            }
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