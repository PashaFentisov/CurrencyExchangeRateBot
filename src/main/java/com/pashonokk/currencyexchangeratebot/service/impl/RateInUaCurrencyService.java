package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.dto.*;
import com.pashonokk.currencyexchangeratebot.exception.CurrencyIsNotSupportedException;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RateInUaCurrencyService implements CurrencyService {
    private final RestTemplate restTemplate;
    private final RateInCurrencyCache rateInCurrencyCache = new RateInCurrencyCache();

    @Override
    public List<SpecificBankResponseExchangeCurrencyRate> requestExchangeCurrencyRate(Long chatId, String currencyName) {
        ResponseEntity<RateInApiRequest> apiRequestResponseEntity = restTemplate
                .getForEntity("https://rate.in.ua/api/service/banks-of-ukraine", RateInApiRequest.class);
        if (rateInCurrencyCache.getRateInApiRequest() != null &&
                Duration.between(rateInCurrencyCache.getRequestTime(), java.time.LocalDateTime.now()).toMinutes() < 10) {
            return mapToResponse(Objects.requireNonNull(rateInCurrencyCache.getRateInApiRequest()), currencyName);
        }
        rateInCurrencyCache.setCache(apiRequestResponseEntity.getBody());
        return mapToResponse(Objects.requireNonNull(apiRequestResponseEntity.getBody()), currencyName);
    }


    /**
     * We filter OTP bank because it doesn't have the latest rates
     */
    private List<SpecificBankResponseExchangeCurrencyRate> mapToResponse(RateInApiRequest rateInApiRequest, String currencyName) {
        return rateInApiRequest.getExchangers()
                .values()
                .stream()
                .filter(exchanger -> !exchanger.getName().equalsIgnoreCase("otp bank"))
                .map(exchanger -> mapToSpecificBankResponse(exchanger, currencyName))
                .sorted(Comparator.comparing(SpecificBankResponseExchangeCurrencyRate::getRateSell)
                        .thenComparing(SpecificBankResponseExchangeCurrencyRate::getRateBuy, Comparator.reverseOrder()))
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
