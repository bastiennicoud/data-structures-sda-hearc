package ch.edulearn.database;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.*;
import ch.edulearn.database.entity.reflector.Reflector;
import ch.edulearn.database.exceptions.SqlQueryFormattingException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * This specific repository contain requests to generate the database scema
 */
public class SchemaDatabaseRepository extends BaseRepository {

    /**
     * @param dbConnection an active connection to the db
     */
    public SchemaDatabaseRepository(Connection dbConnection) {

        super(dbConnection);
    }

    public <E extends Entity> int createTable(Class<E> entityClass)
    throws SQLException, SqlQueryFormattingException {

        var query = formatSqlQuery(
                "CREATE TABLE %1$s (%2$s);",

                // Get the table name
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // Get the fields names annotated with Column annotation ommiting fields with Identity annotation
                Reflector.of(entityClass)
                         .is(Column.class)
                         .stream()
                         .map(this::generateSqlColumnDefinition)
                         .collect(Collectors.joining(", "))
        );

        return execute(query);
    }

    public <E extends Entity> int createFTS5Table(Class<E> entityClass)
    throws SQLException, SqlQueryFormattingException {

        var query = formatSqlQuery(
                "CREATE VIRTUAL TABLE %1$s USING FTS5 (%2$s);",

                // Get the table name
                Reflector.of(entityClass)
                         .getClassAnnotationValue(Table.class)
                         .orElseThrow()
                ,
                // Get the fields names annotated with Column annotation ommiting fields with Identity annotation
                Reflector.of(entityClass)
                         .is(Column.class)
                         .stream()
                         // Specifc mapping to add the unindexed behavior
                         .map(f -> {
                             var columnName = f.getDeclaredAnnotation(Column.class).value();
                             return f.isAnnotationPresent(Searchable.class) ? columnName : columnName + " UNINDEXED";
                         })
                         // Get all columns names, then add to the end a prefix command (specifix to sqlite FTS5)
                         .collect(Collectors.joining(", ")) + ", prefix=1, prefix=2"
        );

        return execute(query);
    }

    /**
     * Generate the sql command for a column from a field
     * Fields must be annotated with Column
     * Support specific behavior for Identity and NotNull annotations
     */
    private String generateSqlColumnDefinition(Field f) {

        var columnName = f.getDeclaredAnnotation(Column.class).value();
        var columnType = f.getType();

        if (f.isAnnotationPresent(Identity.class)) {
            return String.format(
                    "%1$s %2$s PRIMARY KEY AUTOINCREMENT",
                    columnName,
                    getSqlTypeFromJavaType(columnType)
            );
        } else if (f.isAnnotationPresent(NotNull.class)) {
            return String.format(
                    "%1$s %2$s NOT NULL",
                    columnName,
                    getSqlTypeFromJavaType(columnType)
            );
        } else {
            return String.format(
                    "%1$s %2$s",
                    columnName,
                    getSqlTypeFromJavaType(columnType)
            );
        }
    }

    /**
     * Get the sqlite type form a java type
     * Support only types supported by the sqlite jdbc
     */
    private String getSqlTypeFromJavaType(Class<?> type) {

        return switch (type.getName()) {
            case "int" -> "INTEGER";
            case "short", "long", "boolean", "java.math.BigDecimal" -> "NUMERIC";
            case "java.lang.String", "java.sql.Date", "java.sql.Time", "java.sql.Timestamp" -> "TEXT";
            case "byte" -> "BLOB";
            case "float" -> "REAL";
            // Unknown type
            default -> "";
        };
    }

}
