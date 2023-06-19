package org.analyzer;

/**
 * Custom Exception for malformatted fasta input files.
 */
public class MalformattedFastaFileException extends RuntimeException {
    public MalformattedFastaFileException(String message) {
        super(message);
    }
}
