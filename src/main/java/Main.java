import com.sun.net.httpserver.HttpServer;
import database.exceptions.HydrationException;
import http.HomePageHandler;
import http.SearchHandler;
import http.StyleSheetHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main {

    static final String HOST_NAME = "localhost";

    static final int PORT = 8080;

    public static void main(String[] args) throws SQLException, HydrationException, IOException {

        // Tes simple web server for edulearn search bar
        // We dont pass a executor to the server becose we will not use threaded request handling
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST_NAME, PORT), 0);
        // Register handlers
        server.createContext("/", new HomePageHandler());
        server.createContext("/stylesheet", new StyleSheetHandler());
        server.createContext("/search", new SearchHandler());
        server.start();

        System.out.printf(
                "Http server started on %1$s port %2$s%n",
                HOST_NAME,
                PORT
        );
        System.out.println("You can stop the server with ctrl+c");

        // initialize a connexion to the DB
        //DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        // DataRepository repo = new DataRepository(conn.getDbConnection());

        //try {

//            String[] tokens = "Bas".split(" ");
//            Collection<SearchResult> users = repo.textSearch(SearchResult.class, tokens);
//
//            users.forEach(f -> System.out.println("Type : " + f.type + " Titre : " + f.title));
//
//            User baba = repo.findById(User.class, 4);
//            System.out.println(baba.firstName);


//        Faker faker = new Faker();
//        int i = 0;
//        while (i <= 100) {
//            User usr = new User(
//                    faker.name().firstName(),
//                    faker.name().lastName()
//            );
//            usr = repo.insertNew(usr);
//            i++;
//        }

//        } catch (SQLException | HydrationException throwables) {
//
//            throwables.printStackTrace();
//
//        }

        //conn.closeConnexion();

    }

}
