package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.models.MarketDataPoint; // Import the MarketDataPoint model class

public class CsvReader {

    /**
     * Reads a CSV file containing market data and converts each row into a MarketDataPoint Object
     *
     * @param filePath The path to the CSV file.
     * @return A list of MarketDataPoint objects containing the date and close price.
     */

    public static List<MarketDataPoint> readCsv(String filePath) {
        List<MarketDataPoint> data = new ArrayList<>(); // List to store parsed market data

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false; // Track whether we've skipped the first row

            while ((line = br.readLine()) != null) {    // Read each line from the file
                if (!headerSkipped) {
                    // Skip the header if we're at the beginning of the file
                    headerSkipped = true;
                    continue;
                }

                // Split the line by commas to extract individual values
                String[] values = line.split(",");

                // Check to make sure the row contains exactly 2 items (date, close price)
                if (values.length == 2) {
                    String date = values[0];
                    double closePrice = Double.parseDouble(values[1]); // Convert the close price to a double

                    // Create a MarketDataPoint object and add it to the list
                    data.add(new MarketDataPoint(date, closePrice));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return data;    // Return the list of market data points
    }
}
