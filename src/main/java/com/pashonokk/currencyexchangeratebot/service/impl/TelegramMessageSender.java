package com.pashonokk.currencyexchangeratebot.service.impl;

import com.pashonokk.currencyexchangeratebot.TelegramBot;
import com.pashonokk.currencyexchangeratebot.dto.CurrencyRatesFormatter;
import com.pashonokk.currencyexchangeratebot.dto.SpecificBankResponseExchangeCurrencyRate;
import com.pashonokk.currencyexchangeratebot.exception.UnknownCommandException;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import com.pashonokk.currencyexchangeratebot.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramMessageSender implements MessageSender {
    private final TelegramBot telegramBot;
    private final CurrencyService rateInUaCurrencyService;
    private static final String START_RESPONSE = "Hello %s, I am a bot that will help you find out the exchange rate of the currency.";
    private static final String INFO_RESPONSE = """ 
            To start, choose the name of the currency you are interested in.
            Later you can choose another currency
            """;
    private static final String UNKNOWN_COMMAND_RESPONSE = "Command %s is unknown. Please, try again";


    @Override
    @Async
    public void sendMessage(Message message) {
        switch (message.getText()) {
            case "/start":
                sendGenericCommandResponse(List.of("/info"), 1, START_RESPONSE.formatted(message.getChat().getUserName()), message);
                break;
            case "/info":
                sendGenericCommandResponse(List.of("USD", "EUR", "GBP", "PLN"), 2, INFO_RESPONSE, message);
                break;
            case "USD", "EUR", "GBP", "PLN":
                sendCurrencyRates(message);
                break;
            default:
                handleUnknownCommand(message);
        }
    }

    @SneakyThrows
    private void sendGenericCommandResponse(List<String> commands, int rowsCount, String messageText, Message message) {
        ReplyKeyboardMarkup replyKeyboard = constructReplyKeyboard(commands, rowsCount);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(replyKeyboard);
        telegramBot.execute(sendMessage);
    }

    @SneakyThrows
    private void handleUnknownCommand(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(UNKNOWN_COMMAND_RESPONSE.formatted(message.getText()));
        telegramBot.execute(sendMessage);
        throw new UnknownCommandException(UNKNOWN_COMMAND_RESPONSE.formatted(message.getText()));
    }

    private static ReplyKeyboardMarkup constructReplyKeyboard(List<String> buttons, int rows) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        int buttonsPerRow = buttons.size() / rows;
        int remainder = buttons.size() % rows;

        for (int i = 0; i < buttons.size(); i++) {
            keyboardRow.add(buttons.get(i));

            if (keyboardRow.size() == buttonsPerRow || (i == buttons.size() - 1 && remainder > 0)) {
                keyboardRows.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }

        replyKeyboard.setKeyboard(keyboardRows);
        return replyKeyboard;
    }

    @SneakyThrows
    private void sendCurrencyRates(Message message) {
        List<SpecificBankResponseExchangeCurrencyRate> rateInUaCurrencyRates = rateInUaCurrencyService
                .requestExchangeCurrencyRate(message.getText());

        CurrencyRatesFormatter currencyRatesFormatter = new CurrencyRatesFormatter();
        currencyRatesFormatter.getRates().addAll(rateInUaCurrencyRates);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(currencyRatesFormatter.toString());
        telegramBot.execute(sendMessage);
    }
}
