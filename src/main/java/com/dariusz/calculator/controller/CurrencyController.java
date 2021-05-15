package com.dariusz.calculator.controller;


import com.dariusz.calculator.constant.CurrencyEndpoint;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping(CurrencyEndpoint.server)
public interface CurrencyController {

    @GetMapping(CurrencyEndpoint.findAllAvailableCurrency)
    ResponseEntity<Iterable<CurrencyResponse>> findAllAvailableCurrency();

    @PostMapping(CurrencyEndpoint.exchangeCurrency)
    ResponseEntity<CurrencyExchangeResponse> exchangeCurrency(@Valid @RequestBody CurrencyExchangeRequest exchangeRequest) throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException, RateNotPresentException;

    @PostMapping(CurrencyEndpoint.findRates)
    ResponseEntity<Iterable<CurrencyRatesResponse>> findRates(@Valid @RequestBody CurrencyRatesRequest currencyRatesRequest) throws CurrencyNotAvailableException;

}
