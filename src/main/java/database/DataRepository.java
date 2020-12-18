package database;

import database.entity.Entity;
import database.annotations.Field;
import database.annotations.Table;
import database.entity.EntityAnnotationReflector;
import database.exceptions.HydrationException;

import java.sql.*;
import java.util.Arrays;
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
    private final EntityHydrator entityHydrator;

    /**
     * @param dbConnection   an active connection to the db
     * @param entityHydrator Inject an initialised EntityHydrator
     */
    public DataRepository(Connection dbConnection, EntityHydrator entityHydrator) {

        this.dbConnection = dbConnection;
        this.entityHydrator = entityHydrator;

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

        return entityHydrator.hydrate(results, entityClass);

    }

    /**
     * Shortcut to a select all
     * Find all elements from the table corresponding to the entity
     *
     * @param entityClass The entity for which you want to retrieve datas
     * @return A collection of the entity type
     */
    public <E extends Entity> Collection<E> findAll(Class<E> entityClass) {

        EntityAnnotationReflector<E> entityReflector = new EntityAnnotationReflector<E>(entityClass);

        // Generate a select query with parameters annotated on the Entity class
        String sql = String.format(
                "SELECT %1$s FROM %2$s",
                entityReflector.getColumnsNames().collect(Collectors.joining(", ")),
                entityReflector.getTableName()
        );

        System.out.println(sql);

        return null;
    }

}
