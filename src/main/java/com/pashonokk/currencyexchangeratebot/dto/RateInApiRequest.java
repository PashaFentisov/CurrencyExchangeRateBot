package com.pashonokk.currencyexchangeratebot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateInApiRequest {
    private Map<String, Exchanger> exchangers;
}