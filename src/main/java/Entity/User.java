package Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Entity {

    public int id;

    public String first_name;

    public String last_name;


    /**
     * @param id Database id of the user
     * @param first_name First Name
     * @param last_name Last Name
     */
    public User(int id, String first_name, String last_name) {

        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;

    }

    public User(ResultSet dbResults) throws SQLException {

        this(
                dbResults.getInt("user_id"),
                dbResults.getString("first_name"),
                dbResults.getString("last_name")
        );

    }
}
