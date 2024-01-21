package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.TelegramBot;
import com.pashonokk.currencyexchangeratebot.dto.AllBanksCurrencyRates;
import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.service.SendCurrencyRatesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SendCurrencyRatesServiceImpl implements SendCurrencyRatesService {
    private final RateInUaCurrencyService rateInUaCurrencyService;
    private final TelegramBot telegramBot;

    @SneakyThrows
    @Override
    public void sendCurrencyRatesToUser(Long chatId, String currencyName) {

        List<SpecificBankResponseExchangeCurrencyRate> rateInUaCurrencyRates = rateInUaCurrencyService
                .requestExchangeCurrencyRate(chatId, currencyName);

        AllBanksCurrencyRates allBanksCurrencyRates = new AllBanksCurrencyRates();
        allBanksCurrencyRates.getRates().addAll(rateInUaCurrencyRates);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(allBanksCurrencyRates.toString());
        telegramBot.execute(sendMessage);
    }
}
