package org.analyzer;

import java.util.Dictionary;


class FastaEntry {

    String seqID;
    StringBuilder sequence;
    String sequenceType;
    int sequenceLength;
    Dictionary alphabetCount;

    FastaEntry(String type){
        this.sequenceType = type;
    }

    public void calcMeltingPoint(){

    }
    public void calcMolecularWeight(){

    }

    public void calcGC(){

    }

    public void calcNetCharge(){

    }

    public void translateNucleotides(){

    }

}
