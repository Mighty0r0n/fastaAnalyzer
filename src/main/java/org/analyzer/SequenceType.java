package org.analyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Enum class for setting the sequence type and needed for the calculations in this package.
 */
public enum SequenceType {
    /**
     * Specifies Enum Type constants and calculations for DNA
     */
    DNA(createDNAMap()),
    /**
     * Specifies Enum Type constants and calculations for RNA
     */
    RNA(createRNAMap()),
    /**
     * Specifies Enum Type constants and calculations for Peptides
     */
    PEPTIDE(createPeptideMap()),
    /**
     * Specifies Enum Type constants and calculations for Ambiguous sequences
     */
    AMBIGUOUS(createAmbiguousMap());
    private final Map<Character, Double> molecularWeights;

    SequenceType(Map<Character, Double> molecularWeights) {
        this.molecularWeights = molecularWeights;
    }

    private static Map<Character, Double> createDNAMap() {
        Map<Character, Double> dnaMap = new HashMap<>();
        dnaMap.put('A', 313.21);
        dnaMap.put('C', 289.18);
        dnaMap.put('G', 329.21);
        dnaMap.put('T', 304.20);
        return dnaMap;
    }

    private static Map<Character, Double> createRNAMap() {
        Map<Character, Double> rnaMap = new HashMap<>();
        rnaMap.put('A', 329.2);
        rnaMap.put('C', 305.2);
        rnaMap.put('G', 345.2);
        rnaMap.put('U', 306.2);
        return rnaMap;
    }

    private static Map<Character, Double> createPeptideMap() {
        Map<Character, Double> peptideMap = new HashMap<>();
        peptideMap.put('C', 8.33);
        peptideMap.put('D', 3.86);
        peptideMap.put('E', 4.25);
        peptideMap.put('H', 6.0);
        peptideMap.put('K', 10.53);
        peptideMap.put('R', 12.48);
        peptideMap.put('Y', 10.07);
        peptideMap.put('+', 9.69); // + decoding for N_Term. Limited to Chars in this logic
        peptideMap.put('-', 2.34); // - decoding for C_Term. Limited to Chars in this logic
        return peptideMap;
    }

    private static Map<Character, Double> createAmbiguousMap() {
        return new HashMap<>();
    }

    /**
     * Calculation for gC enrichment. (g + c) / sequenceLength
     *
     * @param sequenceLength length of the inputSequence
     * @param alphabetCount  Count of each char occurrence in the inputSequence
     * @return value of the calculation
     */
    double gcEnrichment(int sequenceLength, Map<Character, Double> alphabetCount) {
        switch (this) {
            case DNA, RNA -> {
                return (((alphabetCount.get('G') != null) ? alphabetCount.get('G') : 0.0) +
                        ((alphabetCount.get('C') != null) ? alphabetCount.get('C') : 0.0)) / sequenceLength;
            }
            default -> {
                return 0.0;
            }
        }

    }

    /**
     * Calculation for molecular Weight. Summation of molecular Weights of all occurring Chars of the Alphabet from the
     * Input Sequence.
     *
     * @param alphabetCount Count of each char occurrence in the inputSequence
     * @return value of the calculation
     */
    double molecularWeight(Map<Character, Double> alphabetCount) {
        switch (this) {
            case DNA, RNA -> {
                double seqModifier = (this == SequenceType.DNA) ? 61.96 : 159.00;
                char utPlaceholder = (this == SequenceType.DNA) ? 'T' : 'U';
                double utCount = (alphabetCount.get(utPlaceholder) != null) ? alphabetCount.get(utPlaceholder) : 0.0;

                return ((alphabetCount.get('A') != null) ? alphabetCount.get('A') : 0.0) * this.molecularWeights.get('A') +
                        ((alphabetCount.get('C') != null) ? alphabetCount.get('C') : 0.0) * this.molecularWeights.get('C') +
                        ((alphabetCount.get('G') != null) ? alphabetCount.get('G') : 0.0) * this.molecularWeights.get('G') +
                        utCount * this.molecularWeights.get(utPlaceholder) -
                        seqModifier;
            }
            default -> {
                return 0.0;
            }
        }

    }

    /**
     * Calculation for the melting point of dna sequences, like oligo's.
     * For Sequences short than 14:
     * (A+T)*2+(G+C)*4
     * for longer then 14:
     * 64.9 + ( (g + c) -16.4 / seqLength)
     *
     * @param seqLength     length of the inputSequence
     * @param alphabetCount Count of each char occurrence in the inputSequence
     * @return the meting Point of the Sequence
     */
    double meltingPoint(int seqLength, Map<Character, Double> alphabetCount) {

        if (this == SequenceType.DNA) {
            double meltingPoint;
            if (seqLength < 14) {
                meltingPoint = (((alphabetCount.get('A') != null) ? alphabetCount.get('A') : 0.0) +
                        ((alphabetCount.get('T') != null) ? alphabetCount.get('T') : 0.0)) * 2 +
                        (((alphabetCount.get('G') != null) ? alphabetCount.get('G') : 0.0) +
                                ((alphabetCount.get('C') != null) ? alphabetCount.get('C') : 0.0)) * 4;
            } else {
                meltingPoint = 64.9 + ((41 * (((alphabetCount.get('G') != null) ? alphabetCount.get('G') : 0.0) +
                        ((alphabetCount.get('C') != null) ? alphabetCount.get('C') : 0.0) - 16.4)) / seqLength);
            }

            return meltingPoint;
        }
        return 0.0;
    }

    private double netChargeFractionNTerm(Character aminoAcid, Map<Character, Double> peptideCount, Double pH) {
        return ((peptideCount.get(aminoAcid) != null) ? peptideCount.get(aminoAcid) : 0.0) *
                ((Math.pow(10, this.molecularWeights.get(aminoAcid))) /
                        (Math.pow(10, pH) + Math.pow(10, this.molecularWeights.get(aminoAcid))));
    }

    private double netChargeFractionCTerm(Character aminoAcid, Map<Character, Double> peptideCount, Double pH) {
        return ((peptideCount.get(aminoAcid) != null) ? peptideCount.get(aminoAcid) : 0.0) *
                ((Math.pow(10, pH)) /
                        (Math.pow(10, pH) + Math.pow(10, this.molecularWeights.get(aminoAcid))));
    }

    /**
     * Calculates the netCharge for a Peptide Sequence.
     *
     * @param peptideCount Count of each occurring Peptide in the Sequence
     * @param pH           pH to calculate netCharge. Usually 7.
     * @return the netCharge value
     */
    double netCharge(Map<Character, Double> peptideCount, Double pH) {

        if (this == SequenceType.PEPTIDE) {
            Character[] nTermAminoAcids = {'+', 'R', 'K', 'H'};
            Character[] cTermAminoAcids = {'-', 'D', 'E', 'C', 'Y'};
            Map<Character, Double> termini = new HashMap<>();
            termini.put('+', 1.0);
            termini.put('-', 1.0);

            double nTermSum = this.netChargeFractionNTerm('+', termini, pH);

            for (Character amino : nTermAminoAcids) {
                nTermSum += this.netChargeFractionNTerm(amino, peptideCount, pH);
            }

            double cTermSum = this.netChargeFractionCTerm('-', termini, pH);

            for (Character amino : cTermAminoAcids) {
                cTermSum += this.netChargeFractionCTerm(amino, peptideCount, pH);
            }
            return nTermSum - cTermSum;
        } else {
            return 0.0;
        }

    }

    /**
     * Calculation for the Iso-electric Point. At which pH is the netCharge 0? Only for Peptide Sequences.
     *
     * @param seqType      Sequence Type enum for the Sequence.
     * @param peptideCount Count of each occurring Peptide in the Sequence
     * @param pH           Initial pH for the algorithm. Usually start with 7
     * @return the value of the pH at which the netCharge = 0
     */
    double isoelectricPoint(SequenceType seqType, Map<Character, Double> peptideCount, double pH) {
        double pHadjusted = pH;

        if (Objects.requireNonNull(seqType) == SequenceType.PEPTIDE) {
            final double tolerance = 0.1;

            double tmpNetCharge = this.netCharge(peptideCount, pHadjusted);
            if (Math.abs(tmpNetCharge) <= tolerance) {
                return pHadjusted;
            } else if (tmpNetCharge > 0) {
                pHadjusted = this.isoelectricPoint(this, peptideCount, pHadjusted + (pHadjusted / 2));
            } else if (tmpNetCharge < 0) {
                pHadjusted = this.isoelectricPoint(this, peptideCount, pHadjusted - (pHadjusted / 2));
            }
        } else {
            pHadjusted = 0.0;
        }
        return pHadjusted;
    }
}
