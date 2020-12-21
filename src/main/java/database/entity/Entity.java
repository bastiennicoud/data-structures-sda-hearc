package database.entity;

import database.exceptions.HydrationFieldException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public abstract class Entity {

    protected Entity(ResultSet dbResults) {

        // Iterates the fields for the values annotated with field annotation
        // Call dynamically the methods with the values form the result set

        // Retrieve all the fields annotated with column
        Stream<Field> fields = EntityAnnotationReflector.getColumnAnnotatedFields(this.getClass());

        fields.forEach(f -> {
            try {
                f.set(this, EntityHydrator.hydrateField(f, dbResults));
            } catch (IllegalAccessException | SQLException | HydrationFieldException e) {
                System.out.println("Hydration error");
                e.printStackTrace();
            }
        });

    }

    protected Entity() {

    }

}
