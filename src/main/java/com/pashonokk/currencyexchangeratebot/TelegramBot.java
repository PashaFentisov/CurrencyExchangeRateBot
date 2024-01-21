package com.pashonokk.currencyexchangeratebot;

import com.pashonokk.currencyexchangeratebot.event.UpdateReceivedEvent;
import com.pashonokk.currencyexchangeratebot.util.TelegramProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;

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

    @SneakyThrows
    @PostConstruct
    public void initMenu() {
        List<BotCommand> botCommands = List.of(
                new BotCommand("/start", "start bot"),
                new BotCommand("/info", "info about bot possibilities")
        );
        this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
    }


    @Override
    public void onUpdateReceived(Update update) {
        applicationEventPublisher.publishEvent(new UpdateReceivedEvent(update));
    }
}

