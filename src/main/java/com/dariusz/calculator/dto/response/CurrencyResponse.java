package com.dariusz.calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyResponse {
    private String name;
    private String code;
}
