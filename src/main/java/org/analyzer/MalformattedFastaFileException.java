package org.analyzer;

/**
 * Custom Exception for malformatted fasta input files.
 */
public class MalformattedFastaFileException extends Exception {
    public MalformattedFastaFileException(String message) {
        super(message);
    }
}
