package ch.edulearn.database;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Container to abastract a JDBC connexion to sqlite databas
 * Implement AutoClosable, therefore it can be used in a try with resources
 */
public class DatabaseConnection implements AutoCloseable {

    // SQLite ch.edulearn.database file (relative from project root)
    private final String connectionString = "jdbc:sqlite:./src/main/resources/ch.edulearn.database.db";

    private final Connection dbConnection;

    /**
     * Initialise a connexion to the db when creating an instance
     */
    public DatabaseConnection() throws SQLException {

        SQLiteConfig c = new SQLiteConfig();
        c.setEncoding(SQLiteConfig.Encoding.UTF8);

        this.dbConnection = DriverManager.getConnection(
                connectionString,
                c.toProperties()
        );

        System.out.println("Connexion established to ch.edulearn.database.");

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
