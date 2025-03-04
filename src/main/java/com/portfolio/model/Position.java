package com.portfolio.model;

import com.portfolio.model.security.Security;

public class Position {

    private Security security;
    private int quantity;
    private double marketValue;

    public Position(Security security, int quantity) {
        this.security = security;
        this.quantity = quantity;
        this.marketValue = 0.0;
    }

    public Security getSecurity() {
        return security;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public String getSymbol() {
        return security.getTicker();
    }

    @Override
    public String toString() {
        return String.format("Position{symbol=%s, quantity=%d, marketValue=%.2f}",
                security.getTicker(), quantity, marketValue);
    }
}
