package database.entity;

import database.entity.annotations.Column;
import database.entity.reflector.Reflector;
import database.exceptions.HydrationException;
import database.exceptions.HydrationFieldException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Is responsible of the hydration of a specifc entity from the datas of a result set.
 */
public class EntityHydrator<E extends Entity> implements Hydrator<E> {

    private final Class<E> entityClass;

    public EntityHydrator(Class<E> entityClass) {

        this.entityClass = entityClass;
    }

    /**
     * Hydrate the entity type form a result set
     *
     * @param results A result set from the database that match the entity you want to hydrate
     * @return An ArrayList of the hydrated entities
     * @throws HydrationException If there is an error during the hydration process
     */
    public ArrayList<E> hydrate(ResultSet results) throws HydrationException {

        var datas = new ArrayList<E>();

        try {

            System.out.println("Fetch size" + results.getFetchSize());
            // Iterates trough the query results
            while (results.next()) {

                var entity = entityClass.getConstructor().newInstance();
                Stream<Field> fields = Reflector.of(entity.getClass()).is(Column.class).stream();

                fields.forEach(f -> {
                    try {
                        f.set(entity, hydrateField(f, results));
                    } catch (IllegalAccessException | SQLException | HydrationFieldException e) {
                        System.out.println("Hydration error");
                        e.printStackTrace();
                    }
                });

                datas.add(entity);
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            throw new HydrationException(
                    "Reflexion error, Impossible to get the constructor of the Entity.",
                    e
            );

        } catch (SQLException e) {

            throw new HydrationException(
                    "Hydration error, Impossible to get the datas for hydration.",
                    e
            );

        }

        return datas;
    }

    /**
     * Get the value form the result set for a specific field of the entiry.
     * The field must be annotated fir the Column annotation to retrieve the
     * corresponding value from the result set.
     *
     * @return The value of the corresponding column for the field
     * @throws SQLException            Throws if its impossible to get the column ata from the result set
     * @throws HydrationFieldException Throws if the type is unsupported by the hydrator
     */
    private Object hydrateField(Field field, ResultSet resultSet)
    throws SQLException, HydrationFieldException {

        // Check the field type to use the correct method to retrieve the column from the result set
        var columnName = field.getDeclaredAnnotation(Column.class).value();

        // Check the field type to call the correct getter on the result set
        return switch (field.getType().getName()) {
            case "int" -> resultSet.getInt(columnName);
            case "java.lang.String" -> resultSet.getString(columnName);
            case "long" -> resultSet.getLong(columnName);
            case "short" -> resultSet.getShort(columnName);
            case "boolean" -> resultSet.getBoolean(columnName);
            case "byte" -> resultSet.getByte(columnName);
            case "float" -> resultSet.getFloat(columnName);
            case "java.math.BigDecimal" -> resultSet.getBigDecimal(columnName);
            case "java.sql.Date" -> resultSet.getDate(columnName);
            case "java.sql.Time" -> resultSet.getTime(columnName);
            case "java.sql.Timestamp" -> resultSet.getTimestamp(columnName);
            // Throws a exception to indicate that the hydrator dont know hot to hydrate this field type
            default -> throw new HydrationFieldException(
                    "Cannot hydrate this type of field. Type unsupported by hydrator."
            );
        };
    }

}
