package com.dariusz.calculator.service.standard;

import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dal.repository.CurrencyRepository;
import com.dariusz.calculator.dto.other.NbpCurrencyDto;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.request.CurrencyRatesRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.dto.response.CurrencyRatesResponse;
import com.dariusz.calculator.dto.response.NbpApiCurrencyResponse;
import com.dariusz.calculator.service.CurrencyService;
import com.dariusz.calculator.service.CurrencyValidityService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import com.dariusz.calculator.service.exception.RateNotPresentException;
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
    public CurrencyExchangeResponse exchangeCurrency(CurrencyExchangeRequest exchangeRequest) throws IOException, InterruptedException, CurrencyNotAvailableException, CurrencyAmountNotPositiveException, RateNotPresentException {

        this.currencyValidityService.validateCurrencyExchangeRequest(exchangeRequest);

        NbpCurrencyDto from = this.getNbpCurrencyDtoFromCurrencyCode(exchangeRequest.getCurrencyCodeFrom());

        NbpCurrencyDto to = this.getNbpCurrencyDtoFromCurrencyCode(exchangeRequest.getCurrencyCodeTo());

        return new CurrencyExchangeResponse(exchangeRequest,this.countCurrencyExchange(from,to,exchangeRequest.getAmount()));

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

    private NbpCurrencyDto getNbpCurrencyDtoFromCurrencyCode(String currencyCode) throws IOException, InterruptedException, RateNotPresentException {

        return this.isBaseCurrency(currencyCode)

                ? new NbpCurrencyDto(this.currencyRepository.findBaseCurrency().getName(), this.currencyRepository.findBaseCurrency().getCode(),1)

                : this.convertNbpApiResponseToNbpCurrencyDto(this.findCurrencyFromNbpApi(currencyCode));

    }

    private NbpCurrencyDto convertNbpApiResponseToNbpCurrencyDto(NbpApiCurrencyResponse response) throws RateNotPresentException {

        this.currencyValidityService.validateRatePresence(response);

        return new NbpCurrencyDto(
                response.getCurrency(),
                response.getCode(),
                response.getRates().get(0).getMid()
        );

    }

    private boolean isBaseCurrency(String currencyCode){
        return currencyCode.equals("PLN");
    }

    private double countCurrencyExchange(NbpCurrencyDto currencyFrom, NbpCurrencyDto currencyTo, double amount){

        double pln = currencyFrom.getRate() * amount;

        double output = pln / currencyTo.getRate();

        DecimalFormat df = new DecimalFormat("#.####");

        df.setRoundingMode(RoundingMode.CEILING);

        return Double.valueOf(df.format(output));
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

