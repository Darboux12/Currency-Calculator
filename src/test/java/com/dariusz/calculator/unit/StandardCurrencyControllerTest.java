package com.dariusz.calculator.unit;

import com.dariusz.calculator.controller.CurrencyController;
import com.dariusz.calculator.controller.standard.StandardCurrencyController;
import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.service.CurrencyService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
class StandardCurrencyControllerTest {

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    private StandardCurrencyController currencyController;

    @Test
    void findAllAvailableCurrency_Return_Status_OK(){

        List<Currency> currencies = List.of(Currency.CHF, Currency.USD);

        Mockito.when(currencyService.findAllAvailableCurrency()).thenReturn(currencies);

        ResponseEntity<Iterable<CurrencyResponse>> currencyResponses = currencyController.findAllAvailableCurrency();

        Assertions.assertEquals(currencyResponses.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void findAllAvailableCurrency_Return_Two_Currency_Codes(){

        List<Currency> currencies = List.of(Currency.CHF, Currency.USD);

        Mockito.when(currencyService.findAllAvailableCurrency()).thenReturn(currencies);

        Iterable<CurrencyResponse> currencyResponses = currencyController.findAllAvailableCurrency().getBody();

        int count = 0;

        assert currencyResponses != null;
        for(CurrencyResponse response : currencyResponses)
            count++;

        Assertions.assertEquals(currencies.size(),count);

    }

    @Test
    void exchangeCurrency_Return_Status_OK() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", 5.0);

        Mockito.when(currencyService.exchangeCurrency(request)).thenReturn(new CurrencyExchangeResponse(request,12.3));

        ResponseEntity<CurrencyExchangeResponse> response = currencyController.exchangeCurrency(request);

        Assertions.assertEquals(response.getStatusCode(),HttpStatus.OK);

    }

    @Test
    void exchangeCurrency_Not_Available_Currency_From_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("NON", "EUR", 5.0);

        Mockito.when(currencyService.exchangeCurrency(request)).thenThrow(new CurrencyNotAvailableException("NON"));

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyController.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }

    @Test
    void exchangeCurrency_Not_Available_Currency_To_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "NON", 5.0);

        Mockito.when(currencyService.exchangeCurrency(request)).thenThrow(new CurrencyNotAvailableException("NON"));

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyController.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }

    @Test
    void exchangeCurrency_Negative_Currency_Amount_Return_Exception() throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", -5.0);

        Mockito.when(currencyService.exchangeCurrency(request)).thenThrow(new CurrencyAmountNotPositiveException());

        CurrencyAmountNotPositiveException thrown = assertThrows(
                CurrencyAmountNotPositiveException.class,
                () -> currencyController.exchangeCurrency(request));

        Assertions.assertTrue(thrown.getMessage().contains("Given currency amount must be positive number"));

    }

    @Test
    void findRates_Return_Status_OK() throws CurrencyNotAvailableException {

        Set<String> currenciesCodes = Set.of(Currency.CHF.getCode());

        CurrencyRatesRequest request = new CurrencyRatesRequest(currenciesCodes);

        CurrencyRatesResponse response = new CurrencyRatesResponse(Currency.CHF.getName(),Currency.CHF.getCode(),3.14);

        List<CurrencyRatesResponse> responseIterable = new ArrayList<>();

        responseIterable.add(response);

        Mockito.when(currencyService.findCurrencyRates(request)).thenReturn(responseIterable);

        ResponseEntity<Iterable<CurrencyRatesResponse>> currencyResponses = currencyController.findRates(request);

        Assertions.assertEquals(currencyResponses.getStatusCode(), HttpStatus.OK);

    }

    @Test
    void findRates__Not_Available_Currency_Return_Exception() throws CurrencyNotAvailableException {

        Set<String> currenciesCodes = Set.of("NON");

        CurrencyRatesRequest request = new CurrencyRatesRequest(currenciesCodes);

        Mockito.when(currencyService.findCurrencyRates(request)).thenThrow(new CurrencyNotAvailableException("NON"));

        CurrencyNotAvailableException thrown = assertThrows(
                CurrencyNotAvailableException.class,
                () -> currencyController.findRates(request));

        Assertions.assertTrue(thrown.getMessage().contains("NON"));

    }
}
