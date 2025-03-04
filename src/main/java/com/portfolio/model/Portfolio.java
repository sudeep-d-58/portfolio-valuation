package com.portfolio.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Portfolio {

    private Map<String, Position> positions;
    private double nav;

    public Portfolio() {
        this.positions = new ConcurrentHashMap<>();
        this.nav = 0.0;
    }

    public void addPosition(Position position) {
        positions.put(position.getSymbol(), position);
    }

    public Position getPosition(String symbol) {
        return positions.get(symbol);
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public double getNav() {
        return nav;
    }

    public void updateNav() {
        this.nav = positions.values().stream()
                .mapToDouble(Position::getMarketValue)
                .sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Portfolio:\n");
        positions.values().forEach(p -> sb.append("  ").append(p).append("\n"));
        sb.append(String.format("Total NAV: %.2f", nav));
        return sb.toString();
    }
}
