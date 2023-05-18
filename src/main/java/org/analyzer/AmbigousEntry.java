package org.analyzer;

import java.util.Dictionary;

/**
 * Class for Ambigous Sequences
 */
class AmbigousEntry extends FastaEntry {
    static final Dictionary MOLECULAR_WEIGHTS_AMBIGOUS = null;
    static final Dictionary MELTING_TEMPERATURES_AMBIGOUS = null;



    /**
     * For calculating Melting Point of the sequence
     */
    @Override
    public void calcMeltingPoint(){

    }
    /**
     * For calculating the Molecular weight of the sequence
     */
    @Override
    public void calcMolecularWeight(){

    }
    /**
     * For calculating the GC enrichment in the sequence
     */
    @Override
    public void calcGC(){

    }
    /**
     * For calculating the Net Charge of the sequence
     */
    @Override
    public void calcNetCharge(){

    }
    /**
     * For translating the sequence into another type of sequence
     */
    @Override
    public void translateNucleotides(){

    }
}
