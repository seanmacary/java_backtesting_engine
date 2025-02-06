# Java Backtesting Engine

## Overview
This project is a **Java-based backtesting engine** designed to test trading strategies on historical market data. The engine executes trades, tracks performance metrics, and provides insights into strategy effectiveness.

## Features
### **1. Market Data Handling**
- Reads market data from a CSV file (`data/sample_market_data.csv`).
- Supports **date and closing price** columns.

### **2. Moving Average Crossover Strategy**
- Implements a **short-term vs. long-term moving average crossover** strategy.
- **Buy signal**: When the short-term moving average crosses **above** the long-term moving average.
- **Sell signal**: When the short-term moving average crosses **below** the long-term moving average.
- Uses **configurable short-term and long-term SMA periods**.

### **3. Trade Execution & Risk Management**
- Uses **position sizing** (trades a percentage of available balance per trade).
- Implements **stop-loss and take-profit levels** for risk management.
- Supports **automatic closing of open positions** at the end of backtest.

### **4. Performance Metrics Tracking**
- **Total Return (%)**: Measures overall portfolio performance.
- **Win Rate (%)**: Tracks the percentage of profitable trades.
- **Maximum Drawdown (%)**: Measures the largest portfolio decline.
- **Sharpe Ratio**: Evaluates risk-adjusted returns.
- **Profit Factor**: Ratio of total gains to total losses.

### **5. Trade History Logging**
- Saves executed trades to `data/trade_results.csv`.
- Records **date, price, quantity, stop-loss, take-profit, profit, and balance**.

### **6. Automated Performance Reporting**
- Prints a detailed summary of **strategy performance** at the end of the backtest.
- Tracks cumulative profit/loss and risk-adjusted metrics.