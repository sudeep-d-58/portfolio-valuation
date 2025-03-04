// MarketDataService.java
package com.portfolio.service;

import com.portfolio.model.security.Stock;
import com.portfolio.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MarketDataService {
    private final SecurityRepository securityRepository;
    private final Map<String, Stock> stocks;
    private final Random random;
    private final ScheduledExecutorService scheduler;
    private final List<MarketDataListener> listeners;

    public interface MarketDataListener {
        void onPriceUpdate(String ticker, double price);
    }

    @Autowired
    public MarketDataService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
        this.stocks = new ConcurrentHashMap<>();
        this.random = new Random();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.listeners = new java.util.concurrent.CopyOnWriteArrayList<>();

        // Initialize stocks
        List<Stock> stockList = securityRepository.getAllSecurities().stream()
                .filter(s -> s instanceof Stock)
                .map(s -> (Stock) s)
                .collect(Collectors.toList());

        for (Stock stock : stockList) {
            stocks.put(stock.getTicker(), stock);
        }
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::updatePrices, 0, 500, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }

    private void updatePrices() {
        for (Stock stock : stocks.values()) {
            double currentPrice = stock.getCurrentPrice();
            double expectedReturn = stock.getExpectedReturn();
            double volatility = stock.getVolatility();

            // Geometric Brownian Motion formula
            double dt = (0.5 + random.nextDouble() * 1.5) / (24 * 60 * 60); // Random time between 0.5-2 seconds converted to years
            double epsilon = random.nextGaussian(); // Random normal variable

            double deltaS = currentPrice * (expectedReturn * dt + volatility * Math.sqrt(dt) * epsilon);
            double newPrice = Math.max(0.01, currentPrice + deltaS); // Ensure price doesn't go below 0.01

            stock.setCurrentPrice(newPrice);

            // Notify listeners
            for (MarketDataListener listener : listeners) {
                listener.onPriceUpdate(stock.getTicker(), newPrice);
            }
        }
    }

    public void addListener(MarketDataListener listener) {
        listeners.add(listener);
    }

    public double getPrice(String ticker) {
        Stock stock = stocks.get(ticker);
        return stock != null ? stock.getCurrentPrice() : 0.0;
    }
}