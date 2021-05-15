package com.dariusz.calculator.dto.other;

import lombok.Data;

import java.util.Date;

@Data
public class NbpApiCurrencyRate {
    private String no;
    private Date effectiveDate;
    private double mid;
}
