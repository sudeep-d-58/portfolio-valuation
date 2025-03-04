package com.portfolio.model.security;

import java.time.LocalDate;

public class PutOption extends Option {

    public PutOption(String ticker, double strike, LocalDate maturity, String underlyingTicker) {
        super(ticker, strike, maturity, underlyingTicker);
    }

    @Override
    public double calculatePrice(double underlyingPrice, double riskFreeRate, double volatility) {
        double timeToMaturity = getTimeToMaturity();

        if (timeToMaturity <= 0) {
            return Math.max(0, getStrike() - underlyingPrice);
        }

        double d1 = (Math.log(underlyingPrice / getStrike()) + (riskFreeRate + 0.5 * volatility * volatility) * timeToMaturity) / (volatility * Math.sqrt(timeToMaturity));
        double d2 = d1 - volatility * Math.sqrt(timeToMaturity);

        return getStrike() * Math.exp(-riskFreeRate * timeToMaturity) * normalCDF(-d2) - underlyingPrice * normalCDF(-d1);
    }

    private double normalCDF(double x) {
        // Approximation of the cumulative normal distribution function
        return 0.5 * (1 + Math.signum(x) * (1 - Math.exp(-Math.sqrt(2 / Math.PI) * Math.abs(x) *
                (0.31938153 + Math.abs(x) * (-0.356563782 + Math.abs(x) * (1.781477937 + Math.abs(x) *
                        (-1.821255978 + Math.abs(x) * 1.330274429)))))));
    }
}
