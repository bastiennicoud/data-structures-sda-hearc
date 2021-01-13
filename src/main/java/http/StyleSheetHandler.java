package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class StyleSheetHandler extends HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        File file = new File("./src/main/resources/app.css");

        var responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/css; charset=UTF-8");

        exchange.sendResponseHeaders(200, file.length());

        try (OutputStream os = exchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        }

    }

}
