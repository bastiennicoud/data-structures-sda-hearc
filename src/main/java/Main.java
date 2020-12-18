import Entity.User;
import database.DataRepository;
import database.DatabaseConnexion;
import database.entity.EntityHydrator;
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

        try {

            Collection<User> users = repo.findAll(User.class);

            users.forEach(System.out::println);

        } catch (SQLException | HydrationException throwables) {

            throwables.printStackTrace();

        }

        conn.closeConnexion();

    }

}
