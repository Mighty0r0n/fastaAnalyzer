package org.analyzer;
/**
 * Interface for provided methods for abstract class FastaEntry subclasses
 */
interface EntryI {
    void calcMeltingPoint();
    void calcMolecularWeight();
    void calcGC();
    void calcNetCharge();
    void translateNucleotides();
}
