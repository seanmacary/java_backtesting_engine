package org.example.engine;

import org.example.models.MarketDataPoint;
import org.example.models.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * A trading strategy with risk management using stop-loss and take-profit.
 */
public class Strategy {
    private final double buyThreshold;  // The price at which we buy
    private final double sellThreshold; // The price at which we sell
    private final double riskPerTrade; // % of balance to allocate per trade
    private final double stopLossPercent; // Stop-loss percentage (e.g. 2% below buy price)
    private final double takeProfitPercent; // Take-profit percentage (e.g. 5% above buy price)

    private boolean hasPosition = false; // Tracks if we currently own the asset
    private double buyPrice = 0; // The price at which we last bought
    private double positionSize = 0; // Number of units purchased
    private double stopLossPrice = 0; // Stop-loss level
    private double takeProfitPrice = 0; // Take-profit level


    private final List<Trade> tradeHistory = new ArrayList<>(); // List to keep track of all trades made by strategy
    private double balance; // Account balance
    private final double initialBalance; // Initial capital
    private double totalProfit = 0; // Running total of profits/loss

    /**
     * Constructor to initialize the strategy with buy and sell thresholds.
     *
     * @param buyThreshold The price at which to buy.
     * @param sellThreshold The price at which to sell.
     * @param initialBalance The starting account balance.
     * @param riskPerTrade The percentage of balance allocated per trade (e.g., 5%)
     * @param stopLossPercent The stop-loss percentage (e.g., 0.02 for 2%).
     * @param takeProfitPercent The take-profit percentage (e.g., 0.05 for 5%).
     */
    public Strategy(double buyThreshold, double sellThreshold, double initialBalance,
                    double riskPerTrade, double stopLossPercent, double takeProfitPercent) {
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
        this.riskPerTrade = riskPerTrade;
        this.stopLossPercent = stopLossPercent;
        this.takeProfitPercent = takeProfitPercent;
    }

    /**
     * Runs the strategy on market data, applying stop-loss and take-profit rules.
     *
     * @param marketDataPoints The list of market data points.
    */
    public void run(List<MarketDataPoint> marketDataPoints) {

        for (MarketDataPoint marketDataPoint : marketDataPoints) {
            double price = marketDataPoint.getClosePrice();
            String date = marketDataPoint.getDate();

            // BUY Logic (Enter Trade)
            if (!hasPosition && price <= buyThreshold) {

                // Calculate the  amount to invest (risk % of balance)
                double capitalToInvest = balance * riskPerTrade;
                positionSize = capitalToInvest / price; // Number of units to buy

                if (positionSize > 0 && capitalToInvest < balance) {
                    buyPrice = price;

                    stopLossPrice = buyPrice * (1 - stopLossPercent); // Set the stop-loss level
                    takeProfitPrice = buyPrice * (1 + takeProfitPercent); // Set the take-profit level

                    hasPosition = true;
                    balance -= capitalToInvest; // Deduct investment from balance

                    tradeHistory.add(new Trade("BUY", date, price, 0, balance, positionSize, stopLossPrice, takeProfitPrice));
                    System.out.printf("BUY %.2f units at %.2f on %s | Stop-Loss: %.2f | Take-Profit: %.2f | Balance: %.2f%n",
                            positionSize, price, date, stopLossPrice, takeProfitPrice, balance);
                } else {
                    System.out.printf("INSUFFICIENT FUNDS: Cannot buy at %.2f on %s | Balance: %.2f%n",
                            price, date, balance);
                }

            }
            // SELL logic (Exit trade due to take-profit or stop-loss)
            if (hasPosition) {
                if (price >= takeProfitPrice) {
                    exitTrade("Take-Profit", price, date);
                } else if (price <= stopLossPrice) {
                    exitTrade("Stop-Loss", price, date);
                }
            }
        }
        printTradeSummary();
    }

    /**
     * Handles exiting a trade when stop-loss or take-profit is triggered.
     *
     * @param reason The reason for exiting (Stop-Loss or Take-Profit).
     * @param price The exit price.
     * @param date The exit date.
     */
    private void exitTrade(String reason, double price, String date) {
        double sellValue = positionSize * price;
        double profit = sellValue - (positionSize * buyPrice);
        totalProfit += profit;
        balance += sellValue;

        tradeHistory.add(new Trade(reason, date, price, profit, balance, positionSize, stopLossPrice, takeProfitPrice));
        System.out.printf("%s EXIT: Sold %.2f units at %.2f on %s | Profit: %.2f | New Balance: %.2f%n",
                reason, positionSize, price, date, profit, balance);

        hasPosition = false;
        buyPrice = 0;
        positionSize = 0;
    }

    /**
     * Prints a summary of all trades and total profit.
     * */
    private void printTradeSummary() {
        System.out.println("\n----- Trade Summary -----");
        for (Trade trade : tradeHistory) {
            System.out.println(trade);
        }
        System.out.printf("Total Profit: %.2f | Final Balance: %.2f%n", totalProfit, balance);

    }

    /**
     * Returns the trade history for export.
     */
    public List<Trade> getTradeHistory() {
        return tradeHistory;
    }
}
