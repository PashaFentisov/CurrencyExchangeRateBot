package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.dto.rateInApi.ApiRequest;
import com.pashonokk.currencyexchangeratebot.dto.rateInApi.CurrencyRate;
import com.pashonokk.currencyexchangeratebot.dto.rateInApi.Exchanger;
import com.pashonokk.currencyexchangeratebot.exception.CurrencyIsNotSupportedException;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RateInUaCurrencyService implements CurrencyService {
    private final RestTemplate restTemplate;

    @Override
    public List<SpecificBankResponseExchangeCurrencyRate> requestExchangeCurrencyRate(Long chatId, String currencyName) {
        ResponseEntity<ApiRequest> apiRequestResponseEntity = restTemplate
                .getForEntity("https://rate.in.ua/api/service/banks-of-ukraine", ApiRequest.class);

        return mapToResponse(Objects.requireNonNull(apiRequestResponseEntity.getBody()), currencyName); //todo add cache
    }

    private List<SpecificBankResponseExchangeCurrencyRate> mapToResponse(ApiRequest apiRequest, String currencyName) {
        return apiRequest.getExchangers()
                .values()
                .stream()
                .filter(exchanger -> !exchanger.getName().equalsIgnoreCase("monobank"))
                .map(exchanger -> mapToSpecificBankResponse(exchanger, currencyName))
                .toList();
    }

    private SpecificBankResponseExchangeCurrencyRate mapToSpecificBankResponse(Exchanger exchanger, String currencyName) {
        CurrencyRate currencyRate = getCurrencyRate(exchanger, currencyName);

        return SpecificBankResponseExchangeCurrencyRate.builder()
                .bankName(exchanger.getName())
                .ccy(currencyName.toUpperCase())
                .baseCcy("UAH")
                .rateBuy(currencyRate.getBuy())
                .rateSell(currencyRate.getSel())
                .build();
    }

    private CurrencyRate getCurrencyRate(Exchanger exchanger, String currencyName) {
        return switch (currencyName.toUpperCase()) {
            case "USD" -> exchanger.getRates().getUsd();
            case "EUR" -> exchanger.getRates().getEur();
            case "GBP" -> exchanger.getRates().getGbp();
            case "PLN" -> exchanger.getRates().getPln();
            default -> throw new CurrencyIsNotSupportedException("Currency " + currencyName + " is not supported");
        };
    }
}
