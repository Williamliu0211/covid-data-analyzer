package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.MainMenu;
import edu.upenn.cit594.util.COVIDRecord;
import edu.upenn.cit594.util.PropertyRecord;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // Reset the logger before running the main logic
        resetLogger();

        String covidFile = null;
        String populationFile = null;
        String propertyFile = null;
        String logFile = null;

        Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");

        for (String arg : args) {
            Matcher matcher = pattern.matcher(arg);
            if (!matcher.matches()) {
                System.err.println("Invalid argument format: " + arg);
                return;
            }

            String name = matcher.group("name");
            String value = matcher.group("value");

            switch (name) {
                case "covid":
                    if (covidFile != null) {
                        System.err.println("Duplicate argument: --covid");
                        return;
                    }
                    covidFile = value;
                    break;
                case "population":
                    if (populationFile != null) {
                        System.err.println("Duplicate argument: --population");
                        return;
                    }
                    populationFile = value;
                    break;
                case "properties":
                    if (propertyFile != null) {
                        System.err.println("Duplicate argument: --properties");
                        return;
                    }
                    propertyFile = value;
                    break;
                case "log":
                    if (logFile != null) {
                        System.err.println("Duplicate argument: --log");
                        return;
                    }
                    logFile = value;
                    break;
                default:
                    System.err.println("Invalid argument: " + arg);
                    return;
            }
        }

        // Initialize Logger
        Logger logger = Logger.getInstance();
        if (logFile != null) {
            logger.setOutputDestination(logFile);
        }

        // Log command-line arguments
        StringBuilder cmdArgs = new StringBuilder();
        for (String arg : args) {
            cmdArgs.append(arg).append(" ");
        }
        logger.logEvent("Command-line arguments: " + cmdArgs.toString().trim());

        try {
            COVIDDataParser covidParser = null;
            PopulationDataParser populationParser = null;
            PropertyDataParser propertyParser = null;

            List<COVIDRecord> covidRecords = null;
            Map<String, Integer> populationData = null;
            List<PropertyRecord> propertyRecords = null;

            // Initialize Data Parsers and Parse Data if files are provided
            if (covidFile != null) {
                if (fileExists(covidFile)) {
                    logger.logEvent("Parsing " + covidFile);
                    covidParser = new COVIDDataParser(covidFile);
                    covidRecords = covidParser.parseData();
                } else {
                    System.err.println("COVID data file does not exist or cannot be read: " + covidFile);
                }
            }

            if (populationFile != null) {
                if (fileExists(populationFile)) {
                    logger.logEvent("Parsing " + populationFile);
                    populationParser = new PopulationDataParser(populationFile);
                    populationData = populationParser.parseData();
                } else {
                    System.err.println("Population data file does not exist or cannot be read: " + populationFile);
                }
            }

            if (propertyFile != null) {
                if (fileExists(propertyFile)) {
                    logger.logEvent("Parsing " + propertyFile);
                    propertyParser = new PropertyDataParser(propertyFile);
                    propertyRecords = propertyParser.parseData();
                } else {
                    System.err.println("Property data file does not exist or cannot be read: " + propertyFile);
                }
            }

            // Initialize Processors if data is available
            PopulationProcessor populationProcessor = null;
            if (populationData != null) {
                populationProcessor = new PopulationProcessor(populationData);
            }

            PropertyProcessor propertyProcessor = null;
            if (propertyRecords != null && populationData != null) {
                propertyProcessor = new PropertyProcessor(propertyRecords, populationData);
            }

            COVIDProcessor covidProcessor = null;
            if (covidRecords != null && populationData != null) {
                covidProcessor = new COVIDProcessor(covidRecords, populationData, propertyProcessor);
            }

            // Initialize and Display Main Menu
            MainMenu mainMenu = new MainMenu(populationProcessor, covidProcessor, propertyProcessor);
            mainMenu.displayMenu();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An error occurred during processing: " + e.getMessage());
            logger.logEvent("An error occurred: " + e.getMessage());
        }
    }

    private static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists() && file.canRead();
    }

    // Method to reset the logger
    private static void resetLogger() {
        Logger.getInstance().reset();
    }
}
