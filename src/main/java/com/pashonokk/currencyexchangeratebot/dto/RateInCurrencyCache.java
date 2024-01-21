package com.pashonokk.currencyexchangeratebot.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RateInCurrencyCache {
    private LocalDateTime requestTime;
    private RateInApiRequest rateInApiRequest;

    public void setCache(RateInApiRequest rateInApiRequest) {
        this.rateInApiRequest = rateInApiRequest;
        this.requestTime = LocalDateTime.now();
    }
}
