package database.entity;

import database.annotations.Column;
import database.annotations.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Provides helpers to get metadata from annotation of an entity
 */
public class EntityAnnotationReflector {

    /**
     * @return The table name declared in the Table annotation of the entity
     */
    public static <T extends Entity> String getTableName(Class<T> entityClass) {

        return entityClass.getDeclaredAnnotation(Table.class).value();

    }

    /**
     * @return The columns names declared in the Field annotations of the entity fields
     */
    public static <T extends Entity> Stream<String> getColumnsNames(Class<T> entityClass) {

        return Arrays.stream(entityClass.getDeclaredFields())
                     .map(
                             field -> field.getDeclaredAnnotation(Column.class)
                                           .value()
                     );

    }

    /**
     * @return The columns names declared in the Field annotations of the entity fields
     */
    public static <T extends Entity> Stream<Field> getColumnAnnotatedFields(Class<T> entityClass) {

        return Arrays.stream(entityClass.getDeclaredFields())
                     // Filter the declared fields to keep only Column annotated fields
                     .filter(field -> field.isAnnotationPresent(Column.class));

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
