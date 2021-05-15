package com.dariusz.calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CurrencyExceptionResponse {
    private String title;
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
