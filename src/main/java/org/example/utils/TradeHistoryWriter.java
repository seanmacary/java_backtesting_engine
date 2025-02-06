package org.example.utils;

import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.example.models.Trade;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for writing trade data to a CSV file
 */
public class TradeHistoryWriter {

    /*
    * Writes the trade history to a CSV file for analysis
    *
    * @param trades list of executed trades
    */
    public static void writeToCSV(List<Trade> trades){
        String filePath = "data/trade_results/trade_results.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV headers
            writer.append("Date,Type,Price,Quantity,Stop-Loss,Take-Profit,Profit,Balance\n");

            // Write each trade
            for (Trade trade : trades) {
                writer.append(String.format("%s,%s,%.2f,%.3f,%.2f,%.2f,%.2f,%.2f\n",
                        trade.getDate(),
                        trade.getType(),
                        trade.getPrice(),
                        trade.getPositionSize(),
                        trade.getStopLossPrice(),
                        trade.getTakeProfitPrice(),
                        trade.getProfit(),
                        trade.getBalanceAfterTrade()));
            }
            System.out.println("Trade history successfully saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing trade history CSV: " + e.getMessage());
        }
    }
}
