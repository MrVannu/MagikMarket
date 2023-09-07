package org.project;

public class CreditoEsauritoException extends RuntimeException {
    public CreditoEsauritoException() {
        super("YOU HAVE NOT ENOUGH CREDIT TO PERFORM THIS OPERATION!");
    }
}

