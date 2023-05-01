package org.analyzer;

import java.util.Dictionary;


class AmbigousEntry {

    String seqID;
    StringBuilder sequence;
    String sequenceType;
    int sequenceLength;
    Dictionary alphabetCount;

    AmbigousEntry(String type){
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
