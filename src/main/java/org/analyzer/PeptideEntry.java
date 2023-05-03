package org.analyzer;

import java.util.Dictionary;
/**
 * Class for peptide sequences
 */
class PeptideEntry extends FastaEntry implements EntryI{
    static final Dictionary PEPTIDE_DICTIONARY = null;
    static final Dictionary MOLECULAR_WEIGHTS_PEPTIDES = null;
    static final Dictionary MELTING_TEMPERATURES_PEPTIDES = null;
    /**
     * For calculating Melting Point of the sequence
     */
    public void calcMeltingPoint(){
    }
    /**
     * For calculating the Molecular weight of the sequence
     */
    public void calcMolecularWeight(){

    }
    /**
     * For calculating the GC enrichment in the sequence
     */
    public void calcGC(){

    }
    /**
     * For calculating the Net Charge of the sequence
     */
    public void calcNetCharge(){

    }
    /**
     * For translating the sequence into another type of sequence
     */
    public void translateNucleotides(){

    }
}
