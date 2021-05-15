package com.dariusz.calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private Date date;
    private String localization;
    private String description;
}
