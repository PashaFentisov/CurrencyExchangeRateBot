package com.pashonokk.currencyexchangeratebot.service;

import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;

import java.util.List;

public interface CurrencyService{
    List<SpecificBankResponseExchangeCurrencyRate> requestExchangeCurrencyRate(String currencyRate);

}
