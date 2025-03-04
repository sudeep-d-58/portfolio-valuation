package com.portfolio.repository;

import com.portfolio.model.security.CallOption;
import com.portfolio.model.security.PutOption;
import com.portfolio.model.security.Security;
import com.portfolio.model.security.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SecurityRepositoryImpl implements SecurityRepository{
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Security> securityCache;

    @Autowired
    public SecurityRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.securityCache = new HashMap<>();
        loadSecurities();
    }

    private void loadSecurities() {
        String sql = "SELECT * FROM securities";
        jdbcTemplate.query(sql, (rs, rowNum) -> mapSecurity(rs))
                .forEach(security -> securityCache.put(security.getTicker(), security));
    }

    private Security mapSecurity(ResultSet rs) throws SQLException {
        String ticker = rs.getString("ticker");
        String type = rs.getString("type");

        switch (type) {
            case "STOCK":
                double expectedReturn = rs.getDouble("expected_return");
                double volatility = rs.getDouble("volatility");
                // Default initial price, will be updated by market data service
                return new Stock(ticker, expectedReturn, volatility, 100.0);

            case "CALL":
                double callStrike = rs.getDouble("strike");
                LocalDate callMaturity = rs.getDate("maturity").toLocalDate();
                String callUnderlying = rs.getString("underlying_ticker");
                return new CallOption(ticker, callStrike, callMaturity, callUnderlying);

            case "PUT":
                double putStrike = rs.getDouble("strike");
                LocalDate putMaturity = rs.getDate("maturity").toLocalDate();
                String putUnderlying = rs.getString("underlying_ticker");
                return new PutOption(ticker, putStrike, putMaturity, putUnderlying);

            default:
                throw new IllegalArgumentException("Unknown security type: " + type);
        }
    }

    @Override
    public List<Security> getAllSecurities() {
        return new CopyOnWriteArrayList<>(securityCache.values());
    }

    @Override
    public Security getSecurityByTicker(String ticker) {
        return securityCache.get(ticker);
    }
}
