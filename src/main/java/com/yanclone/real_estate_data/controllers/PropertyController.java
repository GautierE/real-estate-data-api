package com.yanclone.real_estate_data.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yanclone.real_estate_data.models.Property;
import com.yanclone.real_estate_data.services.PropertyService;

import java.io.IOException;
import java.io.InputStream;
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
        try {


            String method = exchange.getRequestMethod();
            String endpoint = exchange.getRequestURI().getPath();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            ResponseData responseData;

            if (endpoint.endsWith("/properties")) {
                responseData = handlePropertiesEndpoint(method, objectMapper);
            } else if (endpoint.endsWith("/property")) {
                responseData = handlePropertyEndpoint(method, exchange, objectMapper);
            } else {
                responseData = new ResponseData(404, "Invalid endpoint");
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseData.getStatusCode(), responseData.getResponse().getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseData.getResponse().getBytes());
            outputStream.close();
            exchange.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private ResponseData handlePropertiesEndpoint(String method, ObjectMapper objectMapper) throws IOException {
        String response;
        int statusCode = 200;

        if (method.equalsIgnoreCase("GET")) {
            List<Property> properties = propertyService.getAllProperties();
            response = objectMapper.writeValueAsString(properties);
        } else {
            response = "Invalid method for /properties endpoint";
            statusCode = 400;
        }

        return new ResponseData(statusCode, response);
    }

    private ResponseData handlePropertyEndpoint(String method, HttpExchange exchange, ObjectMapper objectMapper) throws IOException {
        String response;
        int statusCode = 200;

        if (method.equalsIgnoreCase("POST")) {
            Property newProperty = getPropertyFromRequest(exchange);
            Property createdProperty = propertyService.createProperty(newProperty);
            response = objectMapper.writeValueAsString(createdProperty);
        } else if (method.equalsIgnoreCase("GET")) {
            String propertyId = extractPropertyIdFromRequest(exchange);
            Property retrievedProperty = propertyService.getPropertyById(propertyId);
            if (retrievedProperty != null) {
                response = objectMapper.writeValueAsString(retrievedProperty);
            } else {
                response = "Property not found";
                statusCode = 404;
            }
        } else if (method.equalsIgnoreCase("PUT")) {
            String propertyId = extractPropertyIdFromRequest(exchange);
            Property updatedProperty = getPropertyFromRequest(exchange);
            Property updated = propertyService.updateProperty(propertyId, updatedProperty);
            if (updated != null) {
                response = objectMapper.writeValueAsString(updated);
            } else {
                response = "Failed to update property";
                statusCode = 400;
            }
        } else if (method.equalsIgnoreCase("DELETE")) {
            String propertyId = extractPropertyIdFromRequest(exchange);
            boolean deleted = propertyService.deleteProperty(propertyId);
            if (deleted) {
                response = "Property deleted";
            } else {
                response = "Failed to delete property";
                statusCode = 400;
            }
        } else {
            response = "Invalid method for /property endpoint";
            statusCode = 400;
        }

        return new ResponseData(statusCode, response);
    }

    private static Property getPropertyFromRequest(HttpExchange exchange) throws IOException {
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

        return new Property(propertyId, propertyType, address, city, state, postalCode,
                price, bedrooms, bathrooms, yearBuilt);
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

class ResponseData {
    private final String response;
    private final int statusCode;

    public ResponseData(int statusCode, String response)
    {
        this.statusCode = statusCode;
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponse() {
        return response;
    }
}
