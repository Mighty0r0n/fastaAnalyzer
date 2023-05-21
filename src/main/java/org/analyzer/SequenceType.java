package org.analyzer;

import java.util.HashMap;
import java.util.Map;
public enum SequenceType {
    DNA(createDNAMap()),
    RNA(createRNAMap()),
    PEPTIDE(createPeptideMap()),
    AMBIGOUS(createAmbigousMap());

    private final Map<Character, Double> molecularWeights;

    SequenceType(Map<Character, Double> molecularWeights) {
        this.molecularWeights = molecularWeights;
    }

    public Map<Character, Double> getMolecularWeights() {
        return molecularWeights;
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

    private static Map<Character, Double> createAmbigousMap(){
        Map<Character, Double> ambigousMap = new HashMap<>();
        return ambigousMap;
    }



}
