package ch.edulearn;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.DatabaseConnection;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;
import ch.edulearn.entities.User;
import com.github.javafaker.Faker;

import java.sql.SQLException;

public class FakeDatasGenerator {

    // This script allows to generate a test ch.edulearn.database with fake datas
    public static void main(String[] args)
    throws SQLException, SqlQueryFormattingException {

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
