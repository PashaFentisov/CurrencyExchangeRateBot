package com.pashonokk.currencyexchangeratebot.util;

import com.pashonokk.currencyexchangeratebot.exception.CurrencyIsNotSupportedException;
import lombok.Getter;

@Getter
public enum CurrencyIsoCode {
    USD("USD", 840),
    EUR("EUR", 978),
    UAH("UAH", 980),
    PLN("PLN", 985),
    GBP("GBP", 826);

    private final String name;
    private final int isoCode;

    CurrencyIsoCode(String name, int isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    public static String getCurrencyNameByIsoCode(String isoCode) {
        for (CurrencyIsoCode currency : CurrencyIsoCode.values()) {
            if (isoCode.equalsIgnoreCase(String.valueOf(currency.getIsoCode()))) {
                return currency.getName();
            }
        }
        throw new CurrencyIsNotSupportedException("Currency iso code" + isoCode + " is not supported");
    }

    public static Integer getIsoCodeByCurrencyName(String currencyName) {
        for (CurrencyIsoCode currency : CurrencyIsoCode.values()) {
            if (currency.getName().equals(currencyName)) {
                return currency.getIsoCode();
            }
        }
        throw new CurrencyIsNotSupportedException("Currency " + currencyName + " is not supported");
    }
}
