package org.analyzer;

public class WrongFastaFormatException extends Exception{

    public WrongFastaFormatException() {
        super("File is not correctly fasta formatted");
    }
}
