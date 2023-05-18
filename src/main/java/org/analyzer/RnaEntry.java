package org.analyzer;

import java.util.Dictionary;

/**
 * Class for RNA sequences
 */
class RnaEntry extends FastaEntry {
    static final Dictionary MOLECULAR_WEIGHTS_RIBONUCLEOTIDE = null;
    static final Dictionary MELTING_TEMPERATURES_RIBONUCLEOTIDE = null;



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
//    @Override
//    public void calcGC(){
//
//    }
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
