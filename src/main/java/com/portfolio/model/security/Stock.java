package com.portfolio.model.security;

public class Stock extends Security {
    private double expectedReturn;
    private double volatility;
    private double currentPrice;

    public Stock(String ticker, double expectedReturn, double volatility, double initialPrice) {
        super(ticker);
        this.expectedReturn = expectedReturn;
        this.volatility = volatility;
        this.currentPrice = initialPrice;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public double getVolatility() {
        return volatility;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public double calculateMarketValue(double price, int quantity) {
        return price * quantity;
    }
}
