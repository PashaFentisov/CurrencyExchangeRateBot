package com.pashonokk.currencyexchangeratebot.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class AllBanksCurrencyRates {
    private final Map<String, SpecificBankResponseExchangeCurrencyRate> rates = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Exchange Rates:\n");

        for (Map.Entry<String, SpecificBankResponseExchangeCurrencyRate> entry : rates.entrySet()) {
            String bankName = entry.getKey();
            SpecificBankResponseExchangeCurrencyRate bankRate = entry.getValue();

            sb.append(bankName).append(": ");
            sb.append("Currency Pair: ").append(bankRate.getCcy()).append("/").append(bankRate.getBaseCcy()).append(", ");
            sb.append("Buy Rate: ").append(bankRate.getRateBuy()).append(", ");
            sb.append("Sell Rate: ").append(bankRate.getRateSell()).append("\n");
        }

        return sb.toString();
    }
}
