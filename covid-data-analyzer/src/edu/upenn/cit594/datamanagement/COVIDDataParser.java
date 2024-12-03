package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.COVIDRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class COVIDDataParser {
    private String filename;

    public COVIDDataParser(String filename) {
        this.filename = filename;
    }

    public List<COVIDRecord> parseData() {
        if (filename.toLowerCase().endsWith(".csv")) {
            return parseCSV();
        } else if (filename.toLowerCase().endsWith(".json")) {
            return parseJSON();
        } else {
            throw new IllegalArgumentException("Invalid file type. Only CSV and JSON are supported.");
        }
    }

    private List<COVIDRecord> parseCSV() {
        List<COVIDRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 9) {
                    continue; // Skip invalid records
                }

                
                try{
                    String zipCode = fields[0].trim();
                    String timestamp = fields[8].trim().replace("\"", "");
                    String date = timestamp.split(" ")[0];
                    int partiallyVaccinated = fields[5].isEmpty() ? 0 : Integer.parseInt(fields[5].trim());
                    int fullyVaccinated = fields[6].isEmpty() ? 0 : Integer.parseInt(fields[6].trim());
                    COVIDRecord record = new COVIDRecord(zipCode, date, partiallyVaccinated, fullyVaccinated);
                    records.add(record);
                } catch (NumberFormatException e) {
                    System.err.println("skipping record due to number format issues: " + line);
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private List<COVIDRecord> parseJSON() {
        List<COVIDRecord> records = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            JSONArray jsonArray = (JSONArray) parser.parse(br);
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String zipCode = jsonObject.get("zip_code").toString();
                String timestamp = jsonObject.get("etl_timestamp").toString();
                String date = timestamp.split(" ")[0];
                int partiallyVaccinated = jsonObject.get("partially_vaccinated") == null ? 0 : ((Long) jsonObject.get("partially_vaccinated")).intValue();
                int fullyVaccinated = jsonObject.get("fully_vaccinated") == null ? 0 : ((Long) jsonObject.get("fully_vaccinated")).intValue();
                COVIDRecord record = new COVIDRecord(zipCode, date, partiallyVaccinated, fullyVaccinated);
                records.add(record);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return records;
    }
}
