package com.dariusz.calculator.service.exception;

public class CurrencyNotAvailableException extends Exception {

    public CurrencyNotAvailableException(String currencyCode) {
        super(currencyCode + " is not currently available!");
    }
}
