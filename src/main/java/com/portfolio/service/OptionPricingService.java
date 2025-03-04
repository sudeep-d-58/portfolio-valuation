// OptionPricingService.java
package com.portfolio.service;

import com.portfolio.model.security.Option;
import com.portfolio.model.security.Stock;
import com.portfolio.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionPricingService {
    private final SecurityRepository securityRepository;
    private final MarketDataService marketDataService;
    private final double riskFreeRate = 0.02; // 2% per annum

    @Autowired
    public OptionPricingService(SecurityRepository securityRepository, MarketDataService marketDataService) {
        this.securityRepository = securityRepository;
        this.marketDataService = marketDataService;
    }

    public double calculateOptionPrice(String optionTicker) {
        Option option = (Option) securityRepository.getSecurityByTicker(optionTicker);
        if (option == null) {
            return 0.0;
        }

        String underlyingTicker = option.getUnderlyingTicker();
        double underlyingPrice = marketDataService.getPrice(underlyingTicker);

        Stock underlyingStock = (Stock) securityRepository.getSecurityByTicker(underlyingTicker);
        double volatility = underlyingStock.getVolatility();

        return option.calculatePrice(underlyingPrice, riskFreeRate, volatility);
    }
}