package edu.upenn.cit594.util;

public class COVIDRecord {
    private String zipCode;
    private String date;
    private int partiallyVaccinated;
    private int fullyVaccinated;

    public COVIDRecord(String zipCode, String date, int partiallyVaccinated, int fullyVaccinated) {
        this.zipCode = zipCode;
        this.date = date;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getDate() {
        return date;
    }

    public int getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public int getFullyVaccinated() {
        return fullyVaccinated;
    }

    public void print() {
        System.out.printf("ZIP Code: %s, Date: %s, Partially Vaccinated: %d, Fully Vaccinated: %d%n", 
                          zipCode, date, partiallyVaccinated, fullyVaccinated);
    }
}
