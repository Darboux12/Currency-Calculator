package com.dariusz.calculator.controller.exception;

import com.dariusz.calculator.dto.response.CurrencyExceptionResponse;
import com.dariusz.calculator.service.exception.CurrencyNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class PodiumControllerAdvice {

    @ExceptionHandler(CurrencyNotAvailableException.class)
    public ResponseEntity<CurrencyExceptionResponse> handleCurrencyNotAvailableException(CurrencyNotAvailableException e, WebRequest request) {

        return new ResponseEntity<>(
                this.createResponseMessage("Currency Not Available", HttpStatus.CONFLICT,
                        e.getMessage(),request.getDescription(false)), HttpStatus.CONFLICT
        );

    }

    private CurrencyExceptionResponse createResponseMessage(String title, HttpStatus status,String message, String description) {

        return new CurrencyExceptionResponse(
                title,
                status.value(),
                new Date(),
                message,
                description
        );
    }




}
