package com.pashonokk.currencyexchangeratebot.exception;

public class CurrencyIsNotSupportedException extends RuntimeException{
    public CurrencyIsNotSupportedException(String message) {
        super(message);
    }
}
