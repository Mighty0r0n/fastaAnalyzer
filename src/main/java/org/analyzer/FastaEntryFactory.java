package org.analyzer;

class FastaEntryFactory {

    private static FastaEntryFactory instance;

    public static FastaEntryFactory getInstance(){
        if (instance == null){
            instance = new FastaEntryFactory();
        }return instance;
    }



    FastaEntry generateEntryObject(String sequenceType){

        FastaEntry fastaEntry = null;

        if (sequenceType.equalsIgnoreCase("DNA")){
            fastaEntry = new DnaEntry();
        } else if (sequenceType.equalsIgnoreCase("RNA")) {
            fastaEntry = new RnaEntry();
        } else if (sequenceType.equalsIgnoreCase("PEPTIDE")) {
            fastaEntry = new PeptideEntry();
        } else if (sequenceType.equalsIgnoreCase("AMBIGOUS")) {
            fastaEntry = new AmbigousEntry();
        }
        return fastaEntry;
    }
}
