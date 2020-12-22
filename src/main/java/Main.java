import Entity.SearchResult;
import Entity.User;
import database.DataRepository;
import database.DatabaseConnexion;
import database.exceptions.HydrationException;

import java.sql.SQLException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        try {

            String[] tokens = {"Uti", "Bas"};
            Collection<SearchResult> users = repo.textSearch(SearchResult.class, tokens);

            users.forEach(System.out::println);

        } catch (SQLException | HydrationException throwables) {

            throwables.printStackTrace();

        }

        conn.closeConnexion();

    }

}
