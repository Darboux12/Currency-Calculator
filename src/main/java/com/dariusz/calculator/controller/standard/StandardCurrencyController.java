package com.dariusz.calculator.controller.standard;

import com.dariusz.calculator.controller.CurrencyController;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.service.CurrencyService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StandardCurrencyController implements CurrencyController {

    private final CurrencyService currencyService;

    public StandardCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public ResponseEntity<Iterable<CurrencyResponse>> findAllAvailableCurrency() {

        Iterable<Currency> standardCurrencies = this.currencyService.findAllAvailableCurrency();

        Iterable<CurrencyResponse> currencyResponses = this.convertStandardCurrencyToCurrencyResponseIterable(standardCurrencies);

        return ResponseEntity.ok(currencyResponses);

    }

    @Override
    public ResponseEntity<CurrencyExchangeResponse> exchangeCurrency(CurrencyExchangeRequest exchangeRequest) throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        return ResponseEntity.ok(this.currencyService.exchangeCurrency(exchangeRequest));
    }

    @Override
    public ResponseEntity<Iterable<CurrencyRatesResponse>> findRates(CurrencyRatesRequest currencyRatesRequest) throws CurrencyNotAvailableException {

        return ResponseEntity.ok(this.currencyService.findCurrencyRates(currencyRatesRequest));
    }

    private CurrencyResponse convertStandardCurrencyToCurrencyResponse(Currency currency){

        return new CurrencyResponse(currency.getName(),currency.getCode());
    }

    private Iterable<CurrencyResponse> convertStandardCurrencyToCurrencyResponseIterable(Iterable<Currency> currencies){

        List<CurrencyResponse> responses = new ArrayList<>();

        currencies.forEach(currency -> responses.add(convertStandardCurrencyToCurrencyResponse(currency)));

        return responses;

    }

}
