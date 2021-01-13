package database;

import database.entity.Entity;
import database.entity.annotations.Column;
import database.entity.annotations.Identity;
import database.entity.annotations.Table;
import database.entity.reflector.Reflector;
import database.exceptions.HydrationException;
import database.exceptions.SqlQueryFormattingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Add specific requests query bulders to BaseRepository
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
    throws SQLException, HydrationException, SqlQueryFormattingException {

        // Generate a select query with parameters annotated on the Entity class
        var query = formatSqlQuery(
                // Query template
                "SELECT %1$s FROM %2$s",
                // Get the columns names
                Reflector.of(entityClass)
                         .names(Column.class)
                         .collect(Collectors.joining(", "))
                ,
                // Get the table name
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
        );

        return query(query, entityClass);

    }

    public <E extends Entity> E findById(Class<E> entityClass, int id)
    throws SqlQueryFormattingException, SQLException, HydrationException {

        var query = formatSqlQuery(
                "SELECT %1$s FROM %2$s WHERE %3$s = %4$s",
                // Get the columns names
                Reflector.of(entityClass)
                         .names(Column.class)
                         .collect(Collectors.joining(", "))
                ,
                // Get the table name
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // Get a column annotated with Identity
                Reflector.of(entityClass)
                         .is(Identity.class)
                         .firstName(Column.class)
                         .orElseThrow()
                ,
                id
        );

        return query(query, entityClass).get(0);
    }

    public <E extends Entity> int insertNew(E entity)
    throws SQLException, SqlQueryFormattingException {

        var query = formatSqlQuery(
                "INSERT INTO %1$s (%2$s) VALUES (%3$s); ",

                // Get the table name
                Reflector.of(entity.getClass())
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // Get the fields names annotated with Column annotation ommiting fields with Identity annotation
                Reflector.of(entity.getClass())
                         .is(Column.class)
                         .not(Identity.class)
                         .names(Column.class)
                         .collect(Collectors.joining(", "))
                ,
                // Get the fields values annotated with Column annotation ommiting fields with Identity annotation
                Reflector.of(entity.getClass())
                         .is(Column.class)
                         .not(Identity.class)
                         .values(entity)
                         .map(v -> "'" + v + "'")
                         .collect(Collectors.joining(", "))
        );

        return execute(query);
    }

    /**
     * Create a search query using the sqlite FTS5 MATCH operator
     * You can only use this query generator with entity that represent a virtual table with FTS5
     *
     * @param entityClass The entity you wish to hydrate with the results of the query
     * @param tokens      All the tokens you want to search
     */
    public <E extends Entity> List<E> textSearch(Class<E> entityClass, String[] tokens)
    throws SQLException, HydrationException, SqlQueryFormattingException {

        var query = formatSqlQuery(
                "SELECT %1$s FROM %2$s WHERE %3$s MATCH '%4$s' ORDER BY rank",
                // Fill the list of columns to retrieve
                Reflector.of(entityClass)
                         .names(Column.class)
                         .collect(Collectors.joining(", "))
                ,
                // Fill the table
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // Fill where you want to search
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // The FTS5 match query string
                Arrays.stream(tokens)
                      .map(t -> String.format("\" %1$s \" *", t))
                      .collect(Collectors.joining(" "))
        );

        return query(query, entityClass);
    }

}
