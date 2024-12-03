package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.COVIDRecord;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class COVIDProcessor {
    private List<COVIDRecord> covidData;
    private PopulationProcessor populationProcessor;
    private PropertyProcessor propertyProcessor;

    // Caches for memoization
    private Map<String, Map<String, Double>> vaccinationsPerCapitaCache;
    private Map<String, Double> averageVaccinationsPerCapitaCache;

    public COVIDProcessor(List<COVIDRecord> covidData, Map<String, Integer> populationData, PropertyProcessor propertyProcessor) {
        this.covidData = covidData;
        this.populationProcessor = new PopulationProcessor(populationData);
        this.propertyProcessor = propertyProcessor;
        this.vaccinationsPerCapitaCache = new HashMap<>();
        this.averageVaccinationsPerCapitaCache = new HashMap<>();
    }

    public Map<String, Double> getVaccinationsPerCapita(String type, String date) {
        // Check cache first
        if (vaccinationsPerCapitaCache.containsKey(date) && vaccinationsPerCapitaCache.get(date).containsKey(type)) {
            return vaccinationsPerCapitaCache.get(date);
        }

        Map<String, Double> result = new HashMap<>();

        for (COVIDRecord record : covidData) {
            if (record.getDate().equals(date)) {
                int population = populationProcessor.getPopulationByZipCode(record.getZipCode());
                if (population == 0) {
                    continue;
                }

                double vaccinationCount;
                if (type.equals("partial")) {
                    vaccinationCount = record.getPartiallyVaccinated();
                } else {
                    vaccinationCount = record.getFullyVaccinated();
                }

                double perCapita = vaccinationCount / (double) population;
                result.put(record.getZipCode(), Math.round(perCapita * 10000.0) / 10000.0); // Round to 4 decimal places
            }
        }

        // Store result in cache
        vaccinationsPerCapitaCache.put(date, result);

        return result;
    }

    public Map<String, Double> getAverageVaccinationsPerCapita() {
        // Check cache first
        if (!averageVaccinationsPerCapitaCache.isEmpty()) {
            return averageVaccinationsPerCapitaCache;
        }

        Map<String, List<COVIDRecord>> recordsByZipCode = covidData.stream()
                .collect(Collectors.groupingBy(COVIDRecord::getZipCode));

        Map<String, Double> result = recordsByZipCode.entrySet().stream()
                .filter(entry -> populationProcessor.getPopulationByZipCode(entry.getKey()) > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String zipCode = entry.getKey();
                            List<COVIDRecord> records = entry.getValue();
                            int population = populationProcessor.getPopulationByZipCode(zipCode);
                            if (population == 0)
                                return 0.0;

                            double totalVaccinations = records.stream()
                                    .mapToDouble(
                                            record -> record.getPartiallyVaccinated() + record.getFullyVaccinated())
                                    .sum();

                            return totalVaccinations / (records.size() * population);
                        }));

        // Store result in cache
        averageVaccinationsPerCapitaCache.putAll(result);

        return result;
    }

    public Map<String, String> getVaccinationsPerCapitaAndSortByLivableArea() {
        if (!averageVaccinationsPerCapitaCache.isEmpty() && !propertyProcessor.getTotalLivableAreaCache().isEmpty()) {
            return createSortedMapByLivableArea();
        }

        // Group COVID Records by Zip Code
        Map<String, List<COVIDRecord>> recordsByZipCode = covidData.stream()
                .collect(Collectors.groupingBy(COVIDRecord::getZipCode));

        // Calculate average vaccinations per capita
        Map<String, Double> averageVaccinationsPerCapita = recordsByZipCode.entrySet().stream()
                .filter(entry -> populationProcessor.getPopulationByZipCode(entry.getKey()) > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            String zipCode = entry.getKey();
                            List<COVIDRecord> records = entry.getValue();
                            int population = populationProcessor.getPopulationByZipCode(zipCode);
                            if (population == 0)
                                return 0.0;

                            double totalVaccinations = records.stream()
                                    .mapToDouble(
                                            record -> record.getPartiallyVaccinated() + record.getFullyVaccinated())
                                    .sum();

                            return totalVaccinations / (records.size() * population);
                        }));

        // Calculate total livable area per ZIP Code
        Map<String, Double> totalLivableArea = propertyProcessor.getTotalLivableAreaByZipCode();

        // Store results in cache
        averageVaccinationsPerCapitaCache.putAll(averageVaccinationsPerCapita);
        propertyProcessor.getTotalLivableAreaCache().putAll(totalLivableArea);

        return createSortedMapByLivableArea();
    }

    
private Map<String, String> createSortedMapByLivableArea() {
    return propertyProcessor.getTotalLivableAreaCache().entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> {
                        String zipCode = entry.getKey();
                        double avgVaccinations = averageVaccinationsPerCapitaCache.getOrDefault(zipCode, 0.0);
                        double livableArea = entry.getValue();
                        return String.format("Vaccinations Per Capita: %.4f, Total Livable Area: %.4f", avgVaccinations, livableArea);
                    },
                    (e1, e2) -> e1, // In case of key collision, keep the existing entry
                    LinkedHashMap::new // Collect to LinkedHashMap to maintain sorted order
            ));
    }
}
