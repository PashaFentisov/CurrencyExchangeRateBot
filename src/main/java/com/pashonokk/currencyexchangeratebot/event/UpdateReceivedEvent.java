package com.pashonokk.currencyexchangeratebot.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@AllArgsConstructor
public class UpdateReceivedEvent {
    private Update update;
}
