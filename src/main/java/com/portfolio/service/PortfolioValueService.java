// PortfolioValueService.java
package com.portfolio.service;

import com.portfolio.model.Portfolio;
import com.portfolio.model.Position;
import com.portfolio.model.security.Option;
import com.portfolio.model.security.Security;
import com.portfolio.model.security.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PortfolioValueService implements MarketDataService.MarketDataListener {
    private final Portfolio portfolio;
    private final MarketDataService marketDataService;
    private final OptionPricingService optionPricingService;
    private final List<PortfolioValueListener> listeners;

    public interface PortfolioValueListener {
        void onValueUpdate(Portfolio portfolio);
    }

    @Autowired
    public PortfolioValueService(Portfolio portfolio, MarketDataService marketDataService,
                                 OptionPricingService optionPricingService) {
        this.portfolio = portfolio;
        this.marketDataService = marketDataService;
        this.optionPricingService = optionPricingService;
        this.listeners = new CopyOnWriteArrayList<>();

        marketDataService.addListener(this);
    }

    @Override
    public void onPriceUpdate(String ticker, double price) {
        // Update all positions that might be affected by this price update
        boolean updated = false;

        for (Position position : portfolio.getPositions().values()) {
            Security security = position.getSecurity();

            if (security instanceof Stock && security.getTicker().equals(ticker)) {
                // Direct stock position update
                double value = security.calculateMarketValue(price, position.getQuantity());
                position.setMarketValue(value);
                updated = true;
            } else if (security instanceof Option) {
                Option option = (Option) security;
                if (option.getUnderlyingTicker().equals(ticker)) {
                    // Option position that depends on this stock
                    double optionPrice = optionPricingService.calculateOptionPrice(option.getTicker());
                    double value = option.calculateMarketValue(optionPrice, position.getQuantity());
                    position.setMarketValue(value);
                    updated = true;
                }
            }
        }

        if (updated) {
            portfolio.updateNav();
            notifyListeners();
        }
    }

    public void addListener(PortfolioValueListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (PortfolioValueListener listener : listeners) {
            listener.onValueUpdate(portfolio);
        }
    }
}