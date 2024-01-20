package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.TelegramBot;
import com.pashonokk.currencyexchangeratebot.dto.AllBanksCurrencyRates;
import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendCurrencyRatesService {
    private final CurrencyService monoCurrencyService;
    private final CurrencyService privatCurrencyService;
    private final TelegramBot telegramBot;

    @SneakyThrows
    public void sendNotification(Long chatId, String currencyName) {  //todo later can adjust strategy pattern
        SpecificBankResponseExchangeCurrencyRate responseMonoExchangeCurrencyRates = monoCurrencyService
                .requestExchangeCurrencyRate(chatId, currencyName);
        SpecificBankResponseExchangeCurrencyRate responsePrivatExchangeCurrencyRates = privatCurrencyService
                .requestExchangeCurrencyRate(chatId, currencyName);

        AllBanksCurrencyRates allBanksCurrencyRates = new AllBanksCurrencyRates();
        allBanksCurrencyRates.getRates().put("MonoBank", responseMonoExchangeCurrencyRates);
        allBanksCurrencyRates.getRates().put("PrivatBank", responsePrivatExchangeCurrencyRates);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(allBanksCurrencyRates.toString());
        telegramBot.execute(sendMessage);
    }
}
