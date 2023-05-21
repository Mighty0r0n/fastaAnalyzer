

/**
 * Factoryclass for FastaEntry objects
 */
class FastaEntryFactory {
    private static FastaEntryFactory instance;
    /**
     * singleton method, for ensuring that only 1 instance of this instance is running.
     *
     * @return instance returnes the existing object instance.
     */
    public static FastaEntryFactory getInstance(){
        if (instance == null){
            instance = new FastaEntryFactory();
        }return instance;
    }
//    FastaEntry generateEntryObject(SequenceType sequenceType){
//
//        FastaEntry fastaEntry = null;
//
//        if (sequenceType == SequenceType.DNA){
//            fastaEntry = new DnaEntry();
//        } else if (sequenceType == SequenceType.RNA) {
//            fastaEntry = new RnaEntry();
//        } else if (sequenceType == SequenceType.PEPTIDE) {
//            fastaEntry = new PeptideEntry();
//        } else if (sequenceType == SequenceType.AMBIGOUS) {
//            fastaEntry = new AmbigousEntry();
//        }
//        return fastaEntry;
//    }
}
