package com.dariusz.calculator.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesRequest {

    @NotNull(message = "Currency codes list cannot be empty")
    private List<String> currencyCodes;
}
