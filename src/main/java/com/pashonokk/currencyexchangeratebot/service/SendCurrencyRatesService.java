package com.pashonokk.currencyexchangeratebot.service;

public interface SendCurrencyRatesService {
    void sendCurrencyRatesToUser(Long chatId, String currencyName);
}
