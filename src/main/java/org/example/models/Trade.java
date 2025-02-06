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
    private final double stopLossPrice;
    private final double takeProfitPrice;


    public Trade(String type, String date, double price, double profit,
                 double balanceAfterTrade, double positionSize, double stopLossPrice, double takeProfitPrice) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.profit = profit;
        this.balanceAfterTrade = balanceAfterTrade;
        this.positionSize = positionSize;
        this.stopLossPrice = stopLossPrice;
        this.takeProfitPrice = takeProfitPrice;
    }

    // Getter methods for CSV export
    public String getType() { return type; }
    public String getDate() { return date; }
    public double getPrice() { return price; }
    public double getProfit() { return profit; }
    public double getBalanceAfterTrade() { return balanceAfterTrade; }
    public double getPositionSize() { return positionSize; }
    public double getStopLossPrice() { return stopLossPrice; }
    public double getTakeProfitPrice() { return takeProfitPrice; }

    @Override
    public String toString() {
        return String.format(
                "%-12s | %-10s | Price: %-8.2f | Qty: %-6.3f | SL: %-8.2f | TP: %-8.2f | Profit: %-7.2f | Balance: %-8.2f",
                type, date, price, positionSize, stopLossPrice, takeProfitPrice, profit, balanceAfterTrade
        );
    }
}