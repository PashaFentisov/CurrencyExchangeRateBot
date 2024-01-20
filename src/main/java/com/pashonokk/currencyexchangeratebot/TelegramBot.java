package com.pashonokk.currencyexchangeratebot;

import com.pashonokk.currencyexchangeratebot.event.StartEvent;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import com.pashonokk.currencyexchangeratebot.service.impl.MonoCurrencyService;
import com.pashonokk.currencyexchangeratebot.util.TelegramProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramProperties telegramProperties;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramProperties.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText().startsWith("/start")){
            applicationEventPublisher.publishEvent(new StartEvent(update.getMessage().getChatId(), update.getMessage().getText()));
        }else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            String message = """
                    Hello, I am a bot that will help you find out the exchange rate of the currency in the PrivatBank and MonoBank.
                    To start, enter the name of the currency you are interested in.
                    Here are currencies that I can work with:
                    USD, EUR(you can ignore case)
                    """;
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

