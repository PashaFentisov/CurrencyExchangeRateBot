package com.pashonokk.currencyexchangeratebot.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "bot")
@Data
@Configuration
public class TelegramProperties {
    private String username;
    private String token;
}
