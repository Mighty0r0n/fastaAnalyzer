package org.analyzer;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for DNA sequences
 */
class DnaEntry extends FastaEntry {

    static final Dictionary MOLECULAR_WEIGHTS_NUCLEOTIDES = null;
    static final Dictionary MELTING_TEMPERATURES_NUCLEOTIDES = null;

    public void calcGCtest() {

        double gc_enrichment = (this.getAlphabetCount().get('G') + this.getAlphabetCount().get('C')) /
                (this.getAlphabetCount().get('A') +
                        this.getAlphabetCount().get('G') +
                        this.getAlphabetCount().get('C') +
                        this.getAlphabetCount().get('T'));
        System.out.println(gc_enrichment);

    }


    /**
     * For calculating Melting Point of the sequence
     */
    @Override
    public void calcMeltingPoint() {

    }

    /**
     * For calculating the Molecular weight of the sequence
     */
    @Override
    public void calcMolecularWeight() {

    }
    /**
     * For calculating the GC enrichment in the sequence
     */


    /**
     * For calculating the Net Charge of the sequence
     */
    @Override
    public void calcNetCharge() {

    }

    /**
     * For translating the sequence into another type of sequence
     */
    @Override
    public void translateNucleotides() {

    }
}
