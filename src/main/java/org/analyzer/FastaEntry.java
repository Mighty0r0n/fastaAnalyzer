package org.analyzer;

import java.util.ArrayList;
import java.util.List;

class FastaEntry {

    String seqID;
    StringBuilder sequence;
    String sequenceType;

    FastaEntry(String type){
        this.sequenceType = type;
    }


}
