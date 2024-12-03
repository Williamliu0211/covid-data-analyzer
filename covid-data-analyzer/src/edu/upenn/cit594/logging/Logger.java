package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

    private static Logger instance;
    private PrintWriter writer;

    private Logger() {
        // Private constructor to prevent instantiation
        writer = null; // No logging by default
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setOutputDestination(String filename) {
        try {
            if (writer != null) {
                writer.close();
            }
            writer = new PrintWriter(new FileWriter(filename, true), true); // Append mode
        } catch (IOException e) {
            System.err.println("Failed to set output destination: " + e.getMessage());
            writer = null; // Ensure writer is null if setting destination fails
        }
    }

    public void logEvent(String message) {
        if (writer != null) {
            writer.println(System.currentTimeMillis() + " " + message);
        } else {
            System.err.println("Logger is not initialized.");
        }
    }

    public void reset() {
        if (writer != null) {
            writer.close();
        }
        writer = null;
        instance = null;
    }
}
