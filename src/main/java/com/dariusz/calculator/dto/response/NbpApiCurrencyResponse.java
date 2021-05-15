package com.dariusz.calculator.dto.response;

import com.dariusz.calculator.dto.other.NbpApiCurrencyRate;
import lombok.Data;

import java.util.List;

@Data
public class NbpApiCurrencyResponse {
    private String table;
    private String currency;
    private String code;
    private List<NbpApiCurrencyRate> rates;
}
