package org.example;

import org.example.engine.BacktestRunner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Backtesting Engine...");

        BacktestRunner backtest = new BacktestRunner();
        backtest.run();
    }
}
