package com.yanclone.real_estate_data.models;

public class Property {
    private final int propertyId;
    private final String propertyType;
    private final String address;
    private final String city;
    private final String state;
    private final int postalCode;
    private final double price;
    private final int bedrooms;
    private final int bathrooms;
    private final int yearBuilt;

    private final float latitude;
    private final float longitude;


    public Property(int propertyId, String propertyType, String address, String city, String state, int postalCode,
                    double price, int bedrooms, int bathrooms, int yearBuilt, float latitude, float longitude) {
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
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getPrice() {
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

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}