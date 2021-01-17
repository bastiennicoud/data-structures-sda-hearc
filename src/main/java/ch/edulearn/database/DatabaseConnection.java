package ch.edulearn.database;

import ch.edulearn.database.exceptions.DatabaseConnexionException;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Container to abastract a JDBC connexion to sqlite databas
 * Implement AutoClosable, therefore it can be used in a try with resources
 */
public class DatabaseConnection implements AutoCloseable {

    private final Connection dbConnection;

    // SQLite ch.edulearn.database file (relative from project root)
    private String connectionString = "jdbc:sqlite::resource:";

    /**
     * Initialise a connexion to the db when creating an instance
     */
    public DatabaseConnection() throws DatabaseConnexionException {

        try {
            // Dynamically retrieve the path of the databse resource
            connectionString += Objects.requireNonNull(
                    getClass().getClassLoader().getResource("database.db")
            );
            System.out.println(connectionString);

            SQLiteConfig c = new SQLiteConfig();
            c.setEncoding(SQLiteConfig.Encoding.UTF8);

            this.dbConnection = DriverManager.getConnection(
                    connectionString,
                    c.toProperties()
            );

            System.out.println("Connexion established to ch.edulearn.database.");

        } catch (NullPointerException e) {
            throw new DatabaseConnexionException(
                    "Impossible to find the sqlite database file in the resources folder.",
                    e
            );
        } catch (SQLException e) {
            throw new DatabaseConnexionException(
                    "Impossible to establish the connexion to database.",
                    e
            );
        }

    }

    /**
     * Close the underlying ch.edulearn.database connexion
     */
    @Override
    public void close() throws SQLException {

        this.dbConnection.close();

    }

    public String getConnectionString() {

        return connectionString;
    }

    public Connection getDbConnection() {

        return dbConnection;
    }

}
