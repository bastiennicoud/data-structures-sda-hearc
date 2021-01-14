package ch.edulearn.database.entity.hydrator;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.hydrator.exceptions.EntityHydrationException;
import ch.edulearn.database.entity.hydrator.exceptions.FieldHydrationException;
import ch.edulearn.database.entity.hydrator.exceptions.HydrationException;
import ch.edulearn.database.entity.reflector.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
     * @param results A result set from the ch.edulearn.database that match the entity you want to hydrate
     * @return An ArrayList of the hydrated ch.edulearn.entities
     * @throws HydrationException If there is an error during the hydration process
     */
    public ArrayList<E> hydrate(ResultSet results) throws HydrationException {

        try {

            var datas = new ArrayList<E>(20);

            // Iterates trough the query results
            while (results.next()) {

                var entity = hydrateEntity(
                        entityClass.getConstructor().newInstance(),
                        results
                );

                datas.add(entity);
            }

            return datas;

        } catch (NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                SQLException |
                EntityHydrationException |
                InvocationTargetException e) {

            throw new HydrationException(
                    "Error while trying to hydrate the database results to the " + entityClass.getName() + " entity.",
                    e
            );

        }

    }

    /**
     * @param entity  A fresh instance of the entity to hydrate
     * @param results The result set a the tuple you want to hydrate from
     * @return The entity fullfilled with the datas form the resultset tuple
     * @throws EntityHydrationException Problem trying to get the values from the result set, converting types, or
     *                                  trying to access the entity fields
     */
    private E hydrateEntity(E entity, ResultSet results) throws EntityHydrationException {

        try {

            // Get the fields declared with a column annotation of the entity
            var fields = Reflector.of(entity.getClass()).is(Column.class).toArray();

            // Try to hydrate each fields
            for (var f : fields) {
                f.set(entity, hydrateField(f, results));
            }

            return entity;

        } catch (IllegalAccessException | SQLException | FieldHydrationException e) {

            throw new EntityHydrationException(
                    "Error while trying to hydrate " + entity.getClass().getName() + "entity.",
                    e
            );

        }

    }

    /**
     * Get the value form the result set for a specific field of the entiry.
     * The field must be annotated fir the Column annotation to retrieve the
     * corresponding value from the result set.
     *
     * @return The value of the corresponding column for the field
     * @throws SQLException            Throws if its impossible to get the column ata from the result set
     * @throws FieldHydrationException Throws if the type is unsupported by the hydrator
     */
    private Object hydrateField(Field field, ResultSet resultSet)
    throws SQLException, FieldHydrationException {

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
            default -> throw new FieldHydrationException(
                    "Cannot hydrate this type of field. Type unsupported by hydrator."
            );
        };
    }

}
