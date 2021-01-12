import com.github.javafaker.Faker;
import database.DataRepository;
import database.DatabaseConnexion;
import database.exceptions.HydrationException;
import entities.User;

import java.sql.SQLException;

public class FakeDatasGenerator {

    public static void main(String[] args) throws SQLException, HydrationException {

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        Faker faker = new Faker();
        int i = 0;
        while (i <= 2) {
            User usr = new User(
                    faker.name().firstName(),
                    faker.name().lastName()
            );
            usr = repo.insertNew(usr);
            i++;
        }
    }

}
