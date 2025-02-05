package org.example.engine;

import org.example.utils.CsvReader;
import java.util.List;
import org.example.models.MarketDataPoint;

public class BacktestRunner {

    public void run() {
        System.out.println("Running backtest...");

        // Load market data (this will be replaced with real data logic)
        List<MarketDataPoint> marketData = CsvReader.readCsv("data/sample_market_data.csv");

//        // Print loaded data (TODO - edit logic)
//        for (MarketDataPoint dp : marketData) {
//            System.out.println(dp);
//        }

        // Print total number of loaded points
        System.out.println("Loaded " + marketData.size() + " market data points.");

        // Initialize starting account balance and risk per trade
        double initialBalance = 500.0;
        double riskPerTrade = 0.05; // Risk 5% of balance per trade

        // Initialize a simple trading strategy
        Strategy strategy = new Strategy(150.0, 170.0, initialBalance, riskPerTrade);

        // Run the strategy on the market data
        strategy.run(marketData);
    }
}
