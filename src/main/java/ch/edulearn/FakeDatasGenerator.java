package ch.edulearn;

import ch.edulearn.database.DataRepository;
import ch.edulearn.database.DatabaseConnection;
import ch.edulearn.database.SchemaDatabaseRepository;
import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Table;
import ch.edulearn.database.entity.reflector.Reflector;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;
import ch.edulearn.entities.Course;
import ch.edulearn.entities.Document;
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

    private static DatabaseConnection conn;

    private static DataRepository repo;

    private static Faker faker;

    // This script allows to generate a test ch.edulearn.database with fake datas
    public static void main(String[] args)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        // initialize a connexion to the DB
        conn = new DatabaseConnection();

        // Create the repository for future DB call's
        repo = new DataRepository(conn.getDbConnection());

        // Library to generate on the fly fake datas
        faker = new Faker(Locale.FRENCH);

        generateDatabaseSchema();

        generateFakeDatas();
    }

    /**
     * Generate the database schema
     */
    private static void generateDatabaseSchema()
    throws SQLException, SqlQueryFormattingException {

        // Create the repository for future DB call's
        var schemaRepo = new SchemaDatabaseRepository(conn.getDbConnection());

        // Clean the database
        schemaRepo.dropTableIfExists(User.class);
        schemaRepo.dropTableIfExists(Course.class);
        schemaRepo.dropTableIfExists(Document.class);
        schemaRepo.dropTableIfExists(SearchResult.class);

        // Generate the schema
        schemaRepo.createTable(User.class);
        schemaRepo.createTable(Course.class);
        schemaRepo.createTable(Document.class);
        schemaRepo.createFTS5Table(SearchResult.class);
    }

    /**
     * Fill the database tables with some fake datas
     */
    private static void generateFakeDatas()
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        var users = generateFakeUsers(30);
        var courses = generateFakeCourses(30);
        var docs = generateFakeDocuments(30);

        indexDatasIntoFTS5(users);
        indexDatasIntoFTS5(courses);
        indexDatasIntoFTS5(docs);

    }

    /**
     * Generate some fake users and insert it into the db
     *
     * @return A list of generated users filled with the generated databse id
     */
    private static List<User> generateFakeUsers(int amount)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        var users = new ArrayList<User>(amount);

        for (var i = 0; i < amount; i++) {
            // Add the user in DB and in the list of users
            users.add(repo.insertNew(new User(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.lorem().sentence()
            )));
        }
        return users;
    }

    /**
     * Generate some fake courses and insert it into the db
     *
     * @return A list of generated users filled with the generated databse id
     */
    private static List<Course> generateFakeCourses(int amount)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        var courses = new ArrayList<Course>(amount);

        // Generate some discover courses
        for (var i = 0; i < (amount / 4); i++) {
            // Add the user in DB and in the list of users
            courses.add(repo.insertNew(new Course(
                    "Découvrez " + faker.programmingLanguage().name(),
                    faker.lorem().paragraph()
            )));
        }

        // Generate some advanced courses
        for (var i = 0; i < (amount / 4); i++) {
            // Add the user in DB and in the list of users
            courses.add(repo.insertNew(new Course(
                    "Notions avancées de " + faker.programmingLanguage().name(),
                    faker.lorem().paragraph()
            )));
        }

        // Generate some hands-on courses
        for (var i = 0; i < (amount / 4); i++) {
            // Add the user in DB and in the list of users
            courses.add(repo.insertNew(new Course(
                    "Pratiquons " + faker.programmingLanguage().name(),
                    faker.lorem().paragraph()
            )));
        }

        // Generate some website courses
        for (var i = 0; i < (amount / 4); i++) {
            // Add the user in DB and in the list of users
            courses.add(repo.insertNew(new Course(
                    "Créer son site avec " + faker.programmingLanguage().name(),
                    faker.lorem().paragraph()
            )));
        }
        return courses;
    }

    /**
     * Generate some fake documents and insert it into the db
     *
     * @return A list of generated users filled with the generated databse id
     */
    private static List<Document> generateFakeDocuments(int amount)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        var documents = new ArrayList<Document>(amount);

        for (var i = 0; i < amount; i++) {
            // Add the user in DB and in the list of users
            documents.add(repo.insertNew(new Document(
                    faker.programmingLanguage().name(),
                    faker.lorem().paragraph()
            )));
        }
        return documents;
    }

    /**
     * Add the entities from the provided list in a specific sqlite table for search indexing
     * (Sqlite FTS5 virtual table)
     */
    private static <E extends Entity> void indexDatasIntoFTS5(List<E> entities)
    throws IllegalAccessException, SQLException, SqlQueryFormattingException {

        for (var e : entities) {

            if (e instanceof User) {
                // Users insertion
                var u = (User) e;
                repo.insertNew(new SearchResult(
                        "Utilisateur",
                        u.firstName + " " + u.lastName,
                        u.description,
                        Reflector.of(u.getClass()).getClassAnnotationValue(Table.class).orElseThrow(),
                        u.id
                ));

            } else if (e instanceof Course) {
                // Courses insertion
                var c = (Course) e;
                repo.insertNew(new SearchResult(
                        "Cours",
                        c.title,
                        c.description,
                        Reflector.of(c.getClass()).getClassAnnotationValue(Table.class).orElseThrow(),
                        c.id
                ));
            } else if (e instanceof Document) {
                // Documents insertion
                var d = (Document) e;
                repo.insertNew(new SearchResult(
                        "Document",
                        d.title,
                        d.description,
                        Reflector.of(d.getClass()).getClassAnnotationValue(Table.class).orElseThrow(),
                        d.id
                ));
            }

        }
    }


}
