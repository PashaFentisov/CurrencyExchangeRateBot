package com.pashonokk.currencyexchangeratebot.exception;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
