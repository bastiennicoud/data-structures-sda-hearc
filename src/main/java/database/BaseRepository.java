package database;

import database.entity.Entity;
import database.entity.EntityHydrator;
import database.exceptions.HydrationException;
import database.exceptions.SqlQueryFormattingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Repository base class
 * Provides generic methods for :
 * - Inserting in the db
 * - Queriing the databse
 * <p>
 * Can be extended to provide specifc use cases query builder methods
 */
public class BaseRepository {

    protected final Connection dbConnection;

    /**
     * @param dbConnection an active connection to the db
     */
    public BaseRepository(Connection dbConnection) {

        this.dbConnection = dbConnection;

    }

    /**
     * Perform a query that neet to return a result set.
     * Will try to hydrate the entityClass type with the retruned results.
     * <p>
     * Canno't be uset with INSERT, UPDATE, or DELETE statements
     *
     * @param query       The query statement you want to execute
     * @param entityClass The class you want to hydrate with the query results,
     *                    the query must be compatible with the Entity hydrate method
     * @param <E>         Entity type
     * @return A collection of hydrated Entity
     */
    public <E extends Entity> ArrayList<E> query(String query, Class<E> entityClass)
    throws SQLException, HydrationException {

        try (var statement = dbConnection.createStatement();) {
            var results = statement.executeQuery(query);
            return EntityHydrator.hydrate(results, entityClass);
        }

    }

    /**
     * Perform a query that neet to return a result set.
     * Will try to hydrate the entityClass type with the retruned results.
     * <p>
     * Canno't be uset with INSERT, UPDATE, or DELETE statements
     *
     * @param query The query statement you want to execute
     */
    public int execute(String query)
    throws SQLException {

        // SQL query execution
        try (var statement = dbConnection.createStatement()) {
            return statement.executeUpdate(query);
        }

    }

    protected String formatSqlQuery(String queryFormat, Object... args) throws SqlQueryFormattingException {

        try {
            return String.format(queryFormat, args);
        } catch (Exception e) {
            System.out.println("Error while trying to format the sql query :");
            System.out.println("Provided query format :" + queryFormat);
            System.out.println("Provided query arguments :" + args);
            e.printStackTrace();

            throw new SqlQueryFormattingException();
        }

    }

}
