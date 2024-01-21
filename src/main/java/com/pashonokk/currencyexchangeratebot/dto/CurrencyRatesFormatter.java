package com.pashonokk.currencyexchangeratebot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CurrencyRatesFormatter {
    private final List<SpecificBankResponseExchangeCurrencyRate> rates = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Exchange Rates:\n");

        for (SpecificBankResponseExchangeCurrencyRate rate : rates) {
            String bankName = rate.getBankName();

            sb.append(bankName).append(": ");
            sb.append("Currency Pair: ").append(rate.getCcy()).append("/").append(rate.getBaseCcy());
            sb.append("\nBuy Rate: ").append(rate.getRateBuy()).append(", ");
            sb.append("Sell Rate: ").append(rate.getRateSell()).append("\n");
            sb.append("--------------------------------------------------------------\n");
        }

        return sb.toString();
    }
}
