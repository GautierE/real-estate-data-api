package com.yanclone.real_estate_data.main;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;
import com.yanclone.real_estate_data.controllers.PropertyController;
import com.yanclone.real_estate_data.services.PropertyService;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.configure().load();
        String astraDbId = dotenv.get("ASTRA_DB_ID");
        String astraDbRegion = dotenv.get("ASTRA_DB_REGION");
        String astraToken = dotenv.get("ASTRA_TOKEN");
        String astraKeyspace = dotenv.get("ASTRA_KEYSPACE");

        PropertyService propertyService = new PropertyService(astraDbId, astraDbRegion, astraToken, astraKeyspace);

        PropertyController propertyHandler = new PropertyController(propertyService);

        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/properties", propertyHandler);

        server.start();

        System.out.println("Server started on port " + port);
    }
}
