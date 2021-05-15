package com.dariusz.calculator.dal.repository.standard;

import com.dariusz.calculator.dal.entity.Currency;
import com.dariusz.calculator.dal.repository.CurrencyRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class StandardCurrencyRepository implements CurrencyRepository {

    @Override
    public List<Currency> findAllAvailableCurrency() {
        return Arrays.asList(Currency.values());
    }

    @Override
    public Currency findBaseCurrency() {
        return Currency.PLN;
    }

}
