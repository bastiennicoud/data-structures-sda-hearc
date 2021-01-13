package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


/**
 * Add's some common methods for httphandlers
 */
public abstract class HttpHandler implements com.sun.net.httpserver.HttpHandler {

    /**
     * Little helper to send http errors to the client
     */
    protected void sendHttpError(
            HttpExchange exchange,
            int httpCode,
            String error
    ) throws IOException {

        var response = error.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(httpCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }

    }

}
