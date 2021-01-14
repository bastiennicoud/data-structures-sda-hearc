package ch.edulearn;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.DatabaseConnection;
import ch.edulearn.database.SchemaDatabaseRepository;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;
import ch.edulearn.entities.SearchResult;
import ch.edulearn.entities.User;
import com.github.javafaker.Faker;

import java.sql.SQLException;

/**
 * This little script rebuild the database form ground using faker to generate some custom datas
 */
public class FakeDatasGenerator {

    // This script allows to generate a test ch.edulearn.database with fake datas
    public static void main(String[] args)
    throws SQLException, SqlQueryFormattingException {

        // initialize a connexion to the DB
        DatabaseConnection conn = new DatabaseConnection();

        generateDatabaseSchema(conn);

        // generateFakeDatas(conn);
    }

    /**
     * Generate the database schema
     */
    private static void generateDatabaseSchema(DatabaseConnection conn)
    throws SQLException, SqlQueryFormattingException {

        // Create the repository for future DB call's
        var repo = new SchemaDatabaseRepository(conn.getDbConnection());

        repo.createTable(User.class);
        repo.createFTS5Table(SearchResult.class);
    }

    /**
     * Fill the database tables with some fake datas
     */
    private static void generateFakeDatas(DatabaseConnection conn)
    throws SQLException, SqlQueryFormattingException {

        // Create the repository for future DB call's
        var repo = new DataRepository(conn.getDbConnection());

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
