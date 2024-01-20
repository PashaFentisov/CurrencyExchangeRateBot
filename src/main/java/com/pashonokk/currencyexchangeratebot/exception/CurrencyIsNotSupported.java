package com.pashonokk.currencyexchangeratebot.exception;

public class CurrencyIsNotSupported extends RuntimeException{
    public CurrencyIsNotSupported(String message) {
        super(message);
    }
}
