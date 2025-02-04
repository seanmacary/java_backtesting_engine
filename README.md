# Java Backtesting Engine

## Overview
This project is a **simple backtesting engine** for evaluating trading strategies on historical market data. The engine is written in Java and follows a structured approach using:
- **Maven** for dependency management
- **CSV data files** for historical market data
- **A simple strategy** based on threshold-based buy/sell logic
- **Profit/loss tracking** and account balance management

## Current Features
### **1. CSV Data Loading**
- Reads market data from a CSV file (`data/sample_market_data.csv`)
- Supports basic **date and close price** columns

### **2. Basic Trading Strategy**
- **Buy when price is below a threshold**
- **Sell when price is above a threshold**
- Trades are logged with timestamps

### **3. Profit/Loss Tracking**
- Records trades and **calculates profit/loss** after each trade
- Keeps a running **total profit**
- Displays a **trade history summary**

### **4. Account Balance Management**
- Includes an **initial balance**
- Deducts **purchase price** when buying
- Adds **sale price** when selling
- Prevents buying if **insufficient funds**

## Next Steps
### **1. Position Sizing**
- Instead of going "all in" on trades, allow fractional purchases based on available balance.

### **2. Risk Management**
- Implement **stop-loss and take-profit orders** to limit potential losses.

### **3. Advanced Strategies**
- Add support for **moving average crossover strategies**.
- Implement **momentum-based trading**.

### **4. Performance Metrics**
- Calculate **Sharpe ratio**, **drawdowns**, and other key backtesting performance metrics.

### **5. Data Storage & Analysis**
- Save **trade history** and account balance changes to a CSV file for future analysis.
