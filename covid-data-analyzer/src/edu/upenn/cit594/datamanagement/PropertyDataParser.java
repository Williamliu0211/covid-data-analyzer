package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.upenn.cit594.util.PropertyRecord;

public class PropertyDataParser {
    private String filename;

    public PropertyDataParser(String filename) {
        this.filename = filename;
    }

    public List<PropertyRecord> parseData() {
        List<PropertyRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length < 78) {
                    continue;
                }


                try {
                    
                    String zipCode = fields[73].trim();
                    
                    if (zipCode.length() >= 5) {
                        zipCode = zipCode.substring(0,5);
                    } else {
                        continue;
                    }

                    double marketValue = Double.parseDouble(fields[35]);
                    double livableArea = Double.parseDouble(fields[65]);
                    PropertyRecord record = new PropertyRecord(zipCode, marketValue, livableArea);
                    records.add(record);
                } catch (NumberFormatException e) {
                    continue;
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
