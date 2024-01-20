package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.dto.MonoCurrencyCache;
import com.pashonokk.currencyexchangeratebot.dto.MonoExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.exception.CurrencyIsNotSupportedException;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import com.pashonokk.currencyexchangeratebot.util.CurrencyIsoCode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MonoCurrencyService implements CurrencyService {
    private final RestTemplate restTemplate;
    private static final String CURRENCY_EXCEPTION_MESSAGE = "Currency %s is not supported";
    private final MonoCurrencyCache monoCurrencyCache = new MonoCurrencyCache();


    @SneakyThrows
    @Override
    public List<SpecificBankResponseExchangeCurrencyRate> requestExchangeCurrencyRate(Long chatId, String currencyName) {
        if (monoCurrencyCache.getMonoExchangeCurrencyRateList() != null &&
                Duration.between(monoCurrencyCache.getRequestTime(), java.time.LocalDateTime.now()).toMinutes() < 5) {
            System.out.println("mono from cache"); //todo delete
            return mapToResponse(getNeededCurrency(monoCurrencyCache.getMonoExchangeCurrencyRateList(), currencyName));
        }
        ParameterizedTypeReference<List<MonoExchangeCurrencyRate>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<MonoExchangeCurrencyRate>> exchangeCurrencyRateResponseEntity = restTemplate.exchange( //todo робити запит на всі валюти якось не прікольно і додати кеш
                "https://api.monobank.ua/bank/currency",
                HttpMethod.GET,
                null,
                responseType
        );

        MonoExchangeCurrencyRate neededCurrency = getNeededCurrency(Objects.requireNonNull(exchangeCurrencyRateResponseEntity.getBody()), currencyName);
        List<SpecificBankResponseExchangeCurrencyRate> specificBankResponseExchangeCurrencyRate = mapToResponse(neededCurrency);
        monoCurrencyCache.setCache(exchangeCurrencyRateResponseEntity.getBody());
        return specificBankResponseExchangeCurrencyRate;
    }

    private MonoExchangeCurrencyRate getNeededCurrency(List<MonoExchangeCurrencyRate> monoExchangeCurrencyRateList, String currencyName) {
        return monoExchangeCurrencyRateList.stream()
                .filter(monoExchangeCurrencyRate -> {
                    String neededCode = String.valueOf(CurrencyIsoCode.getIsoCodeByCurrencyName(currencyName.toUpperCase()));
                    String currencyCodeA = monoExchangeCurrencyRate.getCurrencyCodeA();
                    String currencyCodeB = monoExchangeCurrencyRate.getCurrencyCodeB();
                    return (currencyCodeA.equals(neededCode)) && currencyCodeB.equals("980");
                })
                .findFirst()
                .orElseThrow(() -> new CurrencyIsNotSupportedException(
                        String.format(CURRENCY_EXCEPTION_MESSAGE, currencyName)));
    }


    private List<SpecificBankResponseExchangeCurrencyRate> mapToResponse(MonoExchangeCurrencyRate neededCurrencyRate) {
        double rateBuy = neededCurrencyRate.getRateBuy();
        double rateSell = neededCurrencyRate.getRateSell();
        if (rateBuy == 0.0) {
            rateBuy = neededCurrencyRate.getRateCross();
            rateSell = 1 / neededCurrencyRate.getRateCross();
        }
        return List.of(
                SpecificBankResponseExchangeCurrencyRate.builder()
                        .bankName("MonoBank")
                        .ccy(CurrencyIsoCode.getCurrencyNameByIsoCode(neededCurrencyRate.getCurrencyCodeA()))
                        .baseCcy("UAH")
                        .rateBuy(String.format("%.4f", rateBuy))
                        .rateSell(String.format("%.4f", rateSell))
                        .build());
    }

}
