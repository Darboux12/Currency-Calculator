package com.dariusz.calculator.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class TestSpecification {

    private static RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    private static ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();

    public static RequestSpecification buildRequestSpec(){

        return requestSpecBuilder
                .log(LogDetail.URI)
                .log(LogDetail.BODY)
                .build();

    }

    public static ResponseSpecification buildResponseSpec(){

        return responseSpecBuilder

                .log(LogDetail.ALL)
                .build();


    }

}
