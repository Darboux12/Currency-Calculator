package com.dariusz.calculator.dal.repository;

import com.dariusz.calculator.dal.entity.Currency;

import java.util.List;

public interface CurrencyRepository {

    List<Currency> findAllAvailableCurrency();

    Currency findBaseCurrency();



}
