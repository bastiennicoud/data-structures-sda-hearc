package database.entity;

import database.entity.annotations.Column;
import database.entity.annotations.Identity;
import database.entity.annotations.Searchable;
import database.entity.annotations.Table;
import database.entity.reflector.Reflector;
import database.exceptions.NoMatchingEntityAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides helpers to get metadata from annotation of an entity
 */
public class EntityAnnotationReflector {

    /**
     * @return The table name declared in the Table annotation of the entity
     */
    public static <T extends Entity> String getTableName(Class<T> entityClass) {

        Reflector.of(entityClass).is(Table.class).not(Searchable.class).value(Table.class);
        return entityClass.getDeclaredAnnotation(Table.class).value();

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public static <T extends Entity> Stream<Field> getColumnAnnotatedFields(Class<T> entityClass) {

        return Arrays.stream(entityClass.getDeclaredFields())
                     // Filter the declared fields to keep only Column annotated fields
                     .filter(field -> field.isAnnotationPresent(Column.class));

    }

    /**
     * @return The columns names declared in the Field annotations of the entity fields
     */
    public static <T extends Entity> Stream<String> getColumnsNames(Class<T> entityClass) {

        return getColumnAnnotatedFields(entityClass)
                     .map(
                             field -> field.getDeclaredAnnotation(Column.class)
                                           .value()
                     );

    }

    /**
     * @return The columns names declared in the Field annotations of the entity fields
     */
    public static <T extends Entity> Stream<String> getSearchableColumnsNames(Class<T> entityClass) {

        return getColumnAnnotatedFields(entityClass)
                .filter(field -> field.isAnnotationPresent(Searchable.class))
                .map(
                        field -> field.getDeclaredAnnotation(Column.class)
                                      .value()
                );

    }

    /**
     * @return The columns names declared in the Field annotations of the entity fields
     */
    public static <T extends Entity> String getIdentityColumnName(Class<T> entityClass) throws Exception {

        Optional<Field> identityField = getColumnAnnotatedFields(entityClass)
                .filter(field -> field.isAnnotationPresent(Identity.class))
                // Get the first element of the stream
                .findFirst();

        // Check if there is a identity field retrieved by the find first
        if (identityField.isPresent())
            return identityField.get().getDeclaredAnnotation(Column.class).value();

        // If theres no field with @identity throw
        throw new NoMatchingEntityAnnotation("This entity does not contain a field identified with @Identity");

    }

    /**
     * Get the constructor of the entity that allows hydration with java.sql.ResultSet
     *
     * @return The constructor instance for entity hydration with a result set
     * @throws NoSuchMethodException If the passed Entity does not have
     *                               a constructor that match the ResultSet signature.
     */
    public static <T extends Entity> Constructor<T> getEntityHydratorConstructor(Class<T> entityClass)
    throws NoSuchMethodException {

        return entityClass.getConstructor(ResultSet.class);

    }

}
