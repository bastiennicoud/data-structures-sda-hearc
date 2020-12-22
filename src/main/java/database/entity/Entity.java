package database.entity;

import database.exceptions.HydrationFieldException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public abstract class Entity {

    /**
     * Constructor tha auto fill the Entity fields using reflexion.
     * Fill only the fields annotated with Column annotation.
     * Check supported types in the EntityHydrator class.
     * Surcharge this constructor if you need specific behaviors when creating the entity from a result set
     */
    protected Entity(ResultSet dbResults) {

        // Retrieve all the fields annotated with Column annotation
        Stream<Field> fields = EntityAnnotationReflector.getColumnAnnotatedFields(this.getClass());

        // Iterates the fields and tries to hydrate the value from the reflected column name
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
