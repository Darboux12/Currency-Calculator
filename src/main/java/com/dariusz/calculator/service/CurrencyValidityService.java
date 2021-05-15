package com.dariusz.calculator.service;

import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.response.NbpApiCurrencyResponse;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;

import java.util.List;
import java.util.Set;

public interface CurrencyValidityService {

    void validateCurrencyAvailability(List<String> currencyCodes) throws CurrencyNotAvailableException;

    void validateCurrencyAmount(double currencyAmount) throws CurrencyAmountNotPositiveException;

    void validateCurrencyExchangeRequest(CurrencyExchangeRequest request) throws CurrencyNotAvailableException, CurrencyAmountNotPositiveException;

    void validateRatePresence(NbpApiCurrencyResponse response) throws RateNotPresentException;
}
