package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Container to abastract a JDBC connexion to sqlite databas
 * Implement AutoClosable, therefore it can be used in a try with resources
 */
public class DatabaseConnexion implements AutoCloseable {

    // SQLite database file (relative from project root)
    private final String url = "jdbc:sqlite:./src/main/resources/database.db";

    private Connection dbConnection = null;

    /**
     * Initialise a connexion to the db when creating an instance
     */
    public DatabaseConnexion() throws SQLException {

        this.dbConnection = DriverManager.getConnection(this.url);

        System.out.println("Connexion established to database.");

    }

    /**
     * Close the underlying database connexion
     */
    @Override
    public void close() throws SQLException {

        if (this.dbConnection != null) {
            this.dbConnection.close();
        }
    }

    public String getUrl() {

        return url;
    }

    public Connection getDbConnection() {

        return dbConnection;
    }


}
