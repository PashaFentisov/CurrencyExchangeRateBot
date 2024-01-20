package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.dto.PrivatExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.exception.CurrencyIsNotSupported;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Validated
public class PrivatCurrencyService implements CurrencyService {
    private final RestTemplate restTemplate;
    private static final String CURRENCY_EXCEPTION_MESSAGE = "Currency %s is not supported";


    @SneakyThrows
    @Override
    public SpecificBankResponseExchangeCurrencyRate requestExchangeCurrencyRate(Long chatId, String currencyName) {
        ParameterizedTypeReference<List<PrivatExchangeCurrencyRate>> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<PrivatExchangeCurrencyRate>> exchangeCurrencyRateResponseEntity = restTemplate.exchange(
                "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5",
                HttpMethod.GET,
                null,
                responseType
        );
        PrivatExchangeCurrencyRate privatExchangeCurrencyRate =
                filterNeededCurrency(Objects.requireNonNull(exchangeCurrencyRateResponseEntity.getBody()),
                                     currencyName);
        return mapToResponse(privatExchangeCurrencyRate);
    }

    public SpecificBankResponseExchangeCurrencyRate mapToResponse(PrivatExchangeCurrencyRate privateExchangeCurrencyRate) {
        return SpecificBankResponseExchangeCurrencyRate.builder()
                .ccy(privateExchangeCurrencyRate.getCcy())
                .baseCcy(privateExchangeCurrencyRate.getBase_ccy())
                .rateBuy(String.format("%.4f", privateExchangeCurrencyRate.getBuy()))
                .rateSell(String.format("%.4f", privateExchangeCurrencyRate.getSale()))
                .build();
    }

    public PrivatExchangeCurrencyRate filterNeededCurrency(List<PrivatExchangeCurrencyRate> privatApiExchangeCurrencyRate, String currencyName) {
        return privatApiExchangeCurrencyRate.stream()
                .filter(privatExchangeCurrencyRate ->
                        (privatExchangeCurrencyRate.getCcy().equals(currencyName.toUpperCase())))
                .findFirst()
                .orElseThrow(() -> new CurrencyIsNotSupported(
                        String.format(CURRENCY_EXCEPTION_MESSAGE, currencyName)));
    }
}
