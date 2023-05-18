package org.analyzer;
/**
 * Interface for provided methods for abstract class FastaEntry subclasses
 */
interface EntryI {
    default void calcAlphabet(FastaEntry o) {
        for (int i = 0; i < o.getSequenceLength(); i++){
            if (o.getAlphabetCount().containsKey(o.getSequence().charAt(i))){
                o.getAlphabetCount().put(o.getSequence().charAt(i), o.getAlphabetCount().get(o.getSequence().charAt(i))+1);
            } else {
                o.getAlphabetCount().put(o.getSequence().charAt(i), (double)1);
            }
        }
    }

    void calcMeltingPoint();
    void calcMolecularWeight();
    void calcGC();
    void calcNetCharge();
    void translateNucleotides();
}
