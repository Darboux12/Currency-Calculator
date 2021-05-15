package com.dariusz.calculator.service.standard;

import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import com.dariusz.calculator.dto.response.CurrencyExchangeResponse;
import com.dariusz.calculator.service.CurrencyValidityService;
import com.dariusz.calculator.service.exception.CurrencyAmountNotPositiveException;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class StandardCurrencyValidityService implements CurrencyValidityService {

    @Override
    public void validateCurrencyAvailability(Set<String> currencyCodes) throws CurrencyNotAvailableException {

        for(String currencyCode : currencyCodes)
            if(!this.isCurrencyCodeAvailable(currencyCode))
                throw new CurrencyNotAvailableException(currencyCode);

    }

    @Override
    public void validateCurrencyAmount(double currencyAmount) throws CurrencyAmountNotPositiveException {

        if(!this.isCurrencyAmountValid(currencyAmount))
            throw new CurrencyAmountNotPositiveException();

    }

    public void validateCurrencyExchangeRequest(CurrencyExchangeRequest request) throws CurrencyNotAvailableException, CurrencyAmountNotPositiveException {

        this.validateCurrencyAvailability(Set.of(request.getCurrencyCodeFrom(),request.getCurrencyCodeTo()));
        this.validateCurrencyAmount(request.getAmount());

    }

    private boolean isCurrencyCodeAvailable(String currencyCode) {
        return Arrays.stream(Currency.values()).anyMatch(x -> x.getCode().equals(currencyCode));
    }

    private boolean isCurrencyAmountValid(double amount){
        return amount >= 0;
    }


}
