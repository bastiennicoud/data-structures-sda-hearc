package ch.edulearn;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.DatabaseConnection;
import ch.edulearn.database.SchemaDatabaseRepository;
import ch.edulearn.database.entity.annotations.Table;
import ch.edulearn.database.entity.reflector.Reflector;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;
import ch.edulearn.entities.SearchResult;
import ch.edulearn.entities.User;
import com.github.javafaker.Faker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This little script rebuild the database form ground using faker to generate some custom datas
 */
public class FakeDatasGenerator {

    // This script allows to generate a test ch.edulearn.database with fake datas
    public static void main(String[] args)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        // initialize a connexion to the DB
        DatabaseConnection conn = new DatabaseConnection();

        generateDatabaseSchema(conn);

        generateFakeDatas(conn);
    }

    /**
     * Generate the database schema
     */
    private static void generateDatabaseSchema(DatabaseConnection conn)
    throws SQLException, SqlQueryFormattingException {

        // Create the repository for future DB call's
        var repo = new SchemaDatabaseRepository(conn.getDbConnection());

        // Clean the database
        repo.dropTableIfExists(User.class);
        repo.dropTableIfExists(SearchResult.class);

        // Generate the schema
        repo.createTable(User.class);
        repo.createFTS5Table(SearchResult.class);
    }

    /**
     * Fill the database tables with some fake datas
     */
    private static void generateFakeDatas(DatabaseConnection conn)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        // Create the repository for future DB call's
        var repo = new DataRepository(conn.getDbConnection());

        // Library to generate on the fly fake datas
        var faker = new Faker(Locale.FRENCH);

        var users = generateFakeUsers(repo, faker, 10);

        // indexDatasIntoFTS5();
        for (var u : users) {
            repo.insertNew(new SearchResult(
                    "Utilisateur",
                    u.firstName + u.lastName,
                    u.description,
                    Reflector.of(u.getClass()).getClassAnnotationValue(Table.class).orElseThrow(),
                    u.id
            ));
        }
    }

    /**
     * Generate some fake users and insert it into the db
     *
     * @return A list of generated users filled with the generated databse id
     */
    private static List<User> generateFakeUsers(DataRepository repo, Faker faker, int amount)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        var users = new ArrayList<User>(20);
        for (var i = 0; i < amount; i++) {
            User usr = new User(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.lorem().sentence()
            );
            users.add(repo.insertNew(usr));
            System.out.println(usr);
        }
        return users;
    }

}
