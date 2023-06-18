package org.analyzer;

public class CorruptedSequenceException extends RuntimeException{
    public CorruptedSequenceException(String message){
        super(message);
    }
}
