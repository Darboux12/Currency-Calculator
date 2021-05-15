package com.dariusz.calculator.service;

import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;

import java.io.IOException;

public interface CurrencyService {

    public Iterable<Currency> findAllAvailableCurrency();

    public CurrencyExchangeResponse exchangeCurrency(CurrencyExchangeRequest exchangeRequest) throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException, RateNotPresentException;

    public Iterable<CurrencyRatesResponse> findCurrencyRates(CurrencyRatesRequest currencyRatesRequest) throws CurrencyNotAvailableException;

}
