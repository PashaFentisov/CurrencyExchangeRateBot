package com.pashonokk.currencyexchangeratebot.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@ToString
public class PrivatExchangeCurrencyRate{
    private String ccy;
    private String base_ccy;
    private double buy;
    private double sale;
}
