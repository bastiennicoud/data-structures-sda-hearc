package database.entity;

import database.annotations.Field;
import database.annotations.Table;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Provides helpers to get metadata from annotation of an entity
 * @param <E> An entity
 */
public class EntityAnnotationReflector<E extends Entity> {

    private final Class<E> entityClass;

    public EntityAnnotationReflector(Class<E> entityClass) {

        this.entityClass = entityClass;

    }

    /**
     * @return The table name declared in the Table annotation of the entity
     */
    public String getTableName() {

        return entityClass.getDeclaredAnnotation(Table.class).value();

    }

    /**
     * @return The columns names declared in the Field annotations of the entity
     */
    public Stream<String> getColumnsNames() {

        return Arrays.stream(entityClass.getDeclaredFields())
                     .map(
                             field -> field.getDeclaredAnnotation(Field.class)
                                           .value()
                     );


    }

}
