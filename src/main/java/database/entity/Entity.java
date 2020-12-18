package database.entity;

import java.sql.ResultSet;

public abstract class Entity {

    public Entity(ResultSet dbResults) {

        System.out.println("You need to implement the Entity hydration constructor");

    }

    protected Entity() {
    }
}
