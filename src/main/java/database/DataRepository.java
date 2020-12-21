package database;

import database.entity.Entity;
import database.entity.EntityAnnotationReflector;
import database.entity.EntityHydrator;
import database.exceptions.HydrationException;

import java.sql.*;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A class to make call's to the database via shortcuts.
 * Automatically hydrate Entities via reflected attributes.
 * Does not provide verification between SQL returned tuples and entities.
 * You need to provide a SQL query that return the right datas for the matching Entity.
 * Otherwise risk encountering data inconstancy or hydration errors.
 */
public class DataRepository {

    private final Connection dbConnection;

    /**
     * @param dbConnection   an active connection to the db
     */
    public DataRepository(Connection dbConnection) {

        this.dbConnection = dbConnection;

    }

    /**
     * @param query       The query statement you want to execute
     * @param entityClass The class you want to hydrate with the query results,
     *                    the query must be compatible with the Entity hydrate method
     * @param <E>         Entity type
     * @return A collection of hydrated Entity
     */
    public <E extends Entity> Collection<E> query(String query, Class<E> entityClass)
    throws SQLException, HydrationException {

        // SQL query execution
        Statement statement = dbConnection.createStatement();
        ResultSet results = statement.executeQuery(query);

        return EntityHydrator.hydrate(results, entityClass);

    }

    /**
     * Shortcut to a select all
     * Find all elements from the table corresponding to the entity
     *
     * @param entityClass The entity for which you want to retrieve datas
     * @return A collection of the entity type
     */
    public <E extends Entity> Collection<E> findAll(Class<E> entityClass)
    throws SQLException, HydrationException {

        // Generate a select query with parameters annotated on the Entity class
        String sql = String.format(
                "SELECT %1$s FROM %2$s",
                EntityAnnotationReflector
                        .getColumnsNames(entityClass)
                        .collect(Collectors.joining(", ")),
                EntityAnnotationReflector
                        .getTableName(entityClass)
        );

        return query(sql, entityClass);

    }

}
