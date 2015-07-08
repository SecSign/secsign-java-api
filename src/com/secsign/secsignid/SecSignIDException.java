package com.secsign.secsignid;

/**
 * Class to wrap certain exceptions.
 * 
 * @version 1.0
 * @author SecSign Technology Inc.
 */
public class SecSignIDException extends Exception {

    public SecSignIDException() {
    }

    public SecSignIDException(String message) {
        super(message);
    }

    public SecSignIDException(Throwable cause) {
        super(cause);
    }

    public SecSignIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
