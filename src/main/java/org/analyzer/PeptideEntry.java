package org.analyzer;

import java.util.Dictionary;


class PeptideEntry implements EntryI{

    String seqID;
    StringBuilder sequence;
    String sequenceType;
    int sequenceLength;
    Dictionary alphabetCount;

    PeptideEntry(String type){
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
