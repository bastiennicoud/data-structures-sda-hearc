package entities;

import database.annotations.Column;
import database.annotations.Table;
import database.entity.Entity;

import java.sql.ResultSet;

@Table("users")
public class User extends Entity {

    @Column("user_id")
    public int id;

    @Column("first_name")
    public String firstName;

    @Column("last_name")
    public String lastName;

    /**
     * Call the parent constructor for auto hydration from a result set
     */
    public User(ResultSet dbResults) {

        super(dbResults);

    }

}
