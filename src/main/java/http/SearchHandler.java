package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DataRepository;
import database.exceptions.HydrationException;
import entities.SearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class SearchHandler implements HttpHandler {

    private final DataRepository dataRepository;

    private final ObjectMapper mapper;

    public SearchHandler(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Search request");

        var body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining());

        System.out.println(body);

        try {

            var search = mapper.readTree(
                    body
            );

            var results = dataRepository.textSearch(
                    SearchResult.class,
                    search.get("needle").asText().split(" ")
            );

            var searchResponse = mapper.writeValueAsString(results)
                                       .getBytes(StandardCharsets.UTF_8);

            var responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/json; charset=UTF-8");

            exchange.sendResponseHeaders(200, searchResponse.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(searchResponse);
            }

        } catch (SQLException | HydrationException throwables) {
            System.out.println("Error while making the search");
            throwables.printStackTrace();
        }
    }

}
