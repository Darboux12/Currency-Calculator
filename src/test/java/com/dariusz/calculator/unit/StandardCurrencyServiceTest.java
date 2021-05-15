package com.dariusz.calculator.unit;

import com.dariusz.calculator.controller.standard.StandardCurrencyController;
import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dal.repository.CurrencyRepository;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.service.CurrencyService;
import com.dariusz.calculator.service.CurrencyValidityService;
import com.dariusz.calculator.service.EventService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;
import com.dariusz.calculator.service.standard.StandardCurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StandardCurrencyServiceTest {

    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    CurrencyValidityService validityService;

    @Mock
    EventService eventService;

    @InjectMocks
    private StandardCurrencyService currencyService;

    @Test
    void findAllAvailableCurrency_Return_Two_Currency_Codes(){

        List<Currency> currencies = List.of(Currency.CHF, Currency.USD);

        Mockito.when(currencyRepository.findAllAvailableCurrency()).thenReturn(currencies);

        Iterable<Currency> currencyResponses = currencyService.findAllAvailableCurrency();

        currencyResponses.forEach(currency -> Assertions.assertTrue(currency.getCode().equals(Currency.CHF.getCode()) || currency.getCode().equals(Currency.USD.getCode())));

    }

    @Test
    void exchangeCurrency_Return_Valid_Code_From() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException, RateNotPresentException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", 5.0);

        CurrencyExchangeResponse response = currencyService.exchangeCurrency(request);

        Assertions.assertEquals(response.getRequest().getCurrencyCodeFrom(),"USD");

    }

    @Test
    void exchangeCurrency_Return_Valid_Code_To() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException, RateNotPresentException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", 5.0);

        CurrencyExchangeResponse response = currencyService.exchangeCurrency(request);

        Assertions.assertEquals(response.getRequest().getCurrencyCodeTo(),"EUR");

    }

    @Test
    void exchangeCurrency_Not_Available_Currency_From_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("NON", "EUR", 5.0);

        Mockito.doThrow(new CurrencyNotAvailableException("NON")).when(validityService).validateCurrencyExchangeRequest(request);

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyService.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }

    @Test
    void exchangeCurrency_Not_Available_Currency_To_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "NON", 5.0);

        Mockito.doThrow(new CurrencyNotAvailableException("NON")).when(validityService).validateCurrencyExchangeRequest(request);

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyService.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }

    @Test
    void exchangeCurrency_Negative_Currency_Amount_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", -5.0);

        Mockito.doThrow(new CurrencyAmountNotPositiveException()).when(validityService).validateCurrencyExchangeRequest(request);

        CurrencyAmountNotPositiveException thrown = assertThrows(
                CurrencyAmountNotPositiveException.class,
                () -> currencyService.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("Given currency amount must be positive number"));

    }

    @Test
    void findRates_Return_Status_OK() throws CurrencyNotAvailableException {

        List<String> currenciesCodes = List.of(Currency.CHF.getCode());

        CurrencyRatesRequest request = new CurrencyRatesRequest(currenciesCodes);

        Iterable<CurrencyRatesResponse> currencyResponses = currencyService.findCurrencyRates(request);

        boolean isSuccess = false;

        for(CurrencyRatesResponse ratesResponse : currencyResponses){

            if(ratesResponse.getCode().equals(Currency.CHF.getCode()))
                isSuccess = true;

        }

        Assertions.assertTrue(isSuccess);

    }

    @Test
    void findRates_Not_Available_Currency_Return_Exception() throws CurrencyNotAvailableException {

        List<String> currenciesCodes = List.of("NON");

        CurrencyRatesRequest request = new CurrencyRatesRequest(currenciesCodes);

        Mockito.doThrow(new CurrencyNotAvailableException("NON")).when(validityService).validateCurrencyAvailability(request.getCurrencyCodes());

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyService.findCurrencyRates(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }

}
