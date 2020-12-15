package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnexion {

    private final String url = "jdbc:sqlite:./src/main/resources/database.db";
    private Connection dbConnection = null;

    public DatabaseConnexion() {
        // Try to initialise database connexion
        try {

            this.dbConnection = DriverManager.getConnection(this.url);

            System.out.println("Connexion established to database.");

        } catch (SQLException e) {

            System.out.println(e.getErrorCode() + e.getMessage());

        }
    }

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
