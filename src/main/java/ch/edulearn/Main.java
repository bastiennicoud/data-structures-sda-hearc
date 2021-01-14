package ch.edulearn;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.DatabaseConnection;
import ch.edulearn.http.HomePageHandler;
import ch.edulearn.http.ScriptHandler;
import ch.edulearn.http.SearchHandler;
import ch.edulearn.http.StyleSheetHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

/**
 * EduLearn ch.edulearn.Main
 * <p>
 * This app is a simple ch.edulearn.http server that provides an accès to the search
 * page of edulearn.
 * The search is performed in an sqlite databse
 */
public class Main {

    // Host on which the server will listen
    static final String HOST_NAME = "localhost";

    // Listen port for the server
    static final int PORT = 8080;

    public static void main(String[] args) {

        System.out.println("Starting EduLearn !");

        // Try to initialize the ch.edulearn.database connexion and the ch.edulearn.http server
        try {

            // Initialize ch.edulearn.database connection
            // The connection is not closed here because whe want to reuse it for
            // each ch.edulearn.http request
            DatabaseConnection conn = new DatabaseConnection();

            // Create the repository for future DB call's
            DataRepository repo = new DataRepository(conn.getDbConnection());

            startHttpServer(repo);

            System.out.printf(
                    "Http server started on %1$s port %2$s%n",
                    HOST_NAME,
                    PORT
            );
            System.out.println("Go to your browser and try search on EduLearn !");
            System.out.println("You can stop the server with ctrl+c");

        } catch (SQLException e) {

            System.out.println("An SQLException occurred when trying to start the ch.edulearn.database connexion :");
            e.printStackTrace();

        } catch (IOException e) {

            System.out.println("An IOException occurred when creating the HttpServer :");
            e.printStackTrace();

        }
    }

    /**
     * Start the Edulearn Http server
     *
     * @param repo An initialized ch.edulearn.database repository
     * @throws IOException If the server cant be created
     */
    private static void startHttpServer(DataRepository repo) throws IOException {

        // Initialize a simple java Httpserver
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST_NAME, PORT), 0);

        // Register ch.edulearn.http handlers
        // Homepage (return the resources/homepage.html file)
        server.createContext("/", new HomePageHandler());
        // Handler for the stylesheet (resources/app.css file
        server.createContext("/stylesheet", new StyleSheetHandler());
        // Handler for the script (resources/script.js)
        server.createContext("/javascript", new ScriptHandler());
        // Search endpoint handler
        // Accept POST request with search needle in json body
        // Return a json payload wiht the matching results
        server.createContext("/search", new SearchHandler(repo));

        server.start();

    }

}
