package org.example.models;

/**
 * Represents a single data point in the market dataset.
 * Each instance of this class holds a data and the corresponding closing price.
 */
public class MarketDataPoint {
    private final String date;  // The data of the market data point
    private final double closePrice;  // The closing price of the asset on that date

    /**
     * Constructor to initialize the market data point.
     *
     * @param date The data in String format.
     * @param closePrice The closing price as a double.
     */
    public MarketDataPoint(String date, double closePrice) {
        this.date = date;
        this.closePrice = closePrice;
    }

    /**
     * Gets the date of this market data point.
     *
     * @return The date as a String.
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the closing price of this market data point.
     *
     * @return The closing price as a double.
     * */
    public double getClosePrice() {
        return closePrice;
    }

    /**
     * Converts the object into a readable string format for debugging.
     *
     * @return A string representation of the MarketDataPoint.
     */
    @Override
    public String toString() {
        return "MarketDataPoint [date=" + date + ", closePrice=" + closePrice + "]";
    }
}
