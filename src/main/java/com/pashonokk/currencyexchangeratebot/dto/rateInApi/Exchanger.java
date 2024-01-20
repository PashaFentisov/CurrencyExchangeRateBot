package com.pashonokk.currencyexchangeratebot.dto.rateInApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exchanger {
    private String name;

    private Rate rates;
}
