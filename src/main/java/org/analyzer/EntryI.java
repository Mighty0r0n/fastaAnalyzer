package org.analyzer;

import java.util.Map;

/**
 * Interface for provided methods for abstract class FastaEntry subclasses
 */
interface EntryI {

    void calcAlphabet();
    void calcGC(SequenceType seqType);
    void calcMolecularWeight(SequenceType seqType);
    void calcMeltingPoint(SequenceType seqType);
    void calcNetCharge(SequenceType seqType);
    String getSeqID();
    String getSequence();
    int getSequenceLength();
    Map<Character, Double> getAlphabetCount();
    double getGcEnrichment();
    double getMolecularWeight();
    double getMeltingPoint();
    double getNetCharge();

}
