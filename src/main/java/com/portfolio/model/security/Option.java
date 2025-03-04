package com.portfolio.model.security;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Option extends Security {
    private double strike;
    private LocalDate maturity;
    private String underlyingTicker;

    public Option(String ticker, double strike, LocalDate maturity, String underlyingTicker) {
        super(ticker);
        this.strike = strike;
        this.maturity = maturity;
        this.underlyingTicker = underlyingTicker;
    }

    public double getStrike() {
        return strike;
    }

    public LocalDate getMaturity() {
        return maturity;
    }

    public String getUnderlyingTicker() {
        return underlyingTicker;
    }

    public double getTimeToMaturity() {
        // Calculate time to maturity in years
        return ChronoUnit.DAYS.between(LocalDate.now(), maturity) / 365.0;
    }

    @Override
    public double calculateMarketValue(double price, int quantity) {
        return price * quantity;
    }

    public abstract double calculatePrice(double underlyingPrice, double riskFreeRate, double volatility);
}
