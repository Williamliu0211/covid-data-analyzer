package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PopulationDataParser {
    private String filename;

    public PopulationDataParser(String filename) {
        this.filename = filename;
    }

    public Map<String, Integer> parseData() {
        Map<String, Integer> populationData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                String zipCode = fields[0].trim().replace("\"", "");
                int population = Integer.parseInt(fields[1]);
                populationData.put(zipCode, population);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return populationData;
    }
}
