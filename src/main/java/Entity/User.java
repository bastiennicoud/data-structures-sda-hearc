package Entity;

import database.annotations.Column;
import database.annotations.Table;
import database.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Table("users")
public class User extends Entity {

    @Column("user_id")
    public int id;

    @Column("first_name")
    public String firstName;

    @Column("last_name")
    public String lastName;


    /**
     * @param id         Database id of the user
     * @param first_name First Name
     * @param last_name  Last Name
     */
    public User(
            int id,
            String first_name,
            String last_name
    ) {

        this.id = id;
        this.firstName = first_name;
        this.lastName = last_name;

    }


    /**
     * @param dbResults Database results from data repository
     * @throws SQLException
     */
    /*
    public User(ResultSet dbResults) throws SQLException {

        this(
                dbResults.getInt("user_id"),
                dbResults.getString("first_name"),
                dbResults.getString("last_name")
        );

    }*/
}
