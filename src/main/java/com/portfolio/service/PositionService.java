// PositionService.java
package com.portfolio.service;

import com.portfolio.model.Portfolio;
import com.portfolio.model.Position;
import com.portfolio.model.security.Security;
import com.portfolio.repository.SecurityRepository;
import com.portfolio.util.CsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class PositionService {
    private final SecurityRepository securityRepository;

    @Autowired
    public PositionService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public Portfolio loadPortfolioFromCsv(String filePath) throws IOException {
        Map<String, Integer> positionData = CsvReader.readPositionsFromCsv(filePath);
        Portfolio portfolio = new Portfolio();

        for (Map.Entry<String, Integer> entry : positionData.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Security security = securityRepository.getSecurityByTicker(symbol);
            if (security != null) {
                Position position = new Position(security, quantity);
                portfolio.addPosition(position);
            } else {
                System.err.println("Security not found: " + symbol);
            }
        }

        return portfolio;
    }
}