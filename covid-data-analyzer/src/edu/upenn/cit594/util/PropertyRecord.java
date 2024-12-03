package edu.upenn.cit594.util;

public class PropertyRecord {
    private String zipCode;
    private double marketValue;
    private double livableArea;

    public PropertyRecord(String zipCode, double marketValue, double livableArea) {
        this.zipCode = zipCode;
        this.marketValue = marketValue;
        this.livableArea = livableArea;
    }

    public String getZipCode() {
        return zipCode;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getLivableArea() {
        return livableArea;
    }

    public void print() {
        System.out.printf("ZIP Code: %s, Market Value: %.2f, Livable Area: %.2f%n", zipCode, marketValue, livableArea);
    }
}
