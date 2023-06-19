package org.analyzer;

/**
 * Custom Exception for checking the sequence type of sequence input.
 */
public class WrongSequenceTypeException extends RuntimeException {
    public WrongSequenceTypeException(String message) {
        super(message);
    }
}
