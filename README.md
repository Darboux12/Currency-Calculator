# Currency Calculator

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Example usage](#example-usage)

## General info

### Main goal

Currency calculator gives you opportunity to exchange given amount of money from one currency to another. Project uses NBP API to extract currency rates and perform calculations.

### Available currencies 

Currently 8 currencies are available:
* Swiss Franc (CHF)
* United States Dollar (USD)
* Euro (EUR)
* Pound Sterling (GBP)
* Czech Koruna (CZK)
* Polish Zloty (PLN)
* Forint (HUF)
* Norwegian Krone (NOK)

## Technologies

Project is developed with:
* Java
* Spring Boot
* Maven
* Hibernate
* Docker
* Flyway

## Setup

To run this project, install it locally using commands:

```
$ mvn clean install
$ docker-compose up
```

To start and stop containers use:

```
$ docker start
$ docker stop
```

To remove containers use:

```
$ docker-compose down
```

## Example usage

To find all available currencies use browser or send request with curl:

```
$ curl localhost:8080/currency/find/all
```

To find all available currency rates use browser or send request with curl:

```
$ curl -X POST localhost:8080/currency/find/rates
```

Example request body:

```
{"currencyCodes" : ["USD","PLN","CHF"]}
```


To exchange currency from USD to EUR use Postman or send request with curl :

```
$ curl -X POST localhost:8080/currency/exchange
```

Example request body:

```
{
"currencyCodeFrom" : "USD",
"currencyCodeTo" : "EUR",
"amount" : 5.20
}
```

To get events created by your requests use browser or send request with curl:

```
$ curl localhost:8080/events/find/all
```

