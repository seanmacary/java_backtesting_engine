package org.example.models;

/**
 * Represents a single trade (buy or sell) in the backtest.
 */
public class Trade {
    private final String type; // "Buy" or "Sell"
    private final String date; // Date the trade took place
    private final double price; // Closing price at which the trade happened
    private final double profit; // Profit from the trade (0 if a buy)
    private final double balanceAfterTrade; // Account balance after this trade
    private final double positionSize; // New field for tracking trade size


    public Trade(String type, String date, double price, double profit, double balanceAfterTrade, double positionSize) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.profit = profit;
        this.balanceAfterTrade = balanceAfterTrade;
        this.positionSize = positionSize;
    }

    @Override
    public String toString() {
        return type + " at " + price + " on " + date +
                (type.equals("SELL") ? " (Profit: " + profit + ")" : "") +
                "| Balance after trade: " + balanceAfterTrade;
    }
}