package com.dariusz.calculator.dto.response;

import com.dariusz.calculator.dto.request.CurrencyExchangeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyExchangeResponse {
    private CurrencyExchangeRequest request;
    private double result;
}
