package org.example.engine;

import org.example.models.MarketDataPoint;
import org.example.models.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple trading strategy that buys when the price drops below a certain threshold
 * and sells when the price rises above a different threshold.
 * Keeps track of overall profit and loss as well as account balance for backtest.
 * */
public class Strategy {
    private final double buyThreshold;  // The price at which we buy
    private final double sellThreshold; // The price at which we sell
    private final double riskPerTrade; // % of balance to allocate per trade
    private boolean hasPosition = false; // Tracks if we currently own the asset
    private double buyPrice = 0; // The price at which we last bought
    private double positionSize = 0; // Number of units purchased
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
     */
    public Strategy(double buyThreshold, double sellThreshold, double initialBalance, double riskPerTrade) {
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
        this.initialBalance = initialBalance;
        this.balance = initialBalance;  // Set the initial balance
        this.riskPerTrade = riskPerTrade;
    }

    /**
     * Runs the strategy on the given market data and prints buy/sell actions.
     *
     * @param marketDataPoints The list of market data points.
    */
    public void run(List<MarketDataPoint> marketDataPoints) {
        for (MarketDataPoint marketDataPoint : marketDataPoints) {
            double price = marketDataPoint.getClosePrice();
            String date = marketDataPoint.getDate();

            if (!hasPosition && price <= buyThreshold) {

                // Calculate the  amount to invest (risk % of balance)
                double capitalToInvest = balance * riskPerTrade;
                positionSize = capitalToInvest / price; // Number of units to buy

                if (positionSize > 0) {
                    buyPrice = price;
                    hasPosition = true;
                    balance -= capitalToInvest; // Deduct investment from balance

                    tradeHistory.add(new Trade("BUY", date, price, 0, balance, positionSize)); // Profit is 0 for buys
                    System.out.printf("BUY %.2f units at %.2f on %s | New Balance: %.2f%n",
                            positionSize, price, date, balance);
                } else {
                    System.out.printf("INSUFFICIENT FUNDS: Cannot buy at %.2f on %s | Balance: %.2f%n",
                            price, date, balance);
                }

            } else if (hasPosition && price >= sellThreshold) {
                // Sell the asset and calculate the profit
                double sellValue = positionSize * price;
                double profit = sellValue - (positionSize * buyPrice); // Calculate profit
                totalProfit += profit; // Update running total for profit
                balance += sellValue; // Add sell value to the balance

                tradeHistory.add(new Trade("SELL", date, price, profit, balance, positionSize));
                System.out.printf("SELL %.2f units at %.2f on %s | Profit: %.2f | New Balance: %.2f%n",
                        positionSize, price, date, profit, balance);

                hasPosition = false; // Reset position
                buyPrice = 0;
                positionSize = 0;
            }
        }
        // Print final summary
        printTradeSummary();
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
