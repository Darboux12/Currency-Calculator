package com.dariusz.calculator.service.exception;

public class CurrencyAmountNotPositiveException extends Exception {

    public CurrencyAmountNotPositiveException() {
        super("Given currency amount must be positive number");
    }
}
