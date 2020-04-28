package com.timmytime.predictor.lambda.response;

import com.timmytime.predictor.lambda.enumerator.BetProvider;

import java.io.Serializable;
import java.util.UUID;

public class Bet implements Serializable {

    private Double price;
    private BetProvider betProvider;
    private UUID betId;
    private String details;

    public Bet(UUID betId, BetProvider betProvider, Double price, String details) {
        this.betId = betId;
        this.betProvider = betProvider;
        this.price = price;
        this.details = details;
    }

    public UUID getBetId() {
        return betId;
    }

    public void setBetId(UUID betId) {
        this.betId = betId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BetProvider getBetProvider() {
        return betProvider;
    }

    public void setBetProvider(BetProvider betProvider) {
        this.betProvider = betProvider;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}
