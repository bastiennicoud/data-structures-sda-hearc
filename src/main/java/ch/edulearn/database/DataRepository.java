package ch.edulearn.database;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.annotations.Identity;
import ch.edulearn.database.entity.annotations.Searchable;
import ch.edulearn.database.entity.annotations.Table;
import ch.edulearn.database.entity.hydrator.exceptions.HydrationException;
import ch.edulearn.database.entity.reflector.Reflector;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides some specific query builders for find, insert and text search on sqlite FTS5 table
 */
public class DataRepository extends BaseRepository {

    /**
     * @param dbConnection an active connection to the db
     */
    public DataRepository(Connection dbConnection) {

        super(dbConnection);
    }

    /**
     * Retrieve all elements of an entity from the DB
     *
     * @param entityClass The entity you wish to hydrate with the results of the query
     * @return A collection of the entity type
     */
    public <E extends Entity> List<E> findAll(Class<E> entityClass, int limit)
    throws SQLException, HydrationException, SqlQueryFormattingException {

        // Generate a select query with parameters annotated on the Entity class
        var query = formatSqlQuery(
                // Query template
                "SELECT %1$s FROM %2$s LIMIT %3$s",
                // Get the columns names
                Reflector.of(entityClass)
                         .names(Column.class)
                         .collect(Collectors.joining(", "))
                ,
                // Get the table name
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow(),
                limit
        );

        return query(query, entityClass, limit);

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

        return query(query, entityClass, 1).get(0);
    }

    /**
     * @param entity The entity you whant to insert in the databse
     * @param <E>    Type of the entity
     * @return The entity hydrated with the database generated ID
     * @throws IllegalAccessException The Identity field cannot be accesed
     */
    public <E extends Entity> E insertNew(E entity)
    throws SQLException, SqlQueryFormattingException, IllegalAccessException {

        // Generate an insert query with the parameters from the entity annotations
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

        // Execute query and save inserted ID
        var insertedId = execute(query);
        // Retrieve the entity Identity field by reflexion
        var identityField = Reflector
                .of(entity.getClass())
                .is(Identity.class)
                .findFirst();

        // Set the identity field by reflexion (if there is a identity field in the entity
        if (identityField.isPresent())
            identityField.get().set(entity, insertedId);

        return entity;
    }

    /**
     * Create a search query using the sqlite FTS5 MATCH operator
     * You can only use this query generator with entity that represent a virtual table with FTS5
     *
     * @param entityClass The entity you wish to hydrate with the results of the query
     * @param tokens      All the tokens you want to search
     * @param limit       Sql results limit
     */
    public <E extends Entity> List<E> textSearch(Class<E> entityClass, String[] tokens, int limit)
    throws SQLException, HydrationException, SqlQueryFormattingException {

        // Get the table name
        var tableName =
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow();

        // Generate the specific sqlite FTS5 text search query
        var query = formatSqlQuery(
                "SELECT %1$s, %2$s FROM %3$s WHERE %3$s MATCH '%4$s' ORDER BY bm25(%3$s, %5$s) LIMIT %6$s",
                // The list of highlightable fields annotaded with searchable
                String.join(", ", getFormattedHighlightedColumn(entityClass, tableName)),
                // Other fields to retrieve from the table
                // Fill the table
                Reflector.of(entityClass)
                         .is(Column.class)
                         .not(Searchable.class)
                         .names(Column.class)
                         .collect(Collectors.joining(", ")),
                // The table name
                tableName,
                // The FTS5 match query string
                Arrays.stream(tokens)
                      .map(t -> String.format("\" %1$s \" *", t))
                      .collect(Collectors.joining(" ")),
                // Retrieve the search weight form the Searchable annotations
                Reflector.of(entityClass)
                         .is(Searchable.class)
                         .names(Searchable.class)
                         .collect(Collectors.joining(", ")),
                limit
        );

        return query(query, entityClass, limit);
    }

    /**
     * Generate a sqlite FTS5 highlight function select
     * (see https://www.sqlite.org/fts5.html#the_highlight_function)
     */
    private <E extends Entity> List<String> getFormattedHighlightedColumn(Class<E> entityClass, String tableName) {
        // Get all the searchable fields column name
        var searchAnnotatedFields =
                Reflector.of(entityClass)
                         .is(Searchable.class)
                         .names(Column.class)
                         .collect(Collectors.toList());

        // Generate the highlight sql function with the column index
        for (var i = 0; i < searchAnnotatedFields.size(); i++) {
            searchAnnotatedFields.set(
                    i,
                    String.format(
                            "highlight(%1$s, %2$s, '<mark>', '</mark>') AS %3$s",
                            tableName,
                            i,
                            searchAnnotatedFields.get(i)
                    )
            );
        }
        return searchAnnotatedFields;
    }

}
