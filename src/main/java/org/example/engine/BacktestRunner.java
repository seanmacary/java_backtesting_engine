package org.example.engine;

import org.example.utils.CsvReader;
import java.util.List;
import java.util.Scanner;
import org.example.models.MarketDataPoint;
import org.example.utils.TradeHistoryWriter;


public class BacktestRunner {

    public void run() {
        System.out.println("Running Moving Average Crossover Strategy Backtest...");

        // Load market data from CSV
        List<MarketDataPoint> marketData = CsvReader.readCsv("data/sample_market_data_MA_crossover.csv");

        System.out.println("Total data points loaded: " + marketData.size());

        // Input Parameters
        double initialBalance = 5000.0;
        int shortPeriod = 20;
        int longPeriod = 50;
        double riskPerTrade = 0.05;
        double stopLossPercent = 0.02;
        double takeProfitPercent = 0.05;

//        // Set buy threshold
//        System.out.print("Enter buy threshold (price below which to buy): ");
//        double buyThreshold = scanner.nextDouble();
//
//        // Set sell threshold
//        System.out.print("Enter sell threshold (price above which to sell): ");
//        double sellThreshold = scanner.nextDouble();

        // Initialize strategy with user-defined parameters
//        Strategy strategy = new Strategy(buyThreshold, sellThreshold, initialBalance, riskPerTrade, stopLossPercent, takeProfitPercent);
        MovingAverageStrategy strategy = new MovingAverageStrategy(shortPeriod, longPeriod, initialBalance, riskPerTrade, stopLossPercent, takeProfitPercent);

        // Run the strategy on the market data
        strategy.run(marketData);

        // Save trade history to CSV
        TradeHistoryWriter.writeToCSV(strategy.getTradeHistory());
    }
}