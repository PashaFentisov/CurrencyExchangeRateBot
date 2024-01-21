package com.pashonokk.currencyexchangeratebot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageSender {
    void sendMessage(Message message);
}
