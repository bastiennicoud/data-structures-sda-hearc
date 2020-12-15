import database.DatabaseConnexion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args){

        DatabaseConnexion conn = new DatabaseConnexion();

        String query = "SELECT * FROM users";

        try {

            Statement stmt = conn.getDbConnection().createStatement();
            ResultSet res = stmt.executeQuery(query);

            while (res.next()) {
                System.out.println(
                        res.getString("first_name") +
                        res.getString("last_name")
                );
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        conn.closeConnexion();

    }
}
