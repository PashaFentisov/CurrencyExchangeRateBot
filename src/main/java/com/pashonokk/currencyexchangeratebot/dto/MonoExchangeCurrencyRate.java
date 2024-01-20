package com.pashonokk.currencyexchangeratebot.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class MonoExchangeCurrencyRate {
    private String currencyCodeA;
    private String currencyCodeB;
    private OffsetDateTime date;
    private double rateSell;
    private double rateBuy;
    private double rateCross;
}
