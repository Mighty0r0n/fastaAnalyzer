package org.analyzer;

import java.util.HashMap;
import java.util.Map;


/**
 * Class that represents one fasta entry and saves metadata about it.
 */
class FastaEntry implements EntryI {
    public static class SequenceHandler {
        private static final Map<String, Character> codonMap = createCodonMap();

        private static Map<String, Character> createCodonMap() {
            Map<String, Character> codonMap = new HashMap<>();
            codonMap.put("AAA", 'K');
            codonMap.put("AAC", 'N');
            codonMap.put("AAG", 'K');
            codonMap.put("AAT", 'N');
            codonMap.put("ACA", 'T');
            codonMap.put("ACC", 'T');
            codonMap.put("ACG", 'T');
            codonMap.put("ACT", 'T');
            codonMap.put("AGA", 'R');
            codonMap.put("AGC", 'S');
            codonMap.put("AGG", 'R');
            codonMap.put("AGT", 'S');
            codonMap.put("ATA", 'I');
            codonMap.put("ATC", 'I');
            codonMap.put("ATG", 'M');
            codonMap.put("ATT", 'I');
            codonMap.put("CAA", 'Q');
            codonMap.put("CAC", 'H');
            codonMap.put("CAG", 'Q');
            codonMap.put("CAT", 'H');
            codonMap.put("CCA", 'P');
            codonMap.put("CCC", 'P');
            codonMap.put("CCG", 'P');
            codonMap.put("CCT", 'P');
            codonMap.put("CGA", 'R');
            codonMap.put("CGC", 'R');
            codonMap.put("CGG", 'R');
            codonMap.put("CGT", 'R');
            codonMap.put("CTA", 'L');
            codonMap.put("CTC", 'L');
            codonMap.put("CTG", 'L');
            codonMap.put("CTT", 'L');
            codonMap.put("GAA", 'E');
            codonMap.put("GAC", 'D');
            codonMap.put("GAG", 'E');
            codonMap.put("GAT", 'D');
            codonMap.put("GCA", 'A');
            codonMap.put("GCC", 'A');
            codonMap.put("GCG", 'A');
            codonMap.put("GCT", 'A');
            codonMap.put("GGA", 'G');
            codonMap.put("GGC", 'G');
            codonMap.put("GGG", 'G');
            codonMap.put("GGT", 'G');
            codonMap.put("GTA", 'V');
            codonMap.put("GTC", 'V');
            codonMap.put("GTG", 'V');
            codonMap.put("GTT", 'V');
            codonMap.put("TAA", '*');
            codonMap.put("TAC", 'Y');
            codonMap.put("TAG", '*');
            codonMap.put("TAT", 'Y');
            codonMap.put("TCA", 'S');
            codonMap.put("TCC", 'S');
            codonMap.put("TCG", 'S');
            codonMap.put("TCT", 'S');
            codonMap.put("TGA", '*');
            codonMap.put("TGC", 'C');
            codonMap.put("TGG", 'W');
            codonMap.put("TGT", 'C');

            return codonMap;
        }

        public static String translateSequence(String seq) {
            // Ignoring if DNA or RNA... Still need to handle PEPTIDES
            String sequence = seq.toUpperCase().replaceAll("U", "T");

            // If a Sequence is not dividable through 3, the functions gets an Out-ofBound Error, since no corresponding
            // Codon can be found inside the Map for Codon translation. With modulo und subtraction the SeqLength gets
            // corrected.
            int baseOverhang = sequence.length() % 3;
            int newEnd = sequence.length() - baseOverhang;
            StringBuilder translatedSequence = new StringBuilder();

            for (int i = 0; i < newEnd; i += 3) {
                translatedSequence.append(codonMap.get(sequence.substring(i, i + 3)));
            }
            return translatedSequence.toString();

        }

        public static Map<Character, Double> countAlphabet(String sequence) {
            Map<Character, Double> count = new HashMap<>();
            sequence.toUpperCase().chars()
                    .mapToObj(c -> (char) c)
                    .forEach(c -> count.merge(c, 1.0, Double::sum));

            return count;
        }
    }

    private final String seqID;
    private String sequence;
    private String translatedSequence;
    private int sequenceLength;
    private Map<Character, Double> alphabetCount;
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
        this.sequence = sequenceHandler.toUpperCase();
        this.sequenceLength = sequenceHandler.length();
        this.calcAlphabet();
        this.setTranslatedSequence(seqType);
        this.setGC(seqType);
        this.setMolecularWeight(seqType);
        this.setMeltingPoint(seqType);
        this.setNetCharge(seqType);
        this.setIsoelectricPoint(seqType, 7.0);
    }

    /**
     * Calculates the occurrence of a character in a given sequence and saves it into the class field alphabetCount.
     */
    @Override
    public void calcAlphabet() {
        this.alphabetCount = SequenceHandler.countAlphabet(this.sequence);
    }

    /**
     * Sets the peptide translation for this Sequence by the SequenceType Enum.
     * Only available for DNA/RNA sequence types.
     *
     * @param seqType SequenceType Enum for the calculation
     */
    @Override
    public void setTranslatedSequence(SequenceType seqType) {
        switch (seqType) {
            case DNA, RNA -> this.translatedSequence = SequenceHandler.translateSequence(this.sequence);
        }
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
        switch (seqType) {
            case PEPTIDE -> this.netCharge = seqType.netCharge(this.alphabetCount, 7.0);
            case DNA, RNA -> this.netCharge = SequenceType.PEPTIDE.netCharge(SequenceHandler.countAlphabet(
                            this.translatedSequence),
                    7.0
            );
        }
    }

    /**
     * Recursive Logic for getting the iso electric Point of a sequence.
     *
     * @param seqType SequenceType Enum for the calculation
     * @param pH      Initial pH to start the recursive Function. (typically 7)
     */
    // Extractable logic?
    @Override
    public void setIsoelectricPoint(SequenceType seqType, Double pH) {

        switch (seqType) {
            case PEPTIDE -> this.isoelectricPoint = seqType.setIsoelectricPoint(seqType, this.alphabetCount, 7.0);
            case DNA, RNA ->
                    this.isoelectricPoint = SequenceType.PEPTIDE.setIsoelectricPoint(seqType, SequenceHandler.countAlphabet(this.sequence), 7.0);
        }
    }

    @Override
    public String getTranslatedSequence() {
        return this.translatedSequence;
    }

    @Override
    public Map<Character, Double> getAlphabetCount() {
        return this.alphabetCount;
    }

    @Override
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