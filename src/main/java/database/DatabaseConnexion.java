package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnexion {

    // SQLite database file (relative from project root)
    private final String url = "jdbc:sqlite:./src/main/resources/database.db";
    private Connection dbConnection = null;

    /**
     * Initialise a connexion to the db when creating an instance
     */
    public DatabaseConnexion() {
        // Try to initialise database connexion
        try {

            this.dbConnection = DriverManager.getConnection(this.url);

            System.out.println("Connexion established to database.");

        } catch (SQLException e) {

            System.out.println(e.getErrorCode() + e.getMessage());

        }
    }

    /**
     * Allow to close the database connexion
     */
    public void closeConnexion() {
        try {

            if (this.dbConnection != null) {
                this.dbConnection.close();
            }

        } catch (SQLException e) {

            System.out.println(e.getErrorCode() + e.getMessage());

        }
    }

    public String getUrl() {
        return url;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }
}
