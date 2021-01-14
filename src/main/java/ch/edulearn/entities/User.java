package ch.edulearn.entities;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.annotations.Identity;
import ch.edulearn.database.entity.annotations.Table;

@Table("users")
public class User extends Entity {

    @Identity
    @Column("user_id")
    public int id;

    @Column("first_name")
    public String firstName;

    @Column("last_name")
    public String lastName;

    @Column("description")
    public String description;

    public User(String firstName, String lastName, String description) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    public User() {

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

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


}
