package com.dariusz.calculator.api;

import com.dariusz.calculator.constant.CurrencyEndpoint;
import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyApiTest {

    @Test
    void Find_All_Available_Currency_Should_Return_Available_Currencies(){

        CurrencyResponse[] currencyList =

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .when().get(CurrencyEndpoint.server + CurrencyEndpoint.findAllAvailableCurrency)
                .then().assertThat()
                .statusCode(HttpStatus.OK.value())
                .spec(TestSpecification.buildResponseSpec())
                .extract().as((Type) CurrencyResponse[].class);

        Set<String> expectedCodes = Arrays.stream(Currency.values()).map(Currency::getCode).collect(Collectors.toSet());

        Set<String> actualCodes = Arrays.stream(currencyList).map(CurrencyResponse::getCode).collect(Collectors.toSet());

        Assertions.assertTrue(actualCodes.containsAll(expectedCodes));

    }

    @Test
    void Find_Rates_For_Given_Available_Currencies_Return_Rates(){

        CurrencyRatesRequest request = new CurrencyRatesRequest();
        request.setCurrencyCodes(new ArrayList<>());
        request.getCurrencyCodes().add(Currency.CHF.getCode());
        request.getCurrencyCodes().add(Currency.USD.getCode());
        request.getCurrencyCodes().add(Currency.GBP.getCode());

        CurrencyRatesResponse[] currencyList =

                given()
                        .spec(TestSpecification.buildRequestSpec())
                        .contentType(ContentType.JSON)
                        .with().body(request)
                        .when().post(CurrencyEndpoint.server + CurrencyEndpoint.findRates)
                        .then().assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .spec(TestSpecification.buildResponseSpec())
                        .extract().as((Type) CurrencyRatesResponse[].class);

        List<String> expectedCodes = request.getCurrencyCodes();

        Set<String> actualCodes = Arrays.stream(currencyList).map(CurrencyRatesResponse::getCode).collect(Collectors.toSet());

        Assertions.assertTrue(actualCodes.containsAll(expectedCodes));

    }

    @Test
    void Find_Rates_For_Given_Not_Available_Currencies_Return_CONFLICT(){

        CurrencyRatesRequest request = new CurrencyRatesRequest();
        request.setCurrencyCodes(new ArrayList<>());
        request.getCurrencyCodes().add("NOT_EXISTING_CURRENCY");

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .with().body(request)
                .when().post(CurrencyEndpoint.server + CurrencyEndpoint.findRates)
                .then().assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .spec(TestSpecification.buildResponseSpec());

    }

    @Test
    void Find_Rates_Empty_Rate_Currencies_Return_CONFLICT(){

        CurrencyRatesRequest request = new CurrencyRatesRequest();
        request.setCurrencyCodes(new ArrayList<>());
        request.getCurrencyCodes().add("NOT_EXISTING_CURRENCY");

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .with().body(request)
                .when().post(CurrencyEndpoint.server + CurrencyEndpoint.findRates)
                .then().assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .spec(TestSpecification.buildResponseSpec());

    }

    @Test
    void Exchange_Currency_Negative_Currency_Amount_Return_BAD_REQUEST() {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR", -5.0);

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .with().body(request)
                .when().post(CurrencyEndpoint.server + CurrencyEndpoint.exchangeCurrency)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .spec(TestSpecification.buildResponseSpec());
    }

    @Test
    void Exchange_Currency_Empty_Code_From_Return_BAD_REQUEST() {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("", "EUR", 5.0);

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .with().body(request)
                .when().post(CurrencyEndpoint.server + CurrencyEndpoint.exchangeCurrency)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .spec(TestSpecification.buildResponseSpec());
    }

    @Test
    void Exchange_Currency_Empty_Code_To_Return_BAD_REQUEST() {

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "", 5.0);

        given()
                .spec(TestSpecification.buildRequestSpec())
                .contentType(ContentType.JSON)
                .with().body(request)
                .when().post(CurrencyEndpoint.server + CurrencyEndpoint.exchangeCurrency)
                .then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .spec(TestSpecification.buildResponseSpec());
    }


}
