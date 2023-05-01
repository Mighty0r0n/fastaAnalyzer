package org.analyzer;

public interface EntryI {
    abstract void calcMeltingPoint();
    abstract void calcMolecularWeight();
    abstract void calcGC();
    abstract void calcNetCharge();
    abstract void translateNucleotides();
}
