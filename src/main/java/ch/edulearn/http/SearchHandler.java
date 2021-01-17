package ch.edulearn.http;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.entity.hydrator.exceptions.HydrationException;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;
import ch.edulearn.entities.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Handles search calls on the /search endoint
 * Return a json payload with the search results
 */
public class SearchHandler extends HttpHandler {

    // Data repo to acces the DB
    private final DataRepository dataRepository;

    // Mapper for json serialisation and deserialisation
    private final ObjectMapper mapper;

    public SearchHandler(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        this.mapper = new ObjectMapper();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Search request handler.");

        try (
                // Open the input stream and try to get a buffer reader on the body
                var bfReader = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody())
                )
        ) {

            // Get the body payload
            var body = bfReader.lines()
                               .collect(Collectors.joining());

            System.out.println("Request body :" + body);

            // Parse the json paylod
            var search = mapper.readTree(
                    body
            );

            // Perform a text search on the DB with all the tokens of the needle
            var results = dataRepository.textSearch(
                    SearchResult.class,
                    search.get("needle").asText().split(" "),
                    30
            );

            // serialise the search results
            var searchResponse = mapper.writeValueAsString(results)
                                       .getBytes(StandardCharsets.UTF_8);

            // Get response headers and set the Content-Type
            var responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/json; charset=UTF-8");

            exchange.sendResponseHeaders(200, searchResponse.length);

            // Write rhe search results on the output stream
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(searchResponse);
            }

        } catch (SQLException | HydrationException | SqlQueryFormattingException e) {

            sendHttpError(
                    exchange,
                    500,
                    "Error during ch.edulearn.database interactions."
            );

            System.out.println("Error while making the search");
            e.printStackTrace();

        } catch (JsonProcessingException e) {

            sendHttpError(
                    exchange,
                    500,
                    "Error while performing json serialisation or deserialization."
            );

            System.out.println("Error while parsing or serializing json");
            e.printStackTrace();

        } catch (Exception e) {

            sendHttpError(
                    exchange,
                    500,
                    "Error while trying to handle the request."
            );

            System.out.println("An exception occurs while trying to handle a search request :");
            e.printStackTrace();

        }
    }

}
