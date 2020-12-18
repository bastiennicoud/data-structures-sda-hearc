import Entity.User;
import database.DataRepository;
import database.DatabaseConnexion;
import database.EntityHydrator;
import database.exceptions.HydrationException;

import java.sql.SQLException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(
                conn.getDbConnection(),
                new EntityHydrator()
        );

        Collection<User> users = null;

        try {

            users = repo.query("SELECT * FROM users", User.class);

        } catch (SQLException | HydrationException throwables) {

            throwables.printStackTrace();

        }

        Collection<User> userss = repo.findAll(User.class);

        conn.closeConnexion();

        users.forEach(System.out::println);

    }

}
