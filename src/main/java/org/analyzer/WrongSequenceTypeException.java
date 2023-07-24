package org.analyzer;

/**
 * Custom Exception for checking the sequence type of sequence input.
 */
public class WrongSequenceTypeException extends Exception {
    public WrongSequenceTypeException(String message) {
        super(message);
    }
}
