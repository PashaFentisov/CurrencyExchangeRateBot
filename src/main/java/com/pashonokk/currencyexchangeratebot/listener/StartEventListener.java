package com.pashonokk.currencyexchangeratebot.listener;

import com.pashonokk.currencyexchangeratebot.event.StartEvent;
import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import com.pashonokk.currencyexchangeratebot.service.impl.SendCurrencyRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartEventListener {
    private final SendCurrencyRatesService sendCurrencyRatesService;

    @EventListener
    public void sendNotification(StartEvent event) {
        sendCurrencyRatesService.sendNotification(event.getChatId(), event.getCurrencyName());
    }
}
