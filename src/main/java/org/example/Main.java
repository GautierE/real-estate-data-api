package org.example;
import com.sun.net.httpserver.HttpServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.QueryOuterClass;
import io.stargate.proto.StargateGrpc;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException, Exception {
        Dotenv dotenv = null;
        dotenv = Dotenv.configure().load();
        String astra_db_id = dotenv.get("ASTRA_DB_ID");
        String astra_db_region = dotenv.get("ASTRA_DB_REGION");
        String astra_token = dotenv.get("ASTRA_TOKEN");
        String astra_keyspace = dotenv.get("ASTRA_KEYSPACE");

        // Cassandra DB connection
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(astra_db_id + "-" + astra_db_region + ".apps.astra.datastax.com", 443)
                .useTransportSecurity()
                .build();

        StargateGrpc.StargateBlockingStub blockingStub =
                StargateGrpc.newBlockingStub(channel)
                        .withDeadlineAfter(10, TimeUnit.SECONDS)
                        .withCallCredentials(new StargateBearerToken(astra_token));

        QueryOuterClass.Response queryString = blockingStub.executeQuery(QueryOuterClass
                .Query.newBuilder()
                .setCql("SELECT property_id FROM " + astra_keyspace + ".property")
                .build());

        System.out.println(queryString);

        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/hello", (exchange -> {
            String response = "Hello, World!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }));

        server.start();

        System.out.println("Server started on port " + port);
    }
}
