// PortfolioResultSubscriber.java
package com.portfolio.subscriber;

import com.portfolio.model.Portfolio;
import com.portfolio.model.Position;
import com.portfolio.service.PortfolioValueService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PortfolioResultSubscriber implements PortfolioValueService.PortfolioValueListener {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public PortfolioResultSubscriber(PortfolioValueService portfolioValueService) {
        portfolioValueService.addListener(this);
    }

    @Override
    public void onValueUpdate(Portfolio portfolio) {
        System.out.println("\n======== Portfolio Update: " + LocalDateTime.now().format(formatter) + " ========");
        System.out.println("POSITIONS:");
        System.out.printf("%-20s %-15s %15s%n", "SYMBOL", "QUANTITY", "MARKET VALUE");
        System.out.println("-----------------------------------------------------------");

        for (Position position : portfolio.getPositions().values()) {
            System.out.printf("%-20s %,15d %,15.2f%n",
                    position.getSymbol(),
                    position.getQuantity(),
                    position.getMarketValue());
        }

        System.out.println("-----------------------------------------------------------");
        System.out.printf("TOTAL NAV: %,15.2f%n", portfolio.getNav());
        System.out.println("===========================================================");
    }
}