package org.analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum class for setting the sequence type, needed for the calculations in this package. It provides additional
 * calculation methods for metadata.
 */
public enum SequenceType {
    DNA(createDNAMap()),
    RNA(createRNAMap()),
    PEPTIDE(createPeptideMap()),
    AMBIGUOUS(createAmbiguousMap());
    private final Map<Character, Double> molecularWeights;
    SequenceType(Map<Character, Double> molecularWeights) {
        this.molecularWeights = molecularWeights;
    }
    private static Map<Character, Double> createDNAMap(){
        Map<Character, Double> dnaMap = new HashMap<>();
        dnaMap.put('A', 313.21);
        dnaMap.put('C', 289.18);
        dnaMap.put('G', 329.21);
        dnaMap.put('T', 304.20);
        return dnaMap;
    }
    private static Map<Character, Double> createRNAMap(){
        Map<Character, Double> rnaMap = new HashMap<>();
        rnaMap.put('A', 329.2);
        rnaMap.put('C', 305.2);
        rnaMap.put('G', 345.2);
        rnaMap.put('U', 306.2);
        return rnaMap;
    }

    private static Map<Character, Double> createPeptideMap(){
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

    private static Map<Character, Double> createAmbiguousMap(){
        Map<Character, Double> ambigousMap = new HashMap<>();
        return ambigousMap;
    }

    double gcEnrichment(int sequenceLength, Double cCount, Double gCount) {
        return (gCount + cCount) / sequenceLength;
    }

    double molecularWeight(Double aCount, Double cCount, Double gCount, Double utCount, Double seqModifier){
        return aCount * this.molecularWeights.get('A') +
                cCount * this.molecularWeights.get('C') +
                gCount * this.molecularWeights.get('G') +
                utCount * this.molecularWeights.get((this == SequenceType.DNA) ? 'T':'U') -
                seqModifier;
    }

    double meltingPoint(int seqLength, Double aCount, Double cCount, Double gCount, Double utCount){
        double meltingPoint;
        if (seqLength < 14) {
            meltingPoint = (aCount + utCount) * 2 + (gCount + cCount) * 4;
        } else {
            meltingPoint = 64.9 + ((41 * (gCount+cCount-16.4)) / seqLength );
        }

        return meltingPoint;
    }

    double netChargeFractionNTerm(Character aminoAcid, Map<Character, Double> peptideCount) {
        return ((peptideCount.get(aminoAcid) != null) ? peptideCount.get(aminoAcid) *
                ((Math.pow(10, this.molecularWeights.get(aminoAcid))) /
                        (Math.pow(10, 7) + Math.pow(10, this.molecularWeights.get(aminoAcid))))  : 0.0);
    }

    double netChargeFractionCTerm(Character aminoAcid, Map<Character, Double> peptideCount) {
        return ((peptideCount.get(aminoAcid) != null) ? peptideCount.get(aminoAcid) *
                ((Math.pow(10, 7)) /
                        (Math.pow(10, 7) + Math.pow(10, this.molecularWeights.get(aminoAcid))))  : 0.0);
    }

    double netCharge(Map<Character, Double> peptideCount) {
        Character[] nTermAminoAcids = {'+', 'R', 'K', 'H'};
        Character[] cTermAminoAcids = {'-', 'D', 'E', 'C', 'Y'};
        Map<Character, Double> termini = new HashMap<>();
        termini.put('+', 1.0);
        termini.put('-', 1.0);

        double nTermSum = this.netChargeFractionNTerm('+', termini);

        for (Character amino: nTermAminoAcids){
            nTermSum += this.netChargeFractionNTerm(amino, peptideCount);
        }

        double cTermSum = this.netChargeFractionCTerm('-', termini);

        for (Character amino: cTermAminoAcids){
            cTermSum += this.netChargeFractionCTerm(amino, peptideCount);
        }
        return nTermSum - cTermSum;
    }
}
