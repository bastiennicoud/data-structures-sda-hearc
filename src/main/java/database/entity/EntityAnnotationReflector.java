package database.entity;

import database.annotations.Field;
import database.annotations.Table;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Provides helpers to get metadata from annotation of an entity
 * TODO: Make static methods for reflection helpers
 */
public class EntityAnnotationReflector {

    /**
     * @return The table name declared in the Table annotation of the entity
     */
    public static <T extends Entity> String getTableName(Class<T> entityClass) {

        return entityClass.getDeclaredAnnotation(Table.class).value();

    }

    /**
     * @return The columns names declared in the Field annotations of the entity
     */
    public static <T extends Entity> Stream<String> getColumnsNames(Class<T> entityClass) {

        return Arrays.stream(entityClass.getDeclaredFields())
                     .map(
                             field -> field.getDeclaredAnnotation(Field.class)
                                           .value()
                     );


    }

}
