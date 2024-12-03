package edu.upenn.cit594.ui;

import edu.upenn.cit594.processor.COVIDProcessor;
import edu.upenn.cit594.processor.PopulationProcessor;
import edu.upenn.cit594.processor.PropertyProcessor;
import edu.upenn.cit594.logging.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainMenu {
    private PopulationProcessor populationProcessor;
    private COVIDProcessor covidProcessor;
    private PropertyProcessor propertyProcessor;
    private Scanner scanner;
    Logger logger = Logger.getInstance();

    public MainMenu(PopulationProcessor populationProcessor, COVIDProcessor covidProcessor,
                    PropertyProcessor propertyProcessor) {
        this.populationProcessor = populationProcessor;
        this.covidProcessor = covidProcessor;
        this.propertyProcessor = propertyProcessor;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("Select an action:");
            System.out.println("0: Exit");
            System.out.println("1: Show available actions");
            if (populationProcessor != null) {
                System.out.println("2: Show total population for all ZIP Codes");
            }
            if (covidProcessor != null) {
                System.out.println("3: Show total vaccinations per capita for each ZIP Code for the specified date");
            }
            if (propertyProcessor != null) {
                System.out.println("4: Show the average market value for properties in a specified ZIP Code");
                System.out.println("5: Show the average total livable area for properties in a specified ZIP Code");
                System.out.println("6: Show the total market value per capita for a specified ZIP Code");
            }
            if (populationProcessor != null && covidProcessor != null && propertyProcessor != null) {
                System.out.println("7: Show the results of the custom feature");
            }

            System.out.print("> ");
            System.out.flush();
            String choice = scanner.nextLine();
            List<String> acceptedInputs = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7");
            if (!acceptedInputs.contains(choice)) {
                System.out.println("This is an invalid input");
                logger.logEvent(choice);
                continue;
            }

            logger.logEvent(choice);

            switch (choice) {
                case "0":
                    logger.reset();
                    return;
                case "1":
                    showAvailableActions();
                    break;
                case "2":
                    if (populationProcessor != null) {
                        System.out.println();
                        showTotalPopulation();
                        System.out.println();
                    } else {
                        System.out.println("Population data is not available.");
                    }
                    break;
                case "3":
                    if (covidProcessor != null) {
                        System.out.println();
                        showVaccinationsPerCapita();
                        System.out.println();
                    } else {
                        System.out.println("COVID data is not available.");
                    }
                    break;
                case "4":
                    if (propertyProcessor != null) {
                        System.out.println();
                        showAverageMarketValue();
                        System.out.println();
                    } else {
                        System.out.println("Property data is not available.");
                    }
                    break;
                case "5":
                    if (propertyProcessor != null) {
                        System.out.println();
                        showAverageTotalLivableArea();
                        System.out.println();
                    } else {
                        System.out.println("Property data is not available.");
                    }
                    break;
                case "6":
                    if (propertyProcessor != null) {
                        System.out.println();
                        showTotalMarketValuePerCapita();
                        System.out.println();
                    } else {
                        System.out.println("Property data is not available.");
                    }
                    break;
                case "7":
                    if (populationProcessor != null && covidProcessor != null && propertyProcessor != null) {
                        System.out.println();
                        showCustomFeature();
                        System.out.println();
                    } else {
                        System.out.println("Not all data is available for the custom feature.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showAvailableActions() {
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        System.out.println("0");
        System.out.println("1");
        if (populationProcessor != null) {
            System.out.println("2");
        }
        if (covidProcessor != null) {
            System.out.println("3");
        }
        if (propertyProcessor != null) {
            System.out.println("4");
            System.out.println("5");
            System.out.println("6");
        }
        if (populationProcessor != null && covidProcessor != null && propertyProcessor != null) {
            System.out.println("7");
        }
        System.out.println("END OUTPUT");
    }

    private void showTotalPopulation() {
        int totalPopulation = populationProcessor.getTotalPopulation();
        System.out.println("BEGIN OUTPUT");
        System.out.println(totalPopulation);
        System.out.println("END OUTPUT");
    }

    private void showVaccinationsPerCapita() {
        System.out.println("Enter 'partial' or 'full' for vaccination type:");
        System.out.print("> ");
        System.out.flush();
        String type = scanner.nextLine().trim().toLowerCase();
        logger.logEvent(type);
        if (!type.equals("partial") && !type.equals("full")) {
            System.out.println("Invalid vaccination type. Please try again.");
            return;
        }

        System.out.println("Enter date (YYYY-MM-DD):");
        System.out.print("> ");
        System.out.flush();
        String date = scanner.nextLine().trim();
        logger.logEvent(date);

        Map<String, Double> results = covidProcessor.getVaccinationsPerCapita(type, date);

        if (results == null || results.isEmpty()) {
            System.out.println("No data available for the specified date.");
            return;
        }
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        results.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> System.out.printf("%s %.4f%n", entry.getKey(), entry.getValue()));
        System.out.println("END OUTPUT");
    }

    private void showAverageMarketValue() {
        System.out.println("Enter 5-digit ZIP Code:");
        System.out.print("> ");
        System.out.flush();
        String zipCode = scanner.nextLine().trim();
        logger.logEvent(zipCode);
        double averageMarketValue = propertyProcessor.getAverageMarketValue(zipCode);
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        System.out.println((int) averageMarketValue);
        System.out.println("END OUTPUT");
    }

    private void showAverageTotalLivableArea() {
        System.out.println("Enter 5-digit ZIP Code:");
        System.out.print("> ");
        System.out.flush();
        String zipCode = scanner.nextLine().trim();
        logger.logEvent(zipCode);
        double averageTotalLivableArea = propertyProcessor.getAverageLivableArea(zipCode);
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        System.out.println((int) averageTotalLivableArea);
        System.out.println("END OUTPUT");
    }

    private void showTotalMarketValuePerCapita() {
        System.out.println("Enter 5-digit ZIP Code:");
        System.out.print("> ");
        System.out.flush();
        String zipCode = scanner.nextLine().trim();
        logger.logEvent(zipCode);
        double totalMarketValuePerCapita = propertyProcessor.getTotalMarketValuePerCapita(zipCode);
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        System.out.println((int) totalMarketValuePerCapita);
        System.out.println("END OUTPUT");
    }

    private void showCustomFeature() {
        Map<String, String> results = covidProcessor.getVaccinationsPerCapitaAndSortByLivableArea();
        System.out.println();
        System.out.println("BEGIN OUTPUT");
        results.forEach((zip, output) -> System.out.println(zip + " " + output));
        System.out.println("END OUTPUT");
    }
}
