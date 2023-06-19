package org.analyzer;

import java.util.Map;

/**
 * Interface for provided methods for class FastaEntry
 */
interface EntryI {

    void calcAlphabet();

    void setTranslatedSequence(SequenceType seqType);

    void setGC(SequenceType seqType);

    void setMolecularWeight(SequenceType seqType);

    void setMeltingPoint(SequenceType seqType);

    void setNetCharge(SequenceType seqType);

    void setIsoelectricPoint(SequenceType seqType, Double pH);

    void setCommentLine(String comment);

    String getTranslatedSequence();

    double getIsoelectricPoint();

    String getSeqID();

    String getSequence();

    int getSequenceLength();

    Map<Character, Double> getAlphabetCount();

    double getGcEnrichment();

    double getMolecularWeight();

    double getMeltingPoint();

    double getNetCharge();

    String getCommentLine();

}
