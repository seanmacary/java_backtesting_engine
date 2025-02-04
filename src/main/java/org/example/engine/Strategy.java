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
    private boolean hasPosition = false; // Tracks if we currently own the asset
    private double buyPrice = 0; // The price at which we last bought
    private final List<Trade> tradeHistory = new ArrayList<>(); // List to keep track of all trades made by strategy

    private double balance; // Account balance
    private double initialBalance; // Initial capital
    private double totalProfit = 0; // Running total of profits/loss

    /**
     * Constructor to initialize the strategy with buy and sell thresholds.
     *
     * @param buyThreshold The price at which to buy.
     * @param sellThreshold The price at which to sell.
     * @param initialBalance The starting account balance.
     */
    public Strategy(double buyThreshold, double sellThreshold, double initialBalance) {
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
        this.initialBalance = initialBalance;
        this.balance = initialBalance;  // Set the initial balance
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

            if (!hasPosition && price <= buyThreshold && balance >= price) {
                // Buy the asset when price drops below the buy threshold
                buyPrice = price;
                hasPosition = true;
                balance -= buyPrice; // Deduct price from balance
                tradeHistory.add(new Trade("BUY", date, price, 0, balance)); // Profit is 0 for buys
                System.out.println("Buy at " + price + " on " + date + " | Balance: " + balance);

            } else if (hasPosition && price >= sellThreshold) {
                // Sell the asset when price rise above sell threshold
                double profit = price - buyPrice; // Calculate profit
                totalProfit += profit; // Update running total for profit
                balance += price; // Add sale price to the balance

                tradeHistory.add(new Trade("SELL", date, price, profit, balance));
                System.out.println("Sell at " + price + " on " + date + " | Balance: " + balance);

                hasPosition = false; // Reset position
                buyPrice = 0;
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
        System.out.println("\n----- Total Profit: " + totalProfit);
        System.out.println("\n----- Final Balance: " + balance);
    }
}
