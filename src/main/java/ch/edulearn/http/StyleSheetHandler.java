package ch.edulearn.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Objects;

/**
 * Return the css stylesheet
 */
public class StyleSheetHandler extends HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try (
                // Open the stream to read the homepage css file and the output stream to send the data via http
                var os = exchange.getResponseBody();
                var resourceStream = getClass().getClassLoader().getResourceAsStream("app.css")
        ) {

            // Get al the bytes from the css file stream
            var file = Objects.requireNonNull(resourceStream).readAllBytes();

            // Set the response headers and send the headers
            var responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/css; charset=UTF-8");
            exchange.sendResponseHeaders(200, file.length);

            // Write the datas from the css file to the output stream
            os.write(file);

        } catch (NullPointerException e) {

            sendHttpError(
                    exchange,
                    404,
                    "File not found."
            );

            System.out.println("Error while reading the resource stream");
            e.printStackTrace();
        }

    }

}
