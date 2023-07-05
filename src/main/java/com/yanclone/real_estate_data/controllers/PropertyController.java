package com.yanclone.real_estate_data.controllers;

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

        if (method.equalsIgnoreCase("GET")) {
            List<Property> properties = propertyService.getAllProperties();

            // Convert properties to JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            response = objectMapper.writeValueAsString(properties);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
