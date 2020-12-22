package entities;

import database.annotations.Column;
import database.annotations.Identity;
import database.annotations.Table;
import database.entity.Entity;

import java.sql.ResultSet;

@Table("users")
public class User extends Entity {

    @Identity
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

    public User(String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

}
