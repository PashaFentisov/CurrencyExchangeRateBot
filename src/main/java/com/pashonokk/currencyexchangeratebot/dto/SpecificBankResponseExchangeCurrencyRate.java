package com.pashonokk.currencyexchangeratebot.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SpecificBankResponseExchangeCurrencyRate {
    private String bankName;
    private String ccy;
    private String baseCcy;
    private String rateSell;
    private String rateBuy;
}
