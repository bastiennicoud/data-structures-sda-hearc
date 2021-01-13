package database;

import database.entity.Entity;
import database.entity.EntityAnnotationReflector;
import database.entity.annotations.Column;
import database.entity.annotations.Identity;
import database.exceptions.HydrationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class to make call's to the database via shortcuts.
 * Automatically hydrate Entities via reflected attributes.
 * Does not provide verification between SQL returned tuples and entities.
 * You need to provide a SQL query that return the right datas for the matching Entity.
 * Otherwise you risk encountering data inconstancy or hydration errors.
 */
public class DataRepository extends BaseRepository {

    /**
     * @param dbConnection an active connection to the db
     */
    public DataRepository(Connection dbConnection) {

        super(dbConnection);
    }

    /**
     * Shortcut to a select all
     * Find all elements from the table corresponding to the entity
     *
     * @param entityClass The entity you wish to hydrate with the results of the query
     * @return A collection of the entity type
     */
    public <E extends Entity> Collection<E> findAll(Class<E> entityClass)
    throws SQLException, HydrationException {

        // Generate a select query with parameters annotated on the Entity class
        var sql = String.format(
                "SELECT %1$s FROM %2$s",
                EntityAnnotationReflector
                        .getColumnsNames(entityClass)
                        .collect(Collectors.joining(", ")),
                EntityAnnotationReflector
                        .getTableName(entityClass)
        );

        return query(sql, entityClass);

    }

    public <E extends Entity> E findById(Class<E> entityClass, int id)
    throws SQLException, HydrationException {

        var sql = String.format(
                "SELECT %1$s FROM %2$s WHERE %3$s = %4$s",
                EntityAnnotationReflector
                        .getColumnsNames(entityClass)
                        .collect(Collectors.joining(", ")),
                EntityAnnotationReflector
                        .getTableName(entityClass),
                "id_column",
                id
        );

        return query(sql, entityClass).get(0);
    }

    public <E extends Entity> E insertNew(E entity)
    throws SQLException, HydrationException {
        // FIXME: manage transaction and retrurn hydrated entity

        var sql = String.format(
                "INSERT INTO %1$s (%2$s) VALUES (%3$s); ",
                EntityAnnotationReflector
                        .getTableName(entity.getClass()),

                EntityAnnotationReflector
                        .getColumnAnnotatedFields(entity.getClass())
                        .filter(field -> !field.isAnnotationPresent(Identity.class))
                        .map(field -> field.getDeclaredAnnotation(Column.class).value())
                        .collect(Collectors.joining(", ")),

                EntityAnnotationReflector
                        .getColumnAnnotatedFields(entity.getClass())
                        .filter(field -> !field.isAnnotationPresent(Identity.class))
                        .map(field -> {
                            // FIXME : Catch correctly the exception
                            try {
                                return "'" + field.get(entity).toString() + "'";
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .collect(Collectors.joining(", "))
        );

        query(sql, entity.getClass());

        return entity;
    }

    public <E extends Entity> E updateById(E entity, int id) {

        return entity;
    }

    /**
     * Create a search query using the sqlite FTS5 MATCH operator
     * You can only use this query generator with entity that represent a virtual table with FTS5
     *
     * @param entityClass The entity you wish to hydrate with the results of the query
     * @param tokens      All the tokens you want to search
     */
    public <E extends Entity> List<E> textSearch(Class<E> entityClass, String[] tokens)
    throws SQLException, HydrationException {

        var sql = String.format(
                "SELECT %1$s FROM %2$s WHERE %3$s MATCH '%4$s' ORDER BY rank",
                // Fill the list of columns to retrieve
                EntityAnnotationReflector
                        .getColumnsNames(entityClass)
                        .collect(Collectors.joining(", ")),
                // Fill the table
                EntityAnnotationReflector
                        .getTableName(entityClass),
                // Fill where you want to search
                EntityAnnotationReflector
                        .getTableName(entityClass),
                // The FTS5 match query string
                Arrays.stream(tokens)
                      .map(t -> String.format("\" %1$s \" *", t))
                      .collect(Collectors.joining(" "))
        );

        return query(sql, entityClass);
    }

}
