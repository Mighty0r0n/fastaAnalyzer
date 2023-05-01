package org.analyzer;

import java.util.Dictionary;


class DnaEntry implements EntryI{

    String seqID;
    StringBuilder sequence;
    String sequenceType;
    int sequenceLength;
    Dictionary alphabetCount;

    DnaEntry(String type){
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
