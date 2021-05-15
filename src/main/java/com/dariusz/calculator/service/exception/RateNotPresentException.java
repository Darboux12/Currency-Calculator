package com.dariusz.calculator.service.exception;

public class RateNotPresentException extends Exception {

    public RateNotPresentException(String currencyCode) {
        super("Rating for" + currencyCode + " is not available!");
    }
}
