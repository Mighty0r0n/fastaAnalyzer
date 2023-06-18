package org.analyzer;

public class MalformatedFastaFileException extends RuntimeException {
    public MalformatedFastaFileException(String message) {
        super(message);
    }
}
