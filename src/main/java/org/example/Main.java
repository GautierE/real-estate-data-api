package org.example;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Set the port number

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
