package com.portfolio.model.security;

public abstract class Security {
    private String ticker;

    public Security(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    public abstract double calculateMarketValue(double price, int quantity);
}