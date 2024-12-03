package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertyRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyProcessor {
    private List<PropertyRecord> propertyData;
    private Map<String, Integer> populationData;

    // Caches for memoization
    private Map<String, Double> averageMarketValueCache;
    private Map<String, Double> averageLivableAreaCache;
    private Map<String, Double> totalMarketValuePerCapitaCache;
    private Map<String, Double> totalLivableAreaCache;

    public PropertyProcessor(List<PropertyRecord> propertyData, Map<String, Integer> populationData) {
        this.propertyData = propertyData;
        this.populationData = populationData;
        this.averageMarketValueCache = new HashMap<>();
        this.averageLivableAreaCache = new HashMap<>();
        this.totalMarketValuePerCapitaCache = new HashMap<>();
        this.totalLivableAreaCache = new HashMap<>();
    }

    public double getAverageMarketValue(String zipCode) {
        // Check cache first
        if (averageMarketValueCache.containsKey(zipCode)) {
            return averageMarketValueCache.get(zipCode);
        }

        List<PropertyRecord> properties = propertyData.stream()
                .filter(record -> record.getZipCode().equals(zipCode))
                .collect(Collectors.toList());

        if (properties.isEmpty())
            return 0;

        double totalMarketValue = properties.stream()
                .mapToDouble(PropertyRecord::getMarketValue)
                .sum();

        double averageMarketValue = totalMarketValue / properties.size();
        // Store result in cache
        averageMarketValueCache.put(zipCode, averageMarketValue);

        return averageMarketValue;
    }

    public double getAverageLivableArea(String zipCode) {
        // Check cache first
        if (averageLivableAreaCache.containsKey(zipCode)) {
            return averageLivableAreaCache.get(zipCode);
        }

        List<PropertyRecord> properties = propertyData.stream()
                .filter(record -> record.getZipCode().equals(zipCode))
                .collect(Collectors.toList());

        if (properties.isEmpty())
            return 0;

        double totalLivableArea = properties.stream()
                .mapToDouble(PropertyRecord::getLivableArea)
                .sum();

        double averageLivableArea = totalLivableArea / properties.size();
        // Store result in cache
        averageLivableAreaCache.put(zipCode, averageLivableArea);

        return averageLivableArea;
    }

    public double getTotalMarketValuePerCapita(String zipCode) {
        // Check cache first
        if (totalMarketValuePerCapitaCache.containsKey(zipCode)) {
            return totalMarketValuePerCapitaCache.get(zipCode);
        }

        List<PropertyRecord> properties = propertyData.stream()
                .filter(record -> record.getZipCode().equals(zipCode))
                .collect(Collectors.toList());

        if (properties.isEmpty())
            return 0;

        double totalMarketValue = properties.stream()
                .mapToDouble(PropertyRecord::getMarketValue)
                .sum();

        int population = populationData.getOrDefault(zipCode, 0);
        if (population == 0)
            return 0;

        double totalMarketValuePerCapita = totalMarketValue / population;
        // Store result in cache
        totalMarketValuePerCapitaCache.put(zipCode, totalMarketValuePerCapita);

        return totalMarketValuePerCapita;
    }

    public Map<String, Double> getTotalLivableAreaByZipCode() { 
        return propertyData.stream()
            .collect(Collectors.groupingBy(
                PropertyRecord::getZipCode,
                Collectors.summingDouble(PropertyRecord::getLivableArea)
            ));
    }

    public Map<String, Double> getTotalLivableAreaCache() {
        return totalLivableAreaCache;
    }
}