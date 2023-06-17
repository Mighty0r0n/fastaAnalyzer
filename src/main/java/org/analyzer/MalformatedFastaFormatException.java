package org.analyzer;

public class MalformatedFastaFormatException extends RuntimeException{

    public MalformatedFastaFormatException() {
        super("File is not correctly fasta formatted");
    }
}
