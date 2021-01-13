import com.github.javafaker.Faker;
import database.DataRepository;
import database.DatabaseConnection;
import database.exceptions.HydrationException;
import entities.User;

import java.sql.SQLException;

public class FakeDatasGenerator {

    public static void main(String[] args)
    throws SQLException, HydrationException, NoSuchFieldException, IllegalAccessException {

        // initialize a connexion to the DB
        DatabaseConnection conn = new DatabaseConnection();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        Faker faker = new Faker();
        int i = 0;
        while (i <= 2) {
            User usr = new User(
                    faker.name().firstName(),
                    faker.name().lastName()
            );
            repo.insertNew(usr);
            i++;
        }
    }

}
