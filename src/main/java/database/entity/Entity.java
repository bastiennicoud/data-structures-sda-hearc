package database.entity;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.stream.Stream;

public abstract class Entity {

    public Entity(ResultSet dbResults) {

        System.out.println("You need to implement the Entity hydration constructor");

        // Iterates the fields for the values annotated with field annotation
        // Call dynamically the methods with the values form the result set

        // Retrieve all the fields annotated with column
        Stream<Field> fields = EntityAnnotationReflector.getColumnAnnotatedFields(this.getClass());

        fields.forEach(f -> {
            // Check the field type to use the correct method to retrieve the column from the result set
            f.set(this, dbResults.getType());
        });

    }

    protected Entity() {

    }

}
