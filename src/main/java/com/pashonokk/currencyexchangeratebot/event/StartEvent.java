package com.pashonokk.currencyexchangeratebot.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartEvent {
    private Long chatId;
    private String currencyName;
}
