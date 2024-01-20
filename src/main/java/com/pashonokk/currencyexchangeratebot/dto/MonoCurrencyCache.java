package com.pashonokk.currencyexchangeratebot.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MonoCurrencyCache {
    private LocalDateTime requestTime;
    private List<MonoExchangeCurrencyRate> monoExchangeCurrencyRateList;

    public void setCache(List<MonoExchangeCurrencyRate> monoExchangeCurrencyRateList) {
        this.monoExchangeCurrencyRateList = monoExchangeCurrencyRateList;
        this.requestTime = LocalDateTime.now();
    }
}
