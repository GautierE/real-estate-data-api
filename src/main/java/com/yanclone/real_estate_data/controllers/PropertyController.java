package com.yanclone.real_estate_data.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yanclone.real_estate_data.models.Property;
import com.yanclone.real_estate_data.services.PropertyService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PropertyController implements HttpHandler {
    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        // Convert properties to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        if (exchange.getRequestURI().getPath().endsWith("/properties") && method.equalsIgnoreCase("GET")) {
            List<Property> properties = propertyService.getAllProperties();
            response = objectMapper.writeValueAsString(properties);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
        } else if (exchange.getRequestURI().getPath().endsWith("/property")) {
            if (method.equalsIgnoreCase("POST")) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

                int propertyId = jsonObject.get("propertyId").getAsInt();
                String propertyType = jsonObject.get("propertyType").getAsString();
                String address = jsonObject.get("address").getAsString();
                String city = jsonObject.get("city").getAsString();
                String state = jsonObject.get("state").getAsString();
                int postalCode = jsonObject.get("postalCode").getAsInt();
                float price = jsonObject.get("price").getAsFloat();
                int bedrooms = jsonObject.get("bedrooms").getAsInt();
                int bathrooms = jsonObject.get("bathrooms").getAsInt();
                int yearBuilt = jsonObject.get("yearBuilt").getAsInt();

                Property newProperty = new Property(propertyId, propertyType, address, city, state, postalCode,
                        price, bedrooms, bathrooms, yearBuilt);


                Property createdProperty = propertyService.createProperty(newProperty);

                response = objectMapper.writeValueAsString(createdProperty);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
            } else if (method.equalsIgnoreCase("GET")) {
                String propertyId = extractPropertyIdFromRequest(exchange);

                Property retrievedProperty = propertyService.getPropertyById(propertyId);

                if (retrievedProperty != null) {
                    response = objectMapper.writeValueAsString(retrievedProperty);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                } else {
                    response = "Property not found";
                }
            } else if (method.equalsIgnoreCase("PUT")) {
                String propertyId = extractPropertyIdFromRequest(exchange);
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

                String propertyType = jsonObject.get("propertyType").getAsString();
                String address = jsonObject.get("address").getAsString();
                String city = jsonObject.get("city").getAsString();
                String state = jsonObject.get("state").getAsString();
                int postalCode = jsonObject.get("postalCode").getAsInt();
                float price = jsonObject.get("price").getAsFloat();
                int bedrooms = jsonObject.get("bedrooms").getAsInt();
                int bathrooms = jsonObject.get("bathrooms").getAsInt();
                int yearBuilt = jsonObject.get("yearBuilt").getAsInt();

                Property updatedProperty = new Property(Integer.parseInt(propertyId), propertyType, address, city, state, postalCode,
                        price, bedrooms, bathrooms, yearBuilt);

                Property updated = propertyService.updateProperty(propertyId, updatedProperty);

                if (updated != null) {
                    response = objectMapper.writeValueAsString(updated);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                } else {
                    response = "Failed to update property";
                }
            } else if (method.equalsIgnoreCase("DELETE")) {
                String propertyId = extractPropertyIdFromRequest(exchange);

                boolean deleted = propertyService.deleteProperty(propertyId);

                if (deleted) {
                    response = "Property deleted";
                } else {
                    response = "Failed to delete property";
                }
            }
        }


        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private String extractPropertyIdFromRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String propertyId = null;
        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("propertyId")) {
                    propertyId = keyValue[1];
                    break;
                }
            }
        }

        return propertyId;
    }
}
