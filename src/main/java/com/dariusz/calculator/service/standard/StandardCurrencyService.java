package com.dariusz.calculator.service.standard;

import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dal.repository.CurrencyRepository;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.NbpApiCurrencyResponse;
import com.dariusz.calculator.service.CurrencyService;
import com.dariusz.calculator.service.CurrencyValidityService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StandardCurrencyService implements CurrencyService {

    private final CurrencyValidityService currencyValidityService;
    private final CurrencyRepository currencyRepository;

    public StandardCurrencyService(CurrencyValidityService currencyValidityService, CurrencyRepository currencyRepository) {
        this.currencyValidityService = currencyValidityService;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Iterable<Currency> findAllAvailableCurrency() {
        return currencyRepository.findAllAvailableCurrency();
    }

    @Override
    public CurrencyExchangeResponse exchangeCurrency(CurrencyExchangeRequest exchangeRequest) throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        this.currencyValidityService.validateCurrencyExchangeRequest(exchangeRequest);

        NbpApiCurrencyResponse currencyFrom = this.findCurrencyFromNbpApi(exchangeRequest.getCurrencyCodeFrom());

        NbpApiCurrencyResponse currencyTo = this.findCurrencyFromNbpApi(exchangeRequest.getCurrencyCodeTo());

        double pln = currencyFrom.getRates().get(0).getMid() * exchangeRequest.getAmount();

        double output = pln / currencyTo.getRates().get(0).getMid();

        DecimalFormat df = new DecimalFormat("#.####");

        df.setRoundingMode(RoundingMode.CEILING);

        return new CurrencyExchangeResponse(exchangeRequest,Double.valueOf(df.format(output)));

    }

    @Override
    public Iterable<CurrencyRatesResponse> findCurrencyRates(CurrencyRatesRequest currencyRatesRequest) throws CurrencyNotAvailableException {

        this.currencyValidityService.validateCurrencyAvailability(currencyRatesRequest.getCurrencyCodes());

        List<CurrencyRatesResponse> currencyRatesResponses = new ArrayList<>();

        currencyRatesRequest.getCurrencyCodes().forEach(currencyCode -> {

            try {

                NbpApiCurrencyResponse nbpApiCurrencyResponse = this.findCurrencyFromNbpApi(currencyCode);

                CurrencyRatesResponse currencyRatesResponse = this.convertNbpApiResponseToCurrencyRatesResponse(nbpApiCurrencyResponse);

                currencyRatesResponses.add(currencyRatesResponse);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        });

        return currencyRatesResponses;

    }

    private NbpApiCurrencyResponse findCurrencyFromNbpApi(String currencyCode) throws IOException, InterruptedException {

        return new ObjectMapper().readValue(this.performNbpApiRequest(currencyCode).body(),NbpApiCurrencyResponse.class);

    }

    private HttpResponse<String> performNbpApiRequest(String currencyCode) throws IOException, InterruptedException {

        String API_PATH = "http://api.nbp.pl/api/exchangerates/rates/a/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_PATH + currencyCode))
                .header("Accept","application/json")
                .GET().build();

        return HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

    }

    private CurrencyRatesResponse convertNbpApiResponseToCurrencyRatesResponse(NbpApiCurrencyResponse response){

        return new CurrencyRatesResponse(
                response.getCurrency(),
                response.getCode(),
                response.getRates().get(0).getMid()
        );

    }

}

