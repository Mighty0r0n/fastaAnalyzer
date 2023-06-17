package org.analyzer;

public class UnsupportedSequenceTypeError extends Error{
    public UnsupportedSequenceTypeError(){
        super("""
                    Provided sequence type is not valid!
                                        
                    Valid sequence types:
                    -DNA
                    -RNA
                    -PEPTIDE
                    -AMBIGUOUS
                    
                    """);
    }
}
