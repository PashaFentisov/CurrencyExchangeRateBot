package com.pashonokk.currencyexchangeratebot.dto.rateInApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.GET;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rate{

    private OffsetDateTime updateTime;

    private CurrencyRate usd;

    private CurrencyRate eur;

    private CurrencyRate gbp;

    private CurrencyRate pln;

}