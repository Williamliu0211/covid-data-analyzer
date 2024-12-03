package edu.upenn.cit594.processor;

import java.util.Map;

public class PopulationProcessor {
    private Map<String, Integer> populationData;
    // Cache for total population
    private Integer totalPopulationCache; 

    public PopulationProcessor(Map<String, Integer> populationData) {
        this.populationData = populationData;
        
        // Initialize the cache to null
        this.totalPopulationCache = null; 
    }

    public int getTotalPopulation() {
        // Check if the total population is already computed and cached
        if (totalPopulationCache != null) {
            return totalPopulationCache;
        }

        int totalPopulation = 0;
        for (int population : populationData.values()) {
            totalPopulation += population;
        }

        // Store the result in the cache
        totalPopulationCache = totalPopulation;

        return totalPopulation;
    }

    public int getPopulationByZipCode(String zipcode) {
        return populationData.getOrDefault(zipcode, 0);
    }
}