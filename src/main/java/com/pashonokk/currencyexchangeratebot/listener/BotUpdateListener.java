package com.pashonokk.currencyexchangeratebot.listener;

import com.pashonokk.currencyexchangeratebot.event.UpdateReceivedEvent;
import com.pashonokk.currencyexchangeratebot.service.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotUpdateListener {
    private final MessageSender messageSender;

    @EventListener
    public void sendNotification(UpdateReceivedEvent event) {
        messageSender.sendMessage(event.getUpdate().getMessage());
    }
}
