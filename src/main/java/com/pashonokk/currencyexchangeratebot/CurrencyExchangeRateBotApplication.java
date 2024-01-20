package com.pashonokk.currencyexchangeratebot;

import com.pashonokk.currencyexchangeratebot.service.CurrencyService;
import com.pashonokk.currencyexchangeratebot.service.impl.MonoCurrencyService;
import com.pashonokk.currencyexchangeratebot.service.impl.PrivatCurrencyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableAsync
public class CurrencyExchangeRateBotApplication {

    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext run = SpringApplication.run(CurrencyExchangeRateBotApplication.class, args);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(run.getBean(TelegramBot.class));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
