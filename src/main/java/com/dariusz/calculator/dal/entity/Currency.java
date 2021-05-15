package com.dariusz.calculator.dal.entity;

public enum Currency {

    CHF("Swiss Franc","CHF"),
    USD("United States Dollar","USD"),
    EUR("Euro","EUR"),
    GBP("Pound Sterling","GBP"),
    CZK("Czech Koruna","CZK"),
    PLN("Zloty","PLN"),
    HUF("Forint","HUF"),
    NOK("Norwegian Krone","NOK");

    private String name;
    private String code;

    Currency(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
