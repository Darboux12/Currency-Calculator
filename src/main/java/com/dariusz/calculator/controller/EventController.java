package com.dariusz.calculator.controller;

import com.dariusz.calculator.constant.CurrencyEndpoint;
import com.dariusz.calculator.constant.EventEndpoint;
import com.dariusz.calculator.dto.response.CurrencyResponse;
import com.dariusz.calculator.dto.response.EventResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(EventEndpoint.server)
public interface EventController {

    @GetMapping(EventEndpoint.findAllEvent)
    ResponseEntity<Iterable<EventResponse>> findAllEvent();


}
