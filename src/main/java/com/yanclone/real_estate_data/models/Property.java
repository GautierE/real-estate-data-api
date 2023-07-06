package com.yanclone.real_estate_data.models;

public class Property {
    private final int propertyId;
    private final String propertyType;
    private final String address;
    private final String city;
    private final String state;
    private final int postalCode;
    private final float price;
    private final int bedrooms;
    private final int bathrooms;
    private final int yearBuilt;


    public Property(int propertyId, String propertyType, String address, String city, String state, int postalCode,
                    float price, int bedrooms, int bathrooms, int yearBuilt) {
        this.propertyId = propertyId;
        this.propertyType = propertyType;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.yearBuilt = yearBuilt;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public float getPrice() {
        return price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }
}