// Main.java
package com.portfolio;

import com.portfolio.model.Portfolio;
import com.portfolio.service.MarketDataService;
import com.portfolio.service.PositionService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan("com.portfolio")
public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            // Load portfolio from CSV
            PositionService positionService = context.getBean(PositionService.class);
            Portfolio portfolio = positionService.loadPortfolioFromCsv("src/main/resources/positions.csv");

            // Start market data service
            MarketDataService marketDataService = context.getBean(MarketDataService.class);
            marketDataService.start();

            System.out.println("Portfolio system started. Press Ctrl+C to exit.");

            // Keep application running
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                marketDataService.stop();
                System.out.println("Application shutting down...");
            }
        } catch (IOException e) {
            System.err.println("Error loading portfolio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}