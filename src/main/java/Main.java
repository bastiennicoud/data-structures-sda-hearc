import com.sun.net.httpserver.HttpServer;
import database.DataRepository;
import database.DatabaseConnexion;
import http.HomePageHandler;
import http.ScriptHandler;
import http.SearchHandler;
import http.StyleSheetHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    static final String HOST_NAME = "localhost";

    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        // Tes simple web server for edulearn search bar
        // We dont pass a executor to the server becose we will not use threaded request handling
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST_NAME, PORT), 0);
        // Register handlers
        server.createContext("/", new HomePageHandler());
        server.createContext("/stylesheet", new StyleSheetHandler());
        server.createContext("/javascript", new ScriptHandler());
        server.createContext("/search", new SearchHandler(repo));
        server.start();

        System.out.printf(
                "Http server started on %1$s port %2$s%n",
                HOST_NAME,
                PORT
        );
        System.out.println("You can stop the server with ctrl+c");

    }

}
