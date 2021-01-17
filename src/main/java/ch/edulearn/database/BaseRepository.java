package ch.edulearn.database;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.hydrator.Hydrator;
import ch.edulearn.database.entity.hydrator.exceptions.HydrationException;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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

        try (var statement = dbConnection.createStatement()) {
            var results = statement.executeQuery(query);
            return Hydrator.of(entityClass).hydrate(results, 20);
        }

    }

    /**
     * Perform am UPDAT, CREATE or DELETE statement
     * **SUPPORT only one line update for now !!!***
     * Return the inserted id ! (If you insert multiple lines you will not be wrned,
     * but you will retrieve only the first inserted id).
     *
     * @param query The query statement you want to execute
     * @return An array with all the inserted ids
     */
    public int execute(String query)
    throws SQLException {

        // SQL query execution
        try (var statement = dbConnection.createStatement()) {

            statement.executeUpdate(query);
            // Get the generated keys
            var keys = statement.getGeneratedKeys();
            // Return the last inserted id
            return keys.getInt(1);
        }

    }

    /**
     * @param queryFormat A <a href="../util/Formatter.html#syntax">format string</a>
     * @param args        Arguments referenced by the format specifiers in the format string.
     * @return Raw SQL query with arguments binded in the base query string
     * @throws SqlQueryFormattingException Problem whilt trying to format the query
     */
    protected String formatSqlQuery(String queryFormat, Object... args) throws SqlQueryFormattingException {

        try {
            var sql = String.format(queryFormat, args);

            // Show the generated query in the console
            System.out.println(sql);
            return sql;
        } catch (Exception e) {
            System.out.println("Error while trying to format the sql query :");
            System.out.println("Provided query format :" + queryFormat);
            System.out.println("Provided query arguments :" + Arrays.toString(args));
            e.printStackTrace();

            throw new SqlQueryFormattingException();
        }

    }

}
