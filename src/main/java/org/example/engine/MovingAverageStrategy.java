package org.example.engine;

import org.example.models.MarketDataPoint;
import org.example.models.Trade;
import java.util.ArrayList;
import java.util.List;

/*
* Implements a Moving Average Crossover Strategy
* */
public class MovingAverageStrategy {

    private final int shortPeriod;
    private final int longPeriod;
    private final double riskPerTrade;
    private final double stopLossPercent;
    private final double takeProfitPercent;

    private boolean hasPosition = false;
    private double buyPrice = 0;
    private double positionSize = 0;
    private final List<Trade> tradeHistory = new ArrayList<>();
    private double balance;
    private double initialBalance;

    public MovingAverageStrategy(int shortPeriod, int longPeriod, double initialBalance,
                                 double riskPerTrade, double stopLossPercent, double takeProfitPercent) {
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
        this.riskPerTrade = riskPerTrade;
        this.stopLossPercent = stopLossPercent;
        this.takeProfitPercent = takeProfitPercent;
    }

    private double totalWins = 0;
    private double totalLosses = 0;
    private double maxDrawdown = 0;
    private double peakBalance;
    private double cumulativeReturn = 0;
    private int totalTrades = 0;

    public void run(List<MarketDataPoint> marketData) {
        List<Double> shortSMA = calculateSMA(marketData, shortPeriod);
        List<Double> longSMA = calculateSMA(marketData, longPeriod);


        for(int i = longPeriod; i < marketData.size(); i++) {
            double price = marketData.get(i).getClosePrice();
            String date = marketData.get(i).getDate();
            double shortMA = shortSMA.get(i);
            double longMA = longSMA.get(i);

            if (!hasPosition && shortSMA.get(i-1) < longSMA.get(i-1) && shortMA > longMA) {
                double capitalToInvest = balance * riskPerTrade;
                positionSize = capitalToInvest / price;

                if (positionSize > 0 && capitalToInvest <= balance) {
                    buyPrice = price;
                    hasPosition = true;
                    balance -= capitalToInvest;
                    double stopLossPrice = buyPrice * (1 - stopLossPercent);
                    double takeProfitPrice = stopLossPrice * (1 + takeProfitPercent);

                    tradeHistory.add(new Trade("BUY", date, price, 0, balance, positionSize, stopLossPrice, takeProfitPrice));
                    System.out.printf("BUY %.2f units at %.2f on %s | SL: %.2f | TP: %.2f | Balance: %.2f%n",
                            positionSize, price, date, stopLossPrice, takeProfitPrice, balance);
                }
            }

            if (hasPosition && shortSMA.get(i - 1) > longSMA.get(i - 1) && shortMA < longMA) {
                double sellValue = positionSize * price;
                double profit = sellValue - (positionSize * buyPrice);
                balance += sellValue;
                hasPosition = false;
                tradeHistory.add(new Trade("SELL", date, price, profit, balance, positionSize, 0, 0));

                // Update win/loss stats
                if (profit > 0) totalWins++;
                else totalLosses++;
                totalTrades++;

                // Track peak balance and max drawdown
                if (balance > peakBalance) peakBalance = balance;
                double drawdown = (peakBalance - balance) / peakBalance;
                if (drawdown > maxDrawdown) maxDrawdown = drawdown;

                System.out.printf("SELL %.2f units at %.2f on %s | Profit: %.2f | Balance: %.2f%n",
                        positionSize, price, date, profit, balance);

                buyPrice = 0;
                positionSize = 0;
            }
        }

        if (hasPosition) {
            double lastPrice = marketData.get(marketData.size() - 1).getClosePrice();
            double sellValue = positionSize * lastPrice;
            double profit = sellValue - (positionSize * buyPrice);
            balance += sellValue;
            hasPosition = false;

            tradeHistory.add(new Trade("FINAL SELL", marketData.get(marketData.size() - 1).getDate(),
                    lastPrice, profit, balance, positionSize, 0, 0));
            System.out.printf("FINAL SELL | %.2f units at %.2f | Profit: %.2f | Final Balance: %.2f%n",
                    positionSize, lastPrice, profit, balance);
        }

        calculatePerformanceMetrics();
    }

    private List<Double> calculateSMA(List<MarketDataPoint> marketData, int period) {
        List<Double> smaValues = new ArrayList<>();
        for (int i = 0; i < marketData.size(); i++) {
            if (i < period - 1) {
                smaValues.add(0.0);
            } else {
                double sum = 0.0;
                for (int j = i - period + 1; j <= i; j++) {
                    sum += marketData.get(j).getClosePrice();
                }
                smaValues.add(sum / period);
            }
        }
        return smaValues;
    }

    private void calculatePerformanceMetrics() {
        double totalReturn = ((balance - initialBalance) / initialBalance) * 100;
        double winRate = (totalTrades > 0) ? (totalWins / totalTrades) * 100 : 0;
        double avgProfitLoss = (totalTrades > 0) ? cumulativeReturn / totalTrades : 0;
        double profitFactor = (totalLosses > 0) ? totalWins / totalLosses : 0;
        double sharpeRatio = (avgProfitLoss / maxDrawdown) * Math.sqrt(252);  // Approximate annualized Sharpe Ratio

        System.out.println("\n--- Performance Summary ---");
        System.out.printf("Total Return: %.2f%% | Final Balance: %.2f%n", totalReturn, balance);
        System.out.printf("Total Trades: %d | Win Rate: %.2f%%%n", totalTrades, winRate);
        System.out.printf("Max Drawdown: %.2f%%%n", maxDrawdown * 100);
        System.out.printf("Profit Factor: %.2f | Sharpe Ratio: %.2f%n", profitFactor, sharpeRatio);
    }


    private void printTradeSummary() {
        System.out.println("\n--- Trade Summary ---");
        for (Trade trade : tradeHistory) {
            System.out.println(trade);
        }
        System.out.printf("Final Balance: %.2f%n", balance);
    }

    public List<Trade> getTradeHistory() {
        return tradeHistory;
    }
}
